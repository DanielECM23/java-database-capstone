function renderFooter() {
  const footer = document.getElementById("footer");
  if (!footer) return;

  footer.innerHTML = `
    <footer class="footer">
      <div class="footer-logo">
        <img src="/images/logo.png" alt="Logo" />
        <p>© ${new Date().getFullYear()} Mi Clínica. Todos los derechos reservados.</p>
      </div>
      <div class="footer-columns">
        <div class="footer-column">
          <h4>Empresa</h4>
          <a href="#">Acerca de</a>
          <a href="#">Carreras</a>
          <a href="#">Prensa</a>
        </div>
        <div class="footer-column">
          <h4>Soporte</h4>
          <a href="#">Cuenta</a>
          <a href="#">Centro de Ayuda</a>
          <a href="#">Contacto</a>
        </div>
        <div class="footer-column">
          <h4>Legales</h4>
          <a href="#">Términos</a>
          <a href="#">Política de Privacidad</a>
          <a href="#">Licencias</a>
        </div>
      </div>
    </footer>
  `;
}

renderFooter();
