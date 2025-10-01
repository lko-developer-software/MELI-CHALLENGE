El proyecto lo desarrolle bajo la creación de un servicio de backend para entregar el detalle de un
producto, siguiendo un enfoque de microservicios con principios de resiliencia, escalabilidad,
disponibilidad, monitoreo y seguridad.
El flujo de trabajo se dividió en varias etapas clave:
1. Análisis y Diseño:
o Se investigó el detalle de un producto en la página oficial de Mercado Libre para
definir el modelo de datos de la API(le pase un link con el detalle de un producto a
Gemini para que interpretara que datos necesito para el detalle de un producto).
o Se diseñó una arquitectura de microservicios completa, utilizando conceptos como
ApiGateway, balanceo de carga, Circuit Breaker y registro de servicios (registryserver).
o Se implementó un servicio de seguridad con Auth-server (OAuth 2.0) y un configserver para la configuración centralizada.
o Decidí usar Java con Spring Boot y Spring Cloud para agilizar el desarrollo y la
implementación de estos componentes.
2. Tecnología y Herramientas:
o Stack Principal: Java (17), Spring Boot, Spring Cloud.
o IDE de Desarrollo: Se utilizó Visual Studio Code para la construcción del proyecto.
o Componentes: Los componentes desarrollados son:
- gateway:se encargará de centralizar los consumo a los microservicios, garantizando la seguridad
Auth2.0, la escalabilidad mediante el consumo por balanceador y consumos de contingencia en
caso de que el servicios principal de consulta de detalles de productos falle.
- auth-server: se encargará de aprovisionar la autenticación mediante Auth2.0.
- config-server: Se encargará de aprovisionar los parámetros de conexiones, credenciales a base
de datos y parámetros secretos para la generación de JWT en auth2.0.
- registry-server: este será nuestro Servidor de Registro y Descubrimiento de
Servicios(balanceador).
- product-detail: este será el servicio de consulta de detalles de productos (servicio principal)
- product-detail-fallback: este será el servicio de consulta de detalles de productos (servicio de
respaldo o contingencia)
o Bases de Datos: Se utilizó H2 como base de datos en memoria. La construcción de
los modelos relacionales y el precargue inicial de datos desde un archivo CSV se
gestionó con Liquibase.

Empresa: Mercado Libre - Colombia
Prueba: Challenge - Backend Developer AI
Versión Formato: 1.0
o Herramientas de IA: Se utilizó Gemini para el análisis inicial y GitHub Copilot junto
con Claude Sonnet 4 para la construcción del código, optimizando el tiempo de
desarrollo.
3. Implementación y Pruebas:
o Se crearon los microservicios mencionados, con un servicio principal (ProductDetail) y su respectivo fallback para asegurar la resiliencia del sistema.
o Se implementaron soluciones de monitoreo con Actuator, Prometheus, Zipkin y
Grafana para garantizar la trazabilidad y la observación del sistema.
o El proyecto se configuró para una ejecución sencilla en un entorno local, listo para
pruebas sin necesidad de configuraciones adicionales complejas.
4. Entrega y Documentación:
o Se entregó todo el código en un repositorio de GitHub.
o Se elaboró un archivo README.md para cada servicio, tal como lo solicitaba la
prueba.
o Se preparó una guía de instalación completa, que incluyó un documento detallado
y un video tutorial, explicando el proceso de montaje y pruebas.
o Los insumos se compartieron a través de GitHub, un archivo ZIP y un enlace de
Google Drive, asegurando una entrega completa y accesible.
En resumen, la actividad demostró la capacidad de diseñar, desarrollar y documentar una solución
de backend robusta y escalable, utilizando las mejores prácticas de la industria y herramientas de
vanguardia para la eficiencia y calidad del resultado.
