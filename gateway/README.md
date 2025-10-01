# API Gateway - Puerta de Enlace de Microservicios

## 📋 Descripción del Proyecto

Este proyecto es un **API Gateway** desarrollado como parte del desafío técnico de MercadoLibre (MELI). Utiliza **Spring Cloud Gateway** para proporcionar una puerta de enlace unificada que gestiona el enrutamiento, la seguridad OAuth2, el circuit breaker y el balanceo de carga hacia los microservicios de la arquitectura distribuida.

## 🏗️ Arquitectura

El proyecto implementa el patrón **API Gateway** utilizando:
- **Spring Boot 3.5.6** como framework principal
- **Spring Cloud Gateway** para enrutamiento reactivo
- **Spring Cloud 2025.0.0** para funcionalidades de microservicios
- **Netflix Eureka Client** para descubrimiento de servicios
- **Resilience4j** para circuit breaker
- **OAuth2** para autenticación y autorización

El API Gateway actúa como punto de entrada único para todos los microservicios:

```
┌─────────────────────────────────────────────────────────────────────┐
│                         API Gateway Architecture                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────┐     ┌─────────────────────┐     ┌─────────────┐   │
│  │   Client    │────▶│    API Gateway      │────▶│   Registry  │   │
│  │ Application │     │   (Port: 4040)      │     │   Server    │   │
│  └─────────────┘     │                     │     │ (Port:8761) │   │
│                      │ ┌─────────────────┐ │     └─────────────┘   │
│                      │ │  Auth Filter    │ │                       │
│                      │ │  (OAuth2)       │ │                       │
│                      │ └─────────────────┘ │                       │
│                      │ ┌─────────────────┐ │                       │
│                      │ │ Circuit Breaker │ │                       │
│                      │ │ (Resilience4j)  │ │                       │
│                      │ └─────────────────┘ │                       │
│                      │ ┌─────────────────┐ │                       │
│                      │ │ Load Balancer   │ │                       │
│                      │ │ (Spring Cloud)  │ │                       │
│                      │ └─────────────────┘ │                       │
│                      └─────────┬───────────┘                       │
│                                │                                   │
│  ┌─────────────────────────────▼─────────────────────────────────┐ │
│  │                    Route to Services                         │ │
│  └─────────────────────────────┬─────────────────────────────────┘ │
│                                │                                   │
│    ┌─────────────┐    ┌─────────▼─────┐    ┌─────────────┐        │
│    │Product      │    │Auth Server    │    │Fallback     │        │
│    │Detail       │    │Service        │    │Service      │        │
│    │Service      │    │               │    │             │        │
│    └─────────────┘    └───────────────┘    └─────────────┘        │
└─────────────────────────────────────────────────────────────────────┘
```

## 🛠️ Tecnologías y Dependencias

### Información del Proyecto
- **GroupId**: `com.meli`
- **ArtifactId**: `gateway`
- **Versión**: `0.0.1`
- **Java Version**: `17`
- **Puerto por defecto**: `4040`

### Dependencias Principales

#### 1. Spring Boot Starter Parent
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.6</version>
</parent>
```
**Propósito**: Proporciona configuraciones base y gestión de dependencias para Spring Boot.

#### 2. Spring Cloud Starter Gateway
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```
**Propósito**: Implementa el API Gateway reactivo para enrutamiento y filtros.

#### 3. Spring Cloud Starter Netflix Eureka Client
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
**Propósito**: Cliente para registrarse y descubrir servicios en Eureka.

#### 4. Spring Cloud Circuit Breaker Reactor Resilience4j
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
</dependency>
```
**Propósito**: Implementa circuit breaker para tolerancia a fallos.

#### 5. Lombok (Opcional)
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
**Propósito**: Reduce código boilerplate con anotaciones.

#### 6. Spring Boot Starter Test (Test Scope)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```
**Propósito**: Framework de testing que incluye JUnit, Mockito, AssertJ y otras librerías de testing.

### Gestión de Dependencias Spring Cloud
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 📁 Estructura del Proyecto

```
gateway/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── meli/
│   │   │           └── gateway/
│   │   │               ├── GatewayApplication.java
│   │   │               ├── beans/
│   │   │               │   └── GatewayBeans.java
│   │   │               ├── controller/
│   │   │               ├── dtos/
│   │   │               │   └── TokenDto.java
│   │   │               └── filters/
│   │   │                   └── AuthFilter.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── banner.txt
│   └── test/
├── target/                     # Archivos compilados
│   ├── classes/
│   ├── generated-sources/
│   └── maven-status/
├── mvnw                        # Maven Wrapper (Unix)
├── mvnw.cmd                    # Maven Wrapper (Windows)
├── pom.xml                     # Configuración de Maven
├── HELP.md                     # Documentación de referencia
├── README.md                   # Este archivo
└── Run.md                      # Guía de ejecución
```

## 🔧 Configuración

### application.yml
```yaml
spring:
  application:
    name: gateway               # Nombre de la aplicación
  profiles:
    default: meli-auth         # Perfil por defecto
    banner:
      location: classpath:banner.txt
  main:
    banner-mode: console
server:
  port: 4040                   # Puerto del API Gateway
eureka:
  instance:
    instance-id: "${spring.application.name}:${random.value}"
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Configuración de la Aplicación Principal

La clase `GatewayApplication` utiliza las siguientes anotaciones:

- `@SpringBootApplication`: Habilita la configuración automática de Spring Boot
- `@EnableDiscoveryClient`: Habilita el cliente Eureka para discovery

```java
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
```

## 🚀 Ejecución del Proyecto

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+ (o usar el wrapper incluido)

### Ejecutar con Maven Wrapper (Recomendado)

#### Windows:
```powershell
.\mvnw.cmd spring-boot:run
```

#### Unix/Linux/MacOS:
```bash
./mvnw spring-boot:run
```

### Ejecutar con Maven instalado localmente
```bash
mvn spring-boot:run
```

### Ejecutar el JAR compilado
```bash
java -jar target/gateway-0.0.1.jar
```

## 📊 Endpoints Disponibles

### Rutas del API Gateway
- **Product Detail**: http://localhost:4040/product-detail/detail/**
- **Auth Server**: http://localhost:4040/auth-server/auth/**
- **Product Detail Fallback**: http://localhost:4040/product-detail-fallback/detail/**

### Perfiles Disponibles
- **dinamic-routes-cb**: Rutas con Circuit Breaker
- **MeliAuth2**: Rutas con autenticación OAuth2

### Monitoreo (si Actuator está habilitado)
- **Health Check**: http://localhost:4040/actuator/health
- **Info**: http://localhost:4040/actuator/info
- **Metrics**: http://localhost:4040/actuator/metrics

## 🔍 Funcionalidades Principales

1. **API Gateway**: Punto de entrada único para todos los microservicios
2. **Enrutamiento Dinámico**: Enrutamiento basado en paths y service discovery
3. **Circuit Breaker**: Tolerancia a fallos con Resilience4j
4. **Autenticación OAuth2**: Filtro de seguridad para rutas protegidas
5. **Load Balancing**: Balanceo de carga automático entre instancias
6. **Fallback**: Rutas de respaldo cuando los servicios fallan
7. **Service Discovery**: Integración con Eureka para descubrimiento de servicios

## 🏗️ Construcción del Proyecto

### Compilar el proyecto
```bash
mvn clean compile
```

### Ejecutar tests
```bash
mvn test
```

### Generar JAR ejecutable
```bash
mvn clean package
```

### Limpiar archivos generados
```bash
mvn clean
```

## 🔒 Configuraciones de Seguridad

El proyecto incluye un filtro de autenticación OAuth2 que puede habilitarse con el perfil `MeliAuth2`. En un entorno de producción, se recomienda:
- Configurar tokens JWT válidos
- Implementar validación de tokens
- Habilitar HTTPS
- Configurar CORS apropiadamente

## 📈 Monitoreo y Observabilidad

Para habilitar monitoreo, agregar Spring Boot Actuator:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Esto proporcionará:
- Health checks automáticos
- Métricas de la aplicación
- Información del entorno
- Endpoints de gestión

## 🤝 Cómo Configurar un Microservicio Cliente

Para que un microservicio use este API Gateway:

1. Registrarse en Eureka con el nombre correcto
2. Configurar el cliente en `application.yml`:
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    instance-id: "${spring.application.name}:${random.value}"
```

3. El microservicio será accesible a través del gateway en:
   - http://localhost:4040/{service-name}/**

## 📋 Notas Técnicas

- **Profiles**: El gateway soporta diferentes perfiles para diferentes configuraciones de routing
- **Circuit Breaker**: Configurado con Resilience4j para manejar fallos de servicios
- **Load Balancing**: Integrado con Spring Cloud LoadBalancer
- **Reactive**: Basado en Spring WebFlux para alta concurrencia

## 🔗 Referencias y Documentación

- [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Spring Cloud Netflix Eureka](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html)
- [Resilience4j Circuit Breaker](https://resilience4j.readme.io/docs/circuitbreaker)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)

## 🏷️ Versiones y Compatibilidad

| Componente | Versión |
|------------|---------|
| Spring Boot | 3.5.6 |
| Spring Cloud | 2025.0.0 |
| Java | 17+ |
| Maven | 3.6+ |

---

**Desarrollado para**: Desafío técnico MercadoLibre (MELI)  
**Tipo**: API Gateway con Circuit Breaker y OAuth2  
**Framework**: Spring Boot + Spring Cloud Gateway + Resilience4j