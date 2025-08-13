// util.js
// Funciones auxiliares para index.html y el modal

// Guardar rol seleccionado y abrir modal de login
function selectRole(role) {
  // Si llegamos a la página principal, limpiamos token antiguo
  localStorage.removeItem('userRole'); // garantizamos empezar limpio
  localStorage.setItem('userRole', role);
  openModalForRole(role);
}

// Mostrar modal y customizar título según rol
function openModalForRole(role) {
  const modal = document.getElementById('modal');
  const title = document.getElementById('modal-title');
  if (role === 'admin') {
    title.textContent = 'Iniciar sesión - Administrador';
  } else if (role === 'doctor') {
    title.textContent = 'Iniciar sesión - Doctor';
  } else {
    title.textContent = 'Iniciar sesión - Paciente';
  }
  // limpiar campos
  document.getElementById('loginEmail').value = '';
  document.getElementById('loginPwd').value = '';
  modal.classList.add('active');
  modal.setAttribute('aria-hidden', 'false');
}

// Cerrar modal
function closeModal() {
  const modal = document.getElementById('modal');
  modal.classList.remove('active');
  modal.setAttribute('aria-hidden', 'true');
}

// Manejo simple de login (demo). Aquí iría fetch a tu API.
async function submitLoginMock() {
  const email = document.getElementById('loginEmail').value.trim();
  const pwd = document.getElementById('loginPwd').value.trim();
  const role = localStorage.getItem('userRole') || 'patient';

  if (!email || !pwd) {
    alert('Por favor complete email y contraseña.');
    return;
  }

  // Mock: en una implementación real, enviarías fetch a /api/auth/login
  // Por ahora guardamos un token de ejemplo y redirigimos según rol
  const fakeToken = 'token_demo_' + Date.now();
  localStorage.setItem('token', fakeToken);

  // Si es patient guardamos loggedPatient (ejemplo)
  if (role === 'patient') {
    localStorage.setItem('userRole', 'loggedPatient');
    window.location.href = '/pages/patientDashboard.html';
  } else if (role === 'doctor') {
    localStorage.setItem('userRole', 'doctor');
    window.location.href = '/templates/doctor/doctorDashboard.html';
  } else {
    localStorage.setItem('userRole', 'admin');
    window.location.href = '/templates/admin/adminDashboard.html';
  }

  // cerrar modal por si no se redirige
  closeModal();
}
