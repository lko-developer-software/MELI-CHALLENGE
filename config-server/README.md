# Config Server - Servidor de Configuración Centralizada

## 📋 Descripción del Proyecto

Este proyecto es un **Servidor de Configuración Centralizada** desarrollado como parte del desafío técnico de MercadoLibre (MELI). Utiliza **Spring Cloud Config Server** para proporcionar un servicio centralizado de gestión de configuraciones donde los microservicios pueden obtener sus parámetros de configuración de forma dinámica desde un repositorio Git.

## 🏗️ Arquitectura

El proyecto implementa el patrón **Externalized Configuration** utilizando:
- **Spring Boot 3.5.6** como framework principal
- **Spring Cloud 2025.0.0** para funcionalidades de microservicios
- **Spring Cloud Config Server** como servidor de configuración centralizada
- **Git Repository** como almacén de configuraciones

El Config Server implementa el patrón **Externalized Configuration** que es fundamental en arquitecturas de microservicios:

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Externalized Configuration Architecture           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    Git Repository                           │   │
│  │         https://github.com/lko-developer-software/MELI     │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │   │
│  │  │service-a.yml│ │service-b.yml│ │service-c.yml│           │   │
│  │  │             │ │             │ │             │           │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘           │   │
│  └─────────────────────┬───────────────────────────────────────┘   │
│                        │ Git Clone/Pull                            │
│                        ▼                                           │
│              ┌─────────────────────────────────┐                   │
│              │        Config Server            │                   │
│              │   (Spring Cloud Config)         │                   │
│              │        Port: 7777               │                   │
│              │                                 │                   │
│              │ ┌─────────────────────────────┐ │                   │
│              │ │  Configuration Repository   │ │                   │
│              │ │  - service-a configurations │ │                   │
│              │ │  - service-b configurations │ │                   │
│              │ │  - service-c configurations │ │                   │
│              │ └─────────────────────────────┘ │                   │
│              └─────────────┬───────────────────┘                   │
│                            │ HTTP REST API                         │
│         ┌──────────────────┼──────────────────┐                    │
│         │ 1. GET Config    │ 2. GET Config    │ 3. GET Config      │
│         │                  │                  │                    │
│  ┌──────▼──────┐    ┌──────▼──────┐    ┌──────▼──────┐            │
│  │Service A    │    │Service B    │    │Service C    │            │
│  │Port: 8081   │    │Port: 8082   │    │Port: 8083   │            │
│  │             │    │             │    │             │            │
│  └─────────────┘    └─────────────┘    └─────────────┘            │
└─────────────────────────────────────────────────────────────────────┘

## 🛠️ Tecnologías y Dependencias

### Información del Proyecto
- **GroupId**: `com.meli`
- **ArtifactId**: `config-server`
- **Versión**: `0.0.1`
- **Java Version**: `17`
- **Puerto por defecto**: `7777`

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

#### 2. Spring Cloud Config Server
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```
**Propósito**: Implementa el servidor de configuración centralizada que sirve configuraciones desde repositorios Git.

#### 3. Spring Cloud Starter Netflix Eureka Client
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
**Propósito**: Permite que el Config Server se registre como cliente en un servidor Eureka para ser descubierto por otros servicios.

#### 4. Spring Boot Starter Test (Test Scope)
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
config-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── meli/
│   │   │           └── config_server/
│   │   │               └── ConfigServerApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── banner.txt
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
    active: default
  application:
    name: config-server          # Nombre de la aplicación
  banner:
    location: classpath:banner.txt
  main:
    banner-mode: console
  cloud:
    config:
      server:
        git:
          uri: https://github.com/lko-developer-software/MELI.git
          default-label: main      # Rama por defecto del repositorio Git
server:
  port: 7777                     # Puerto del Config Server

eureka:
  instance:
    instance-id: "${spring.application.name}:${random.value}"
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true   # Se registra en Eureka
    fetch-registry: true         # Obtiene el registro de Eureka
```

### Configuración de la Aplicación Principal

La clase `ConfigServerApplication` utiliza las siguientes anotaciones:

- `@SpringBootApplication`: Habilita la configuración automática de Spring Boot
- `@EnableConfigServer`: Habilita el servidor de configuración de Spring Cloud
- `@EnableDiscoveryClient`: Habilita el cliente para registro en service discovery

```java
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
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
java -jar target/config-server-0.0.1.jar
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

1. **Externalized Configuration**: Centralización de configuraciones en un repositorio Git externo
2. **Dynamic Configuration**: Actualización de configuraciones sin reiniciar los microservicios
3. **Environment-specific Configs**: Soporte para perfiles (dev, test, prod) y configuraciones específicas
4. **Version Control**: Historial y versionado de configuraciones a través de Git
5. **Service Discovery Integration**: Registro automático en Eureka para ser descubierto por otros servicios
6. **Security**: Soporte para configuraciones sensibles y cifrado de propiedades

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

## 🤝 Cómo Conectar un Cliente al Config Server

Para que un microservicio obtenga configuraciones desde este Config Server, debe:

1. Incluir la dependencia de cliente Config:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

2. Configurar el cliente en `bootstrap.yml` o `application.yml`:
```yaml
spring:
  application:
    name: mi-servicio
  cloud:
    config:
      uri: http://localhost:7777
      fail-fast: true
  config:
    import: "configserver:"
```

3. El servicio automáticamente obtendrá configuraciones desde:
   - `http://localhost:7777/mi-servicio/default`
   - O según el perfil activo: `http://localhost:7777/mi-servicio/dev`

## 📋 Notas Técnicas

- **Repositorio Git**: Las configuraciones se obtienen desde `https://github.com/lko-developer-software/MELI.git`
- **Compatibilidad**: Compatible con Spring Boot 3.x y Spring Cloud 2025.x
- **Rama por Defecto**: Utiliza la rama `main` del repositorio Git
- **Registro en Eureka**: El Config Server se registra automáticamente en Eureka para service discovery
- **Refresh Scope**: Soporte para actualización dinámica de configuraciones usando `/actuator/refresh`

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
**Tipo**: Servidor de Configuración Centralizada  
**Framework**: Spring Boot + Spring Cloud Config Server