import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";
import { selectRole } from "../render.js";

// Endpoints
const ADMIN_API = API_BASE_URL + "/admin/login";
const DOCTOR_API = API_BASE_URL + "/doctor/login";

// Ejecutar al cargar la página
window.onload = function () {
  // Botón Admin
  const adminBtn = document.getElementById("btnAdmin");
  if (adminBtn) {
    adminBtn.addEventListener("click", () => {
      openModal("adminLogin");
    });
  }

  // Botón Doctor
  const doctorBtn = document.getElementById("btnDoctor");
  if (doctorBtn) {
    doctorBtn.addEventListener("click", () => {
      openModal("doctorLogin");
    });
  }
};

/**
 * Login de Admin
 */
window.adminLoginHandler = async function () {
  const username = document.getElementById("adminUsername")?.value;
  const password = document.getElementById("adminPassword")?.value;

  if (!username || !password) {
    alert("Por favor ingresa usuario y contraseña");
    return;
  }

  try {
    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole("admin");
      alert("Inicio de sesión de Admin exitoso");
    } else {
      alert("¡Credenciales inválidas!");
    }
  } catch (error) {
    console.error("Error:", error);
    alert("Error al iniciar sesión como Admin");
  }
};

/**
 * Login de Doctor
 */
window.doctorLoginHandler = async function () {
  const email = document.getElementById("doctorEmail")?.value;
  const password = document.getElementById("doctorPassword")?.value;

  if (!email || !password) {
    alert("Por favor ingresa email y contraseña");
    return;
  }

  try {
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole("doctor");
      alert("Inicio de sesión de Doctor exitoso");
    } else {
      alert("¡Credenciales inválidas!");
    }
  } catch (error) {
    console.error("Error:", error);
    alert("Error al iniciar sesión como Doctor");
  }
};
