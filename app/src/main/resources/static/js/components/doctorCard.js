import { deleteDoctor } from "./services/doctorServices.js";
import { getPatientData } from "./services/patientServices.js";
import { showBookingOverlay } from "./bookingOverlay.js";

export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  const role = localStorage.getItem("userRole");

  // Información del doctor
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Especialidad: ${doctor.specialty}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  const availability = document.createElement("p");
  availability.textContent = `Disponibilidad: ${doctor.availability?.join(", ") || "No disponible"}`;

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);

  // Acciones
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Eliminar";
    removeBtn.addEventListener("click", async () => {
      if (!confirm("¿Seguro que quieres eliminar este doctor?")) return;
      const token = localStorage.getItem("token");
      const success = await deleteDoctor(doctor.id, token);
      if (success) card.remove();
    });
    actionsDiv.appendChild(removeBtn);

  } else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Reservar ahora";
    bookNow.addEventListener("click", () => {
      alert("Por favor inicia sesión para reservar.");
    });
    actionsDiv.appendChild(bookNow);

  } else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Reservar ahora";
    bookNow.addEventListener("click", async (e) => {
      const token = localStorage.getItem("token");
      const patientData = await getPatientData(token);
      showBookingOverlay(e, doctor, patientData);
    });
    actionsDiv.appendChild(bookNow);
  }

  // Montar tarjeta
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
