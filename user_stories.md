# Historias de Usuario

## Administrador

**Title:** Iniciar sesión como administrador
*As an administrator, I want to log into the portal with my username and password, so that I can manage the platform securely.*
**Acceptance Criteria:**

1. The system must validate the username and password against stored credentials.
2. Successful login redirects to the admin dashboard.
3. Failed login attempts show an error message.
   **Priority:** High
   **Story Points:** 3
   **Notes:** - Login must use secure authentication protocols.

**Title:** Cerrar sesión como administrador
*As an administrator, I want to log out of the portal, so that I can protect system access.*
**Acceptance Criteria:**

1. The session is terminated upon logout.
2. The user is redirected to the login page.
3. All session cookies are cleared.
   **Priority:** High
   **Story Points:** 2
   **Notes:** - Ensure session expiration upon logout.

**Title:** Agregar doctores al portal
*As an administrator, I want to add doctors to the portal, so that they can manage their profiles and appointments.*
**Acceptance Criteria:**

1. Admin can input doctor details (name, specialization, contact info).
2. The new doctor receives an account activation email.
3. The doctor appears in the public directory.
   **Priority:** High
   **Story Points:** 5
   **Notes:** - Email verification required.

**Title:** Eliminar perfil de doctor
*As an administrator, I want to remove a doctor’s profile from the portal, so that outdated or inactive profiles are not visible.*
**Acceptance Criteria:**

1. Admin confirms deletion action.
2. Doctor’s data is archived before deletion.
3. The profile is removed from the directory.
   **Priority:** Medium
   **Story Points:** 4
   **Notes:** - Ensure no active appointments remain.

**Title:** Ejecutar procedimiento almacenado para estadísticas
*As an administrator, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics.*
**Acceptance Criteria:**

1. Stored procedure retrieves monthly appointment counts.
2. Output is displayed in CLI format.
3. Data is accurate and up-to-date.
   **Priority:** Medium
   **Story Points:** 3
   **Notes:** - Ensure DB user has EXECUTE privilege.

---

## Paciente

**Title:** Ver lista de doctores sin iniciar sesión
*As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.*
**Acceptance Criteria:**

1. Doctor list is publicly accessible.
2. Each doctor shows name, specialization, and rating.
3. No booking options are available without login.
   **Priority:** Medium
   **Story Points:** 3
   **Notes:** - Cache list for performance.

**Title:** Registrarme como paciente
*As a patient, I want to register using my email and password, so that I can book appointments.*
**Acceptance Criteria:**

1. Registration form validates required fields.
2. Email verification is sent.
3. Successful registration redirects to login page.
   **Priority:** High
   **Story Points:** 4
   **Notes:** - Password must meet security standards.

**Title:** Iniciar sesión como paciente
*As a patient, I want to log into the portal, so that I can manage my bookings.*
**Acceptance Criteria:**

1. Credentials are validated.
2. Successful login loads the patient dashboard.
3. Failed attempts show appropriate error messages.
   **Priority:** High
   **Story Points:** 3
   **Notes:** - Use JWT for authentication.

**Title:** Cerrar sesión como paciente
*As a patient, I want to log out, so that I can secure my account.*
**Acceptance Criteria:**

1. Session is terminated.
2. Redirect to login page.
3. Session cookies are deleted.
   **Priority:** High
   **Story Points:** 2
   **Notes:** - Automatic logout after inactivity.

**Title:** Reservar cita de una hora
*As a patient, I want to book a one-hour appointment with a doctor, so that I can consult about my health.*
**Acceptance Criteria:**

1. Appointment form allows date, time, and doctor selection.
2. Availability is checked before booking.
3. Confirmation email is sent.
   **Priority:** High
   **Story Points:** 5
   **Notes:** - Avoid overlapping appointments.

**Title:** Ver próximas citas
*As a patient, I want to see my upcoming appointments, so that I can prepare accordingly.*
**Acceptance Criteria:**

1. List shows date, time, doctor, and location.
2. Only future appointments are displayed.
3. Cancellations are reflected in real-time.
   **Priority:** Medium
   **Story Points:** 3
   **Notes:** - Sort by nearest date.

---

## Doctor

**Title:** Iniciar sesión como doctor
*As a doctor, I want to log into the portal, so that I can manage my appointments.*
**Acceptance Criteria:**

1. Credentials validated.
2. Dashboard shows upcoming appointments.
3. Failed login shows error message.
   **Priority:** High
   **Story Points:** 3
   **Notes:** - Multi-factor authentication optional.

**Title:** Cerrar sesión como doctor
*As a doctor, I want to log out of the portal, so that I can protect my data.*
**Acceptance Criteria:**

1. Session ends on logout.
2. Redirect to login page.
3. Session cookies cleared.
   **Priority:** High
   **Story Points:** 2
   **Notes:** - Session timeout after inactivity.

**Title:** Ver calendario de citas
*As a doctor, I want to view my appointment calendar, so that I can stay organized.*
**Acceptance Criteria:**

1. Calendar view shows daily, weekly, and monthly appointments.
2. Each entry displays patient name and appointment time.
3. Filters for date and patient.
   **Priority:** Medium
   **Story Points:** 4
   **Notes:** - Sync with external calendar optional.

**Title:** Marcar indisponibilidad
*As a doctor, I want to mark my unavailability, so that patients only see available times.*
**Acceptance Criteria:**

1. Doctor can block off time slots.
2. Blocked slots are hidden from patient booking.
3. Changes update in real-time.
   **Priority:** High
   **Story Points:** 3
   **Notes:** - Prevent overlap with existing appointments.

**Title:** Actualizar perfil
*As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information.*
**Acceptance Criteria:**

1. Profile form updates specialization, contact, and profile picture.
2. Changes are visible on patient-facing pages.
3. Data validation before saving.
   **Priority:** Medium
   **Story Points:** 3
   **Notes:** - Allow optional description.

**Title:** Ver detalles del paciente
*As a doctor, I want to see patient details for upcoming appointments, so that I can be prepared.*
**Acceptance Criteria:**

1. Details include patient history, contact info, and reason for visit.
2. Only visible for confirmed appointments.
3. Data is read-only.
   **Priority:** High
   **Story Points:** 4
   **Notes:** - Ensure HIPAA compliance.
