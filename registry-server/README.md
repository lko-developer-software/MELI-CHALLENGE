# Registry Server - Servidor de Descubrimiento de Servicios

## 📋 Descripción del Proyecto

Este proyecto es un **Servidor de Registro y Descubrimiento de Servicios** desarrollado como parte del desafío técnico de MercadoLibre (MELI). Utiliza **Netflix Eureka Server** para proporcionar un servicio centralizado donde los microservicios pueden registrarse y descubrir otros servicios en la arquitectura distribuida.

## 🏗️ Arquitectura

El proyecto implementa el patrón **Service Discovery** utilizando:
- **Spring Boot 3.5.0** como framework principal
- **Spring Cloud 2025.0.0** para funcionalidades de microservicios
- **Netflix Eureka Server** como servidor de registro de servicios

El Registry Server implementa el patrón **Service Discovery** que es fundamental en arquitecturas de microservicios:

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Service Discovery Architecture                 │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐           │
│  │Service A    │────▶│Service B    │────▶│Service C    │           │
│  │Port: 8081   │     │Port: 8082   │     │Port: 8083   │           │
│  └──────┬──────┘     └──────┬──────┘     └──────┬──────┘           │
│         │                   │                   │                   │
│         │ 1. Register       │ 2. Register       │ 3. Register       │
│         │                   │                   │                   │
│         └───────────────────┼───────────────────┘                   │
│                             │                                       │
│              ┌──────────────▼──────────────┐                       │
│              │    Registry Server          │                       │
│              │    (Netflix Eureka)         │                       │
│              │    Port: 8761               │                       │
│              │                             │                       │
│              │ ┌─────────────────────────┐ │                       │
│              │ │   Service Registry      │ │                       │
│              │ │   - Service A: 8081     │ │                       │
│              │ │   - Service B: 8082     │ │                       │
│              │ │   - Service C: 8083     │ │                       │
│              │ └─────────────────────────┘ │                       │
│              └─────────────────────────────┘                       │
│                             │                                       │
│         ┌───────────────────┼───────────────────┐                   │
│         │ 4. Discover       │ 5. Discover       │ 6. Discover       │
│         │                   │                   │                   │
│  ┌──────▼──────┐     ┌──────▼──────┐     ┌──────▼──────┐           │
│  │API Gateway  │     │Load Balancer│     │Client App   │           │
│  │Port: 8080   │     │Port: 8090   │     │Port: 8091   │           │
│  └─────────────┘     └─────────────┘     └─────────────┘           │
└─────────────────────────────────────────────────────────────────────┘

## 🛠️ Tecnologías y Dependencias

### Información del Proyecto
- **GroupId**: `com.meli`
- **ArtifactId**: `registry-server`
- **Versión**: `0.0.1`
- **Java Version**: `17`
- **Puerto por defecto**: `8761`

### Dependencias Principales

#### 1. Spring Boot Starter Parent
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.0</version>
</parent>
```
**Propósito**: Proporciona configuraciones base y gestión de dependencias para Spring Boot.

#### 2. Spring Boot Starter Actuator
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
**Propósito**: Habilita endpoints de monitoreo y gestión de la aplicación (health checks, métricas, info, etc.).

#### 3. Spring Cloud Starter Netflix Eureka Server
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
**Propósito**: Implementa el servidor de registro Netflix Eureka para service discovery.

#### 4. Spring Boot DevTools (Runtime)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```
**Propósito**: Herramientas de desarrollo que proporcionan reinicio automático y recarga en caliente durante el desarrollo.

#### 5. Spring Boot Starter Test (Test Scope)
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
registry-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── meli/
│   │   │           └── registry_server/
│   │   │               └── RegistryServerApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── target/                     # Archivos compilados
│   ├── classes/
│   ├── generated-sources/
│   ├── maven-archiver/
│   ├── maven-status/
│   ├── registry-server-0.0.1.jar
│   └── registry-server-0.0.1.jar.original
├── mvnw                        # Maven Wrapper (Unix)
├── mvnw.cmd                    # Maven Wrapper (Windows)
├── pom.xml                     # Configuración de Maven
├── HELP.md                     # Documentación de referencia
└── README.md                   # Este archivo
```

## 🔧 Configuración

### application.yml
```yaml
spring:
  application:
    name: registry-server        # Nombre de la aplicación
server:
  port: 8761                     # Puerto del servidor Eureka
eureka:
  client:
    register-with-eureka: false  # No se registra a sí mismo
    fetch-registry: false        # No obtiene el registro de otros servicios
```

### Configuración de la Aplicación Principal

La clase `RegistryServerApplication` utiliza las siguientes anotaciones:

- `@SpringBootApplication`: Habilita la configuración automática de Spring Boot
- `@EnableEurekaServer`: Habilita el servidor Netflix Eureka

```java
@SpringBootApplication
@EnableEurekaServer
public class RegistryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistryServerApplication.class, args);
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
java -jar target/registry-server-0.0.1.jar
```

## 📊 Endpoints Disponibles

### Eureka Server Dashboard
- **URL**: http://localhost:8761
- **Descripción**: Interfaz web para visualizar los servicios registrados

### Actuator Endpoints (Monitoreo)
- **Health Check**: http://localhost:8761/actuator/health
- **Info**: http://localhost:8761/actuator/info
- **Metrics**: http://localhost:8761/actuator/metrics

### API Eureka
- **Registro de servicios**: http://localhost:8761/eureka/apps
- **Información de instancias**: http://localhost:8761/eureka/apps/{app-name}

## 🔍 Funcionalidades Principales

1. **Service Registry**: Los microservicios se pueden registrar automáticamente
2. **Service Discovery**: Los servicios pueden descubrir y comunicarse con otros servicios registrados
3. **Health Monitoring**: Monitoreo de la salud de los servicios registrados
4. **Load Balancing**: Soporte para balanceo de carga entre múltiples instancias
5. **Failover**: Manejo automático de fallos de servicios

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

## 🤝 Cómo Registrar un Cliente

Para que un microservicio se registre en este servidor Eureka, debe:

1. Incluir la dependencia de cliente Eureka:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2. Configurar el cliente en `application.yml`:
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

3. Anotar la clase principal con `@EnableEurekaClient` (opcional en versiones recientes)

## 📋 Notas Técnicas

- **Nota del Package**: El nombre original del paquete `com.dlko.registry-server` fue cambiado a `com.meli.registry_server` debido a restricciones de nomenclatura de Java
- **Compatibilidad**: Compatible con Spring Boot 3.x y Spring Cloud 2025.x
- **Perfil de Desarrollo**: Incluye DevTools para desarrollo con hot-reload

## 🔗 Referencias y Documentación

- [Spring Cloud Netflix Eureka](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html#spring-cloud-eureka-server)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.5.6/reference/actuator/index.html)
- [Service Registration and Discovery Guide](https://spring.io/guides/gs/service-registration-and-discovery/)
- [Maven Documentation](https://maven.apache.org/guides/index.html)

## 🏷️ Versiones y Compatibilidad

| Componente | Versión |
|------------|---------|
| Spring Boot | 3.5.0 |
| Spring Cloud | 2025.0.0 |
| Java | 17+ |
| Maven | 3.6+ |

---

**Desarrollado para**: Desafío técnico MercadoLibre (MELI)  
**Tipo**: Servidor de Registro y Descubrimiento de Servicios  
**Framework**: Spring Boot + Spring Cloud Netflix Eureka