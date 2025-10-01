# Auth Server - Servidor de Autenticación y Autorización

## 📋 Descripción del Proyecto

Este proyecto es un **Servidor de Autenticación y Autorización** desarrollado como parte del desafío técnico de MercadoLibre (MELI). Utiliza **Spring Security** junto con **## 📆 Endpoints Disponibles

### Auth Server API
- **URL Base**: http://localhost:3030/auth-server
- **Login**: `POST /auth/login` - Autenticación de usuario y generación de token
- **Validación de Token**: `POST /auth/jwt` - Validación de token JWT

### Ejemplos de Uso

#### Login de Usuario
```http
POST http://localhost:3030/auth-server/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```

#### Validación de Token
```http
POST http://localhost:3030/auth-server/auth/jwt
accessToken: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Consola H2 Database
- **H2 Console**: http://localhost:3030/auth-server/h2-console
  - **JDBC URL**: `jdbc:h2:mem:users`
  - **Username**: `sa`
  - **Password**: `sa`

### Health Check
- **Health Check**: http://localhost:3030/auth-server/actuator/health
- **Info**: http://localhost:3030/auth-server/actuator/infookens)** para proporcionar servicios de autenticación segura y generación de tokens de acceso para microservicios.

## 🏗️ Arquitectura

El proyecto implementa el patrón **Authentication & Authorization** utilizando:
- **Spring Boot 3.5.6** como framework principal
- **Spring Security** para autenticación y autorización
- **JWT** para generación y validación de tokens
- **H2 Database** como base de datos en memoria para usuarios

El Auth Server implementa el patrón **Centralized Authentication** que es fundamental en arquitecturas de microservicios:

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Centralized Authentication Architecture           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│                 ┌─────────────────────────────────┐                 │
│                 │          Auth Server            │                 │
│                 │     (Spring Security + JWT)     │                 │
│                 │         Port: 3030              │                 │
│                 │                                 │                 │
│                 │  ┌───────────────────────────┐  │                 │
│                 │  │     User Repository       │  │                 │
│                 │  │     (H2 Database)         │  │                 │
│                 │  │  ┌─────────────────────┐  │  │                 │
│                 │  │  │ Users Table         │  │  │                 │
│                 │  │  │ - id, username      │  │  │                 │
│                 │  │  │ - password (hash)   │  │  │                 │
│                 │  │  └─────────────────────┘  │  │                 │
│                 │  └───────────────────────────┘  │                 │
│                 │                                 │                 │
│                 │  ┌───────────────────────────┐  │                 │
│                 │  │      JWT Helper           │  │                 │
│                 │  │  - Token Generation       │  │                 │
│                 │  │  - Token Validation       │  │                 │
│                 │  │  - Token Signing          │  │                 │
│                 │  └───────────────────────────┘  │                 │
│                 └─────────────┬───────────────────┘                 │
│                               │ HTTP REST API                       │
│       ┌───────────────────────┼───────────────────────┐             │
│       │ 1. POST /auth/login   │ 2. POST /auth/jwt     │             │
│       │ (User Credentials)    │ (Token Validation)    │             │
│       │                       │                       │             │
│ ┌─────▼─────┐          ┌──────▼──────┐         ┌──────▼──────┐      │
│ │Service A  │          │Service B    │         │Service C    │      │
│ │Port: 8081 │          │Port: 8082   │         │Port: 8083   │      │
│ │           │          │             │         │             │      │
│ └───────────┘          └─────────────┘         └─────────────┘      │
└─────────────────────────────────────────────────────────────────────┘

## 🛠️ Tecnologías y Dependencias

### Información del Proyecto
- **GroupId**: `com.meli`
- **ArtifactId**: `auth-server`
- **Versión**: `0.0.1-SNAPSHOT`
- **Java Version**: `17`
- **Puerto por defecto**: `3030`
- **Context Path**: `/auth-server`

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

#### 2. Spring Boot Starter Security
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
**Propósito**: Proporciona funcionalidades de seguridad, autenticación y autorización.

#### 3. JWT Dependencies (JJWT)
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
</dependency>
```
**Propósito**: Biblioteca para crear, parsear y validar JSON Web Tokens (JWT).

#### 4. H2 Database
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```
**Propósito**: Base de datos en memoria para desarrollo y testing. Almacena usuarios y credenciales.

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
auth-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── meli/
│   │   │           └── auth_server/
│   │   │               ├── AuthServerApplication.java
│   │   │               ├── controllers/
│   │   │               │   └── AuthController.java
│   │   │               ├── services/
│   │   │               │   ├── AuthService.java
│   │   │               │   └── AuthServiceImpl.java
│   │   │               ├── helpers/
│   │   │               │   └── JwtHelper.java
│   │   │               ├── dtos/
│   │   │               │   ├── UserDto.java
│   │   │               │   └── TokenDto.java
│   │   │               ├── entities/
│   │   │               │   └── UserEntity.java
│   │   │               └── repositories/
│   │   │                   └── UserRepository.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── banner.txt
│   │       ├── data.sql
│   │       └── schema.sql
│   └── test/
├── target/                     # Archivos compilados
│   ├── classes/
│   ├── generated-sources/
│   ├── maven-archiver/
│   ├── maven-status/
│   ├── config-server-0.0.1.jar
│   └── config-server-0.0.1.jar.original
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
  profiles:
    default: Dev
  application:
    name: auth-server            # Nombre de la aplicación
  banner:
    location: classpath:banner.txt
  main:
    banner-mode: console
  h2:
    console:
      enabled: true              # Habilita consola H2
      path: /h2-console
  config:
    import: "optional:configserver:http://localhost:7777"
  datasource:
    url: jdbc:h2:mem:users       # Base de datos H2 en memoria
    username: sa
    password: sa
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
server:
  port: 3030                     # Puerto del Auth Server
  servlet:
    context-path: /auth-server   # Context path

application:
  jwt:
    secret: mySecretKey123456789012345678901234567890  # JWT Secret
  expirationTime: 3600000        # Token expiration (1 hora)

eureka:
  instance:
    instance-id: "${spring.application.name}:${random.value}"
    prefer-ip-address: true
    ip-address: localhost
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Configuración de la Aplicación Principal

La clase `AuthServerApplication` utiliza las siguientes anotaciones:

- `@SpringBootApplication`: Habilita la configuración automática de Spring Boot
- `@EnableEurekaClient`: Habilita el cliente para registro en service discovery de Eureka

```java
@SpringBootApplication
@EnableEurekaClient
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
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
java -jar target/auth-server-0.0.1-SNAPSHOT.jar
```

## 📊 Endpoints Disponibles

### Config Server API
- **URL Base**: http://localhost:7777
- **Configuraciones por aplicación**: http://localhost:7777/{application}/{profile}
- **Configuraciones con label**: http://localhost:7777/{application}/{profile}/{label}

### Ejemplos de Endpoints de Configuración
- **Configuración de servicio-a**: http://localhost:7777/servicio-a/default
- **Configuración de servicio-a en dev**: http://localhost:7777/servicio-a/dev
- **Configuración con label específico**: http://localhost:7777/servicio-a/prod/v1.0

### Health Check
- **Health Check**: http://localhost:7777/actuator/health
- **Info**: http://localhost:7777/actuator/info

## 🔍 Funcionalidades Principales

1. **Autenticación JWT**: Generación y validación de tokens JWT seguros
2. **Gestión de Usuarios**: Almacenamiento y autenticación de usuarios en base de datos H2
3. **Password Encoding**: Cifrado seguro de contraseñas usando BCrypt
4. **Token Expiration**: Control de tiempo de vida de tokens de acceso
5. **Service Discovery Integration**: Registro automático en Eureka para ser descubierto por otros servicios
6. **RESTful API**: Endpoints REST para integración con microservicios
7. **Database Console**: Acceso a consola H2 para gestión de datos de usuarios

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

Por defecto, el servidor Eureka no tiene autenticación habilitada. En un entorno de producción, se recomienda:
- Habilitar HTTPS
- Configurar autenticación básica o OAuth2
- Implementar restricciones de red

## 📈 Monitoreo y Observabilidad

El proyecto incluye **Spring Boot Actuator** que proporciona:
- Health checks automáticos
- Métricas de la aplicación
- Información del entorno
- Endpoints de gestión

## 🤝 Cómo Conectar un Cliente al Auth Server

Para que un microservicio utilice este Auth Server para autenticación, debe:

1. Incluir dependencias de seguridad y JWT:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
```

2. Configurar el cliente para comunicarse con Auth Server:
```yaml
auth:
  server:
    url: http://localhost:3030/auth-server
    login-endpoint: /auth/login
    validate-endpoint: /auth/jwt
```

3. Implementar intercepción de tokens en requests HTTP:
   - Obtener token desde: `POST http://localhost:3030/auth-server/auth/login`
   - Validar token con: `POST http://localhost:3030/auth-server/auth/jwt`
   - Incluir token en header: `Authorization: Bearer <token>`

## 📋 Notas Técnicas

- **Base de Datos**: H2 en memoria, datos se pierden al reiniciar la aplicación
- **JWT Secret**: Configurado en application.yml, cambiar en producción
- **Token Expiration**: Por defecto 1 hora (3600000 ms)
- **Password Encoding**: BCrypt para cifrado seguro de contraseñas
- **Compatibilidad**: Compatible con Spring Boot 3.x y Spring Cloud 2025.x
- **Registro en Eureka**: El Auth Server se registra automáticamente en Eureka para service discovery
- **Context Path**: Todos los endpoints están bajo `/auth-server`

## 🔗 Referencias y Documentación

- [Spring Cloud Config Server](https://docs.spring.io/spring-cloud-config/reference/server.html)
- [Centralized Configuration Guide](https://spring.io/guides/gs/centralized-configuration/)
- [Spring Cloud Config Reference](https://cloud.spring.io/spring-cloud-config/reference/html/)
- [Maven Documentation](https://maven.apache.org/guides/index.html)

## 🏷️ Versiones y Compatibilidad

| Componente | Versión |
|------------|---------|
| Spring Boot | 3.5.6 |
| Spring Cloud | 2025.0.0 |
| Java | 17+ |
| Maven | 3.6+ |

---

**Desarrollado para**: Desafío técnico MercadoLibre (MELI)  
**Tipo**: Servidor de Autenticación y Autorización  
**Framework**: Spring Boot + Spring Security + JWT