# Product Detail Microservice

Microservicio para la gestión de detalles de productos de MercadoLibre desarrollado como parte del desafío técnico MELI.

## 📋 Descripción

Este microservicio proporciona una API REST para consultar detalles completos de productos, incluyendo atributos, información de envío y datos del vendedor. Implementa trazabilidad distribuida, métricas de Prometheus y documentación OpenAPI.

## 🚀 Características Principales

- **API REST** - Endpoints para consulta de productos
- **Trazabilidad Distribuida** - Integración con Zipkin y Micrometer
- **Métricas** - Exportación a Prometheus con métricas personalizadas
- **Documentación OpenAPI** - Swagger UI integrado
- **Base de Datos** - H2 in-memory con Liquibase para migrations
- **Manejo de Errores** - Excepciones personalizadas con trace IDs
- **Estereotipos Swagger** - Anotaciones centralizadas para documentación

## 🛠️ Tecnologías

- **Spring Boot 3.5.6** - Framework principal
- **Java 17** - Versión de Java
- **H2 Database** - Base de datos en memoria
- **Liquibase** - Gestión de esquemas de BD
- **Micrometer Tracing** - Trazabilidad distribuida
- **Zipkin** - Collector de trazas
- **Prometheus** - Métricas y monitoreo
- **OpenAPI/Swagger** - Documentación de API
- **Lombok** - Reducción de boilerplate

## 📁 Estructura del Proyecto

```
src/main/java/com/meli/product_detail/
├── ProductDetailApplication.java          # Clase principal
├── annotations/                          # Estereotipos Swagger personalizados
│   ├── GetAllProductDetails.java
│   ├── GetProductDetailByProductId.java
│   ├── ProductDetailController.java
│   └── ProductIdParameter.java
├── config/                              # Configuraciones
│   ├── OpenApiConfig.java              # Configuración OpenAPI
│   └── TimedAspecConfig.java           # Configuración métricas
├── controllers/                         # Controladores REST
│   └── ProductDetailControllers.java
├── entities/                           # Entidades JPA
│   ├── ProductDetail.java             # Entidad principal
│   ├── Attribute.java                 # Atributos del producto
│   ├── Shipping.java                  # Información de envío
│   ├── Seller.java                    # Información del vendedor
│   └── SellerAddress.java             # Dirección del vendedor
├── exceptions/                         # Manejo de excepciones
│   ├── MeliException.java             # Excepción personalizada
│   ├── MeliExceptionDto.java          # DTO para errores
│   └── SpanErrorHandler.java          # Utilidad para trazas
├── repositories/                       # Repositorios JPA
│   └── ProductDetailRepositories.java
└── services/                          # Servicios de negocio
    ├── ProductDetailService.java      # Interface del servicio
    └── ProductDetailServiceImpl.java  # Implementación del servicio
```

## 🔧 Configuración

### Variables de Entorno

- `SPRING_PROFILES_ACTIVE` - Perfil activo (default: default)
- `SERVER_PORT` - Puerto del servidor (default: 8080)
- `ZIPKIN_URL` - URL del servidor Zipkin (default: http://localhost:9411)

### Endpoints Principales

- `GET /product-detail/detail` - Obtener todos los productos
- `GET /product-detail/detail/{productId}` - Obtener producto por ID
- `GET /product-detail/swagger-ui/index.html` - Documentación Swagger
- `GET /product-detail/actuator/prometheus` - Métricas Prometheus

## 🚦 Ejecución

### Prerequisitos
- Java 17+
- Maven 3.8+

### Ejecutar la aplicación
```bash
# Compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Ejecutar aplicación
./mvnw spring-boot:run
```

### Acceso a servicios
- **Aplicación**: http://localhost:8080/product-detail
- **Swagger UI**: http://localhost:8080/product-detail/swagger-ui/index.html
- **H2 Console**: http://localhost:8080/product-detail/h2-console
- **Métricas**: http://localhost:8080/product-detail/actuator/prometheus

## 📊 Monitoreo y Observabilidad

### Trazabilidad
- Cada request genera un trace ID único
- Integración con Zipkin para visualización de trazas
- Tags personalizados en errores para debugging

### Métricas
- Métricas de timing con `@Timed`
- Exportación automática a Prometheus
- Métricas de aplicación y JVM

### Logging
- Logs estructurados con SLF4J
- Trace ID incluido en logs para correlación

## 🛡️ Manejo de Errores

El microservicio implementa un sistema robusto de manejo de errores:

- **MeliException** - Excepción base con códigos HTTP y mensajes contextuales
- **SpanErrorHandler** - Etiquetado automático de errores en trazas
- **Trace ID** - Generación automática para correlación de errores
- **Respuestas estructuradas** - DTOs consistentes para errores

## 📚 Documentación API

La API está completamente documentada usando OpenAPI 3.0 con estereotipos personalizados que mantienen el código limpio:

- **@ProductDetailController** - Documentación del controlador
- **@GetAllProductDetails** - Documentación endpoint listado
- **@GetProductDetailByProductId** - Documentación endpoint por ID
- **@ProductIdParameter** - Validación y documentación de parámetros

## 🏗️ Arquitectura

El microservicio sigue los principios de Clean Architecture:

1. **Capa de Presentación** - Controllers REST con documentación centralizada
2. **Capa de Aplicación** - Services con lógica de negocio
3. **Capa de Dominio** - Entities y excepciones de dominio
4. **Capa de Infraestructura** - Repositories y configuraciones

## 👤 Autor

**Osneider Manuel Acevedo Naranjo**

## 📄 Licencia

Este proyecto fue desarrollado como parte del desafío técnico de MercadoLibre.

---

*Desarrollado con ❤️ para el desafío MELI*