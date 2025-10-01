# 🚀 Guía de Ejecución en Visual Studio Code# 🚀 Guía de Ejecución en Visual Studio Code



## 📋 Guía Completa para Ejecutar API Gateway desde VS Code## 📋 Guía Completa para Ejecutar Registry Server desde VS Code



Esta guía te llevará paso a paso para configurar y ejecutar el **API Gateway** directamente desde Visual Studio Code en tu entorno local de desarrollo.Esta guía te llevará paso a paso para configurar y ejecutar el **Registry Server** directamente desde Visual Studio Code en tu entorno local de desarrollo.



------



## 🛠️ Prerrequisitos## 🛠️ Prerrequisitos



### Requisitos del Sistema### Requisitos del Sistema

- **Java 17+** instalado y configurado- **Java 17+** instalado y configurado

- **Maven 3.6+** (opcional, se incluye wrapper)- **Maven 3.6+** (opcional, se incluye wrapper)

- **Visual Studio Code** con las extensiones necesarias- **Visual Studio Code** con las extensiones necesarias

- **Git** para clonar el repositorio- **Git** para clonar el repositorio

- **Registry Server** ejecutándose en puerto 8761

### Verificar Instalación de Java

### Verificar Instalación de Java```powershell

```powershell# Verificar versión de Java

# Verificar versión de Javajava -version

java -version

# Verificar JAVA_HOME

# Verificar JAVA_HOMEecho $env:JAVA_HOME

echo $env:JAVA_HOME

# Si no está configurado, configurar JAVA_HOME

# Si no está configurado, configurar JAVA_HOME$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"```

```



## 📦 Extensiones Recomendadas para VS Code## 📦 Extensiones Recomendadas para VS Code



### Extensiones Esenciales### Extensiones Esenciales

Instala las siguientes extensiones desde el Marketplace de VS Code:Instala las siguientes extensiones desde el Marketplace de VS Code:



1. **Extension Pack for Java** (Microsoft)1. **Extension Pack for Java** (Microsoft)

   - Incluye: Language Support, Debugger, Test Runner, Maven, Project Manager   - Incluye: Language Support, Debugger, Test Runner, Maven, Project Manager

   - ID: `vscjava.vscode-java-pack`   - ID: `vscjava.vscode-java-pack`



2. **Spring Boot Extension Pack** (VMware)2. **Spring Boot Extension Pack** (VMware)

   - Incluye: Spring Boot Tools, Spring Initializr, Spring Boot Dashboard   - Incluye: Spring Boot Tools, Spring Initializr, Spring Boot Dashboard

   - ID: `vmware.vscode-boot-dev-pack`   - ID: `vmware.vscode-boot-dev-pack`



3. **Maven for Java** (Microsoft)3. **Maven for Java** (Microsoft)

   - ID: `vscjava.vscode-maven`   - ID: `vscjava.vscode-maven`



4. **YAML** (Red Hat)4. **YAML** (Red Hat)

   - Para editar archivos de configuración YAML   - Para editar archivos de configuración YAML

   - ID: `redhat.vscode-yaml`   - ID: `redhat.vscode-yaml`



### Instalación Rápida de Extensiones

```powershell### Instalación Rápida de Extensiones

# Ejecutar en terminal de VS Code o PowerShell```powershell

code --install-extension vscjava.vscode-java-pack# Ejecutar en terminal de VS Code o PowerShell

code --install-extension vmware.vscode-boot-dev-packcode --install-extension vscjava.vscode-java-pack

code --install-extension redhat.vscode-yamlcode --install-extension vmware.vscode-boot-dev-pack

code --install-extension humao.rest-clientcode --install-extension redhat.vscode-yaml

```code --install-extension humao.rest-client

```

## 🎯 Métodos de Ejecución** Ejecución como desarrollador **

0- Abrir el proyecto con visual studio code

### **Ejecución como desarrollador**1- Realizar un maven clean:

0. Abrir el proyecto con Visual Studio Code2- Ejecutar un maven install:

1. Realizar un maven clean3- Ubicarse en el archivo RegistryServerApplication.java y ejecutar proyecto.

2. Ejecutar un maven compile

3. Ubicarse en el archivo GatewayApplication.java y ejecutar proyecto2. **Ejecutar con Maven Wrapper:**

   ```powershell

### Método 1: Ejecutar desde Spring Boot Dashboard   # Limpiar y ejecutar

1. Abrir el **Spring Boot Dashboard** (panel izquierdo)   .\mvnw.cmd clean spring-boot:run

2. Localizar "gateway" en la lista de aplicaciones   

3. Hacer clic en el ícono ▶️ para ejecutar   # Solo ejecutar (si ya está compilado)

4. Seleccionar perfil si es necesario   .\mvnw.cmd spring-boot:run

   ```

### Método 2: Ejecutar desde Java File

1. Abrir `src/main/java/com/meli/gateway/GatewayApplication.java`## 🌐 Verificación de Ejecución

2. Hacer clic derecho en el archivo

3. Seleccionar "Run Java" o "Debug Java"### URLs de Verificación



### Método 3: Terminal IntegradoUna vez ejecutado, verifica estos endpoints:

```powershell

# 1. Limpiar proyecto```http

.\mvnw.cmd clean# Dashboard principal de Eureka

GET http://localhost:8761/

# 2. Compilar proyecto  

.\mvnw.cmd compile# Health check

GET http://localhost:8761/actuator/health

# 3. Ejecutar proyecto

.\mvnw.cmd spring-boot:run# Información de la aplicación

```GET http://localhost:8761/actuator/info



**Con diferentes perfiles:**# Servicios registrados (JSON)

```powershellGET http://localhost:8761/eureka/apps

# Con perfil OAuth2

.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=MeliAuth2# Métricas

GET http://localhost:8761/actuator/metrics

# Con perfil Circuit Breaker```

.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dinamic-routes-cb

```## ✅ Checklist de Verificación



## 🌐 Verificación de EjecuciónAntes de comenzar el desarrollo, verifica:



### URLs de Verificación- [ ] Java 17+ instalado

- [ ] VS Code con extensiones Java instaladas

Una vez ejecutado, verifica estos endpoints:- [ ] Proyecto abierto correctamente en VS Code

- [ ] Maven wrapper funciona (`.\\mvnw.cmd -version`)

```http- [ ] Puerto 8761 disponible

# Rutas principales del Gateway- [ ] Archivos de configuración `.vscode` creados

GET http://localhost:4040/product-detail/detail/123- [ ] Spring Boot Dashboard visible

GET http://localhost:4040/auth-server/auth/login- [ ] Tests ejecutándose correctamente

GET http://localhost:4040/product-detail-fallback/detail/123

---

# Health check (si Actuator está habilitado)

GET http://localhost:4040/actuator/health¡Listo! Ya tienes todo configurado para desarrollar el Registry Server desde Visual Studio Code de manera eficiente. 🎉


# Información de la aplicación
GET http://localhost:4040/actuator/info
```

## 🔍 Troubleshooting

### Problemas Comunes y Soluciones

#### 1. Puerto 4040 ya está en uso
```powershell
# Verificar qué proceso usa el puerto
netstat -ano | findstr :4040

# Cambiar puerto en application.yml
server:
  port: 4041
```

#### 2. Registry Server no disponible
- Verificar que Eureka Server esté ejecutándose en puerto 8761
- Revisar configuración en `application.yml`

#### 3. Problemas de compilación
```powershell
# Forzar descarga de dependencias
.\mvnw.cmd clean install -U

# Limpiar caché de Maven
.\mvnw.cmd clean
```

## ✅ Checklist de Verificación

Antes de comenzar el desarrollo, verifica:

- [ ] Java 17+ instalado
- [ ] VS Code con extensiones Java instaladas
- [ ] Proyecto abierto correctamente en VS Code
- [ ] Maven wrapper funciona (`.\\mvnw.cmd -version`)
- [ ] Registry Server ejecutándose en puerto 8761
- [ ] Puerto 4040 disponible
- [ ] Spring Boot Dashboard visible
- [ ] Tests ejecutándose correctamente
- [ ] Endpoints del Gateway respondiendo

## 📱 Perfiles de Ejecución

### Perfil por defecto (meli-auth)
- Configuración básica del gateway
- Sin autenticación OAuth2
- Circuit breaker básico

### Perfil MeliAuth2
- Autenticación OAuth2 habilitada
- Filtro de autorización activo
- Validación de tokens

### Perfil dinamic-routes-cb
- Circuit breaker avanzado
- Rutas dinámicas
- Fallback automático

---

¡Listo! Ya tienes todo configurado para desarrollar el API Gateway desde Visual Studio Code de manera eficiente. 🎉