# Smart Clinic — Schema Design

Este documento define el **diseño de base de datos relacional (MySQL)** y el **diseño de colecciones (MongoDB)** para el Sistema de Gestión de Clínica Inteligente.

---

## MySQL Database Design

> Objetivo: Modelar datos **operativos, estructurados y transaccionales** (pacientes, doctores, usuarios, citas, pagos, disponibilidad, etc.) con integridad referencial.

### Convenciones

* Motor: **InnoDB**, charset **utf8mb4**.
* Timestamps: `created_at` y `updated_at` (`TIMESTAMP` con `DEFAULT CURRENT_TIMESTAMP` y `ON UPDATE CURRENT_TIMESTAMP`).
* Claves primarias: `INT AUTO_INCREMENT`.
* Fechas/horas: `DATETIME` (zona horaria de aplicación) o `TIMESTAMP` si se requiere UTC.
* Soft delete donde aplica: columna `active TINYINT(1) DEFAULT 1` o `deleted_at TIMESTAMP NULL`.

---

### Table: `patients`

* `id` **INT** PK, AUTO\_INCREMENT
* `document_number` **VARCHAR(20)** NOT NULL, **UNIQUE** (cédula u otro ID)
* `first_name` **VARCHAR(60)** NOT NULL
* `last_name` **VARCHAR(60)** NOT NULL
* `birth_date` **DATE** NULL
* `gender` **ENUM('M','F','X')** NULL
* `email` **VARCHAR(120)** NULL, UNIQUE (opcional)
* `phone` **VARCHAR(30)** NULL
* `address` **VARCHAR(200)** NULL
* `active` **TINYINT(1)** NOT NULL DEFAULT 1
* `created_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP
* `updated_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP ON UPDATE CURRENT\_TIMESTAMP

**Notas:** El email se valida a nivel de aplicación (regex). Se conserva historial; no se borran físicamente citas al eliminar pacientes.

---

### Table: `doctors`

* `id` **INT** PK, AUTO\_INCREMENT
* `full_name` **VARCHAR(120)** NOT NULL
* `specialty` **VARCHAR(60)** NOT NULL (ej. "CARDIOLOGY", "PEDIATRICS")
* `email` **VARCHAR(120)** NULL, UNIQUE
* `phone` **VARCHAR(30)** NULL
* `active` **TINYINT(1)** NOT NULL DEFAULT 1
* `created_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP
* `updated_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP ON UPDATE CURRENT\_TIMESTAMP

**Notas:** Las especialidades también pueden normalizarse a una tabla `specialties` si se requieren catálogos.

---

### Table: `admins`

* `id` **INT** PK, AUTO\_INCREMENT
* `username` **VARCHAR(60)** NOT NULL, **UNIQUE**
* `password_hash` **VARCHAR(255)** NOT NULL
* `email` **VARCHAR(120)** NULL, UNIQUE
* `role` **ENUM('SUPER\_ADMIN','ADMIN')** NOT NULL DEFAULT 'ADMIN'
* `active` **TINYINT(1)** NOT NULL DEFAULT 1
* `created_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP
* `updated_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP ON UPDATE CURRENT\_TIMESTAMP

**Notas:** Autenticación/roles mínimos para backoffice.

---

### Table: `appointments`

* `id` **INT** PK, AUTO\_INCREMENT
* `doctor_id` **INT** NOT NULL, **FK** → `doctors(id)` ON UPDATE CASCADE ON DELETE RESTRICT
* `patient_id` **INT** NOT NULL, **FK** → `patients(id)` ON UPDATE CASCADE ON DELETE RESTRICT
* `start_time` **DATETIME** NOT NULL
* `end_time` **DATETIME** NOT NULL
* `status` **ENUM('SCHEDULED','COMPLETED','CANCELLED')** NOT NULL DEFAULT 'SCHEDULED'
* `reason` **VARCHAR(200)** NULL
* `room` **VARCHAR(20)** NULL
* `created_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP
* `updated_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP ON UPDATE CURRENT\_TIMESTAMP

**Índices y reglas:**

* Índice compuesto para búsquedas: `INDEX idx_appt_doctor_time (doctor_id, start_time)`.
* **Evitar solapamientos:** no es trivial en SQL estándar. Se sugiere **validación en capa de servicio**. Opcionalmente una `UNIQUE(doctor_id, start_time)` si los bloques son discretos (p.ej., cada 30 o 60 min redondeados).

**Política de borrado:** `ON DELETE RESTRICT` para preservar historial clínico y facturación.

---

### Table: `doctor_availability`

* `id` **INT** PK, AUTO\_INCREMENT
* `doctor_id` **INT** NOT NULL, **FK** → `doctors(id)` ON UPDATE CASCADE ON DELETE CASCADE
* `day_of_week` **TINYINT** NOT NULL CHECK (day\_of\_week BETWEEN 1 AND 7)  -- 1=Lunes … 7=Domingo
* `start_time` **TIME** NOT NULL
* `end_time` **TIME** NOT NULL
* `recurring` **TINYINT(1)** NOT NULL DEFAULT 1  -- 1 = recurrente semanal
* `created_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP
* `updated_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP ON UPDATE CURRENT\_TIMESTAMP

**Notas:** Soporta ventana recurrente; para excepciones, usar `doctor_unavailability` abajo.

---

### Table: `doctor_unavailability` (opcional pero útil)

* `id` **INT** PK, AUTO\_INCREMENT
* `doctor_id` **INT** NOT NULL, **FK** → `doctors(id)` ON UPDATE CASCADE ON DELETE CASCADE
* `start_datetime` **DATETIME** NOT NULL
* `end_datetime` **DATETIME** NOT NULL
* `reason` **VARCHAR(120)** NULL

**Uso:** Bloquea huecos específicos (vacaciones, congresos) y oculta slots a pacientes.

---

### Table: `payments` (opcional)

* `id` **INT** PK, AUTO\_INCREMENT
* `appointment_id` **INT** NOT NULL, **FK** → `appointments(id)` ON UPDATE CASCADE ON DELETE RESTRICT
* `amount` **DECIMAL(10,2)** NOT NULL
* `currency` **CHAR(3)** NOT NULL DEFAULT 'USD'
* `method` **ENUM('CASH','CARD','TRANSFER','INSURANCE')** NOT NULL
* `status` **ENUM('PENDING','PAID','REFUNDED','FAILED')** NOT NULL DEFAULT 'PENDING'
* `paid_at` **DATETIME** NULL
* `created_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP
* `updated_at` **TIMESTAMP** DEFAULT CURRENT\_TIMESTAMP ON UPDATE CURRENT\_TIMESTAMP

---

### Relaciones clave

* **Patient (1) — (N) Appointment**
* **Doctor (1) — (N) Appointment**
* **Doctor (1) — (N) Availability / Unavailability**
* **Appointment (1) — (0..1) Payment**

---

### Decisiones y justificación

* **Historial clínico**: No se borran citas; se usan flags `active`/`deleted_at` si se requiere ocultar.
* **Solapamiento de citas**: Validado en la **capa de servicio** con consulta de rango (`WHERE doctor_id = ? AND start_time < ? AND end_time > ?`).
* **Disponibilidad médica**: Modelo híbrido (recurrencia + excepciones) para flexibilidad.

---

## MongoDB Collection Design

> Objetivo: Modelar **datos no estructurados/semiestructurados y de rápida evolución**: recetas, notas libres, mensajes/chat, adjuntos y trazas.

### 1) Collection: `prescriptions`

Documenta recetas con estructura flexible y listas de medicamentos.

**Ejemplo de documento:**

```json
{
  "_id": { "$oid": "64abc1234567890abcdef123" },
  "prescriptionCode": "RX-2025-000123",
  "appointmentId": 51,
  "patientId": 67890,
  "doctorId": 54321,
  "medications": [
    { "name": "Amoxicillin", "dosage": "500mg", "frequency": "q8h", "durationDays": 7, "instructions": "Con alimentos" },
    { "name": "Ibuprofen", "dosage": "200mg", "frequency": "q6h PRN", "durationDays": 3 }
  ],
  "tags": ["antibiotic", "pain-relief"],
  "metadata": {
    "createdAt": { "$date": "2025-08-13T12:00:00Z" },
    "updatedAt": { "$date": "2025-08-13T12:00:00Z" },
    "status": "active"
  },
  "notes": "Tomar con agua; suspender si hay reacción alérgica",
  "attachments": [
    { "type": "pdf", "url": "s3://bucket/rx/51.pdf" }
  ]
}
```

**Comentarios de diseño:**

* Se **referencian** `appointmentId`, `patientId`, `doctorId` (números de MySQL) para mantener cohesión con el mundo transaccional.
* Si se requiere independencia total, podría **duplicarse** información básica del paciente/doctor para auditoría.

---

### 2) Collection: `doctor_notes`

Notas libres del médico por cita, con versión y auditoría.

```json
{
  "_id": { "$oid": "64def09876543210fedcba987" },
  "appointmentId": 51,
  "patientId": 67890,
  "doctorId": 54321,
  "title": "Control postoperatorio",
  "content": "Paciente refiere dolor leve. Se indica reposo y analgésico.",
  "labels": ["post-op", "analgesic"],
  "version": 3,
  "createdAt": { "$date": "2025-08-13T12:15:00Z" },
  "updatedAt": { "$date": "2025-08-13T12:40:00Z" }
}
```

---

### 3) Collection: `messages`

Chat simple paciente–doctor por cita o conversación abierta.

```json
{
  "_id": { "$oid": "64aa00bb11cc22dd33ee44ff" },
  "threadId": "MSG-2025-000045",
  "participants": [ { "userId": 67890, "role": "PATIENT" }, { "userId": 54321, "role": "DOCTOR" } ],
  "appointmentId": 51,
  "messages": [
    { "senderId": 67890, "sentAt": { "$date": "2025-08-12T16:05:00Z" }, "text": "¿Debo ayunar antes del examen?" },
    { "senderId": 54321, "sentAt": { "$date": "2025-08-12T16:10:00Z" }, "text": "Sí, 8 horas por favor." }
  ],
  "status": "OPEN",
  "lastUpdated": { "$date": "2025-08-12T16:10:00Z" }
}
```

**Índices sugeridos:** `appointmentId`, `participants.userId`, `lastUpdated`.

---

### 4) Collection: `audit_logs` (opcional)

Trazas de cambios y accesos críticos.

```json
{
  "_id": { "$oid": "64f0c0ffee1234567890abcd" },
  "actor": { "userId": 1001, "role": "ADMIN" },
  "action": "EXECUTE_STORED_PROCEDURE",
  "entity": { "type": "MYSQL", "name": "appointments_per_month" },
  "context": { "ip": "203.0.113.10" },
  "createdAt": { "$date": "2025-08-13T14:20:11Z" }
}
```

---

## Embedding vs Referencing (decisiones)

* **Referenciar (recomendado aquí):** Guardar `patientId/doctorId/appointmentId` como números que apuntan a MySQL mantiene una única fuente de verdad para datos maestros y facilita reportes SQL.
* **Embebido:** Cuando se requiere **inmutabilidad histórica** (ej., snapshot de datos de paciente en la receta), se puede duplicar `patientName`, `dob`, etc. dentro del documento.

---

## Preguntas de diseño (reflexión)

* ¿Qué pasa si se elimina un paciente? ➜ Bloquear borrado duro; usar `active=0` y mantener citas/pagos por trazabilidad.
* ¿Se permite a un médico tener citas solapadas? ➜ No; controlar en servicio con consulta de rango y, si se usan slots discretos, `UNIQUE(doctor_id, start_time)`.
* ¿Las recetas se vinculan a citas? ➜ Sí (preferido) vía `appointmentId` para correlación clínica; también se permite receta independiente con `patientId/doctorId` si no hubo cita.

---

## (Opcional) DDL de arranque

> Snippets de ejemplo para iniciar tablas principales.

```sql
CREATE TABLE patients (
  id INT PRIMARY KEY AUTO_INCREMENT,
  document_number VARCHAR(20) NOT NULL UNIQUE,
  first_name VARCHAR(60) NOT NULL,
  last_name VARCHAR(60) NOT NULL,
  birth_date DATE NULL,
  gender ENUM('M','F','X') NULL,
  email VARCHAR(120) UNIQUE NULL,
  phone VARCHAR(30) NULL,
  address VARCHAR(200) NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE doctors (
  id INT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(120) NOT NULL,
  specialty VARCHAR(60) NOT NULL,
  email VARCHAR(120) UNIQUE NULL,
  phone VARCHAR(30) NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE appointments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  doctor_id INT NOT NULL,
  patient_id INT NOT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME NOT NULL,
  status ENUM('SCHEDULED','COMPLETED','CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
  reason VARCHAR(200) NULL,
  room VARCHAR(20) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_appt_doctor_time (doctor_id, start_time),
  CONSTRAINT fk_appt_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE doctor_availability (
  id INT PRIMARY KEY AUTO_INCREMENT,
  doctor_id INT NOT NULL,
  day_of_week TINYINT NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  recurring TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_av_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON UPDATE CASCADE ON DELETE CASCADE
);
```

---

## Cómo subir este archivo

* En GitHub: **Add file → Create new file** → nombre: `schema-design.md` → pegar contenido → commit directo a `main`.
* Con git:

```bash
git add schema-design.md
git commit -m "Added schema design for Smart Clinic"
git push origin main
```
