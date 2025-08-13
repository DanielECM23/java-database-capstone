// render.js
document.addEventListener('DOMContentLoaded', () => {
  // Si estamos en la raíz (index) limpiamos datos de sesión no deseados
  if (window.location.pathname.endsWith('/') || window.location.pathname.endsWith('/index.html')) {
    // opcional: solo limpiar token si no quieres mantener sesión
    // no borres token si quieres persistencia real
    // localStorage.removeItem('userRole');
    // localStorage.removeItem('token');
  }

  // Botones del index
  const btnAdmin = document.getElementById('btnAdmin');
  const btnDoctor = document.getElementById('btnDoctor');
  const btnPatient = document.getElementById('btnPatient');
  const closeModalBtn = document.getElementById('closeModal');
  const loginSubmit = document.getElementById('loginSubmit');

  // Attach listeners (si existen)
  if (btnAdmin) btnAdmin.addEventListener('click', () => selectRole('admin'));
  if (btnDoctor) btnDoctor.addEventListener('click', () => selectRole('doctor'));
  if (btnPatient) btnPatient.addEventListener('click', () => selectRole('patient'));

  if (closeModalBtn) closeModalBtn.addEventListener('click', closeModal);

  // Cerrar modal con ESC
  document.addEventListener('keydown', (ev) => {
    if (ev.key === 'Escape') closeModal();
  });

  // Submit login (mock)
  if (loginSubmit) loginSubmit.addEventListener('click', (e) => {
    e.preventDefault();
    submitLoginMock();
  });

  // Cerrar modal si el usuario hace click fuera del contenido
  const modal = document.getElementById('modal');
  if (modal) {
    modal.addEventListener('click', (ev) => {
      if (ev.target === modal) closeModal();
    });
  }
});
