Sección 1: Resumen de la arquitectura
Esta aplicación de Spring Boot combina controladores MVC y REST para servir tanto interfaces web como APIs. Las vistas para el panel de administración y el panel de doctor se generan mediante plantillas Thymeleaf, mientras que las API REST atienden a otros módulos como gestión de citas, historial de pacientes y registros médicos.

La aplicación interactúa con dos bases de datos: MySQL (para datos estructurados como pacientes, doctores, citas y administración) y MongoDB (para documentos flexibles como recetas médicas). Los controladores dirigen las solicitudes a través de una capa de servicio, que centraliza la lógica de negocio y delega las operaciones 
de persistencia a los repositorios correspondientes. En MySQL se usan entidades JPA, mientras que en MongoDB se emplean modelos de documentos anotados con @Document.

Sección 2: Flujo numerado de datos y control
El usuario accede a la aplicación, ya sea a través de paneles web (AdminDashboard, DoctorDashboard) o clientes API (módulos de citas, historial de pacientes, etc.).

La acción solicitada se enruta al controlador correspondiente (Thymeleaf Controller para vistas HTML o REST Controller para peticiones JSON).

El controlador valida la solicitud y delega la lógica de negocio a la capa de servicio.

La capa de servicio aplica reglas, coordina procesos y solicita datos a la capa de repositorio.

El repositorio accede a la base de datos correspondiente: MySQL para datos relacionales o MongoDB para datos documentales.

Los datos obtenidos se mapean a modelos Java: entidades JPA para MySQL o documentos con @Document para MongoDB.

El controlador recibe los modelos y los entrega al cliente: como HTML renderizado (en MVC) o como JSON (en REST).

