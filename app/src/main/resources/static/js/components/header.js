function renderHeader() {
  const headerDiv = document.getElementById("header");
  if (!headerDiv) return;

  // Si estamos en la página de inicio, limpiar la sesión
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
  }

  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");
  let headerContent = "";

  // Manejo de sesión inválida
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Sesión expirada o inicio de sesión inválido. Por favor, inicie sesión nuevamente.");
    window.location.href = "/";
    return;
  }

  // Header dinámico según el rol
  if (role === "admin") {
    headerContent = `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Agregar Doctor</button>
      <a href="#" id="logoutBtn">Cerrar sesión</a>
    `;
  } else if (role === "doctor") {
    headerContent = `
      <a href="/doctor-dashboard.html">Inicio</a>
      <a href="#" id="logoutBtn">Cerrar sesión</a>
    `;
  } else if (role === "patient") {
    headerContent = `
      <a href="/login.html">Iniciar sesión</a>
      <a href="/register.html">Registrarse</a>
    `;
  } else if (role === "loggedPatient") {
    headerContent = `
      <a href="/patient-dashboard.html">Inicio</a>
      <a href="/appointments.html">Citas</a>
      <a href="#" id="logoutBtn">Cerrar sesión</a>
    `;
  }

  // Inyectar contenido
  headerDiv.innerHTML = headerContent;
  attachHeaderButtonListeners();
}

// Listeners para botones dinámicos
function attachHeaderButtonListeners() {
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", logout);
  }
}

// Funciones de cierre de sesión
function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("userRole");
  window.location.href = "/";
}

function logoutPatient() {
  localStorage.removeItem("token");
  localStorage.setItem("userRole", "patient");
  window.location.href = "/patient-dashboard.html";
}

renderHeader();
