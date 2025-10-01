# 🚀 Guía de Ejecución en Visual Studio Code

## 📋 Guía Completa para Ejecutar Config Server desde VS Code

Esta guía te llevará paso a paso para configurar y ejecutar el **Config Server** directamente desde Visual Studio Code en tu entorno local de desarrollo.

---

## 🛠️ Prerrequisitos

### Requisitos del Sistema
- **Java 17+** instalado y configurado
- **Maven 3.6+** (opcional, se incluye wrapper)
- **Visual Studio Code** con las extensiones necesarias
- **Git** para clonar el repositorio

### Verificar Instalación de Java
```powershell
# Verificar versión de Java
java -version

# Verificar JAVA_HOME
echo $env:JAVA_HOME

# Si no está configurado, configurar JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```


## 📦 Extensiones Recomendadas para VS Code

### Extensiones Esenciales
Instala las siguientes extensiones desde el Marketplace de VS Code:

1. **Extension Pack for Java** (Microsoft)
   - Incluye: Language Support, Debugger, Test Runner, Maven, Project Manager
   - ID: `vscjava.vscode-java-pack`

2. **Spring Boot Extension Pack** (VMware)
   - Incluye: Spring Boot Tools, Spring Initializr, Spring Boot Dashboard
   - ID: `vmware.vscode-boot-dev-pack`

3. **Maven for Java** (Microsoft)
   - ID: `vscjava.vscode-maven`

4. **YAML** (Red Hat)
   - Para editar archivos de configuración YAML
   - ID: `redhat.vscode-yaml`


### Instalación Rápida de Extensiones
```powershell
# Ejecutar en terminal de VS Code o PowerShell
code --install-extension vscjava.vscode-java-pack
code --install-extension vmware.vscode-boot-dev-pack
code --install-extension redhat.vscode-yaml
code --install-extension humao.rest-client
```
** Ejecución como desarrollador **
0- Abrir el proyecto con visual studio code
1- Realizar un maven clean:
2- Ejecutar un maven install:
3- Ubicarse en el archivo ConfigServerApplication.java y ejecutar proyecto.

2. **Ejecutar con Maven Wrapper:**
   ```powershell
   # Limpiar y ejecutar
   .\mvnw.cmd clean spring-boot:run
   
   # Solo ejecutar (si ya está compilado)
   .\mvnw.cmd spring-boot:run
   ```

## 🌐 Verificación de Ejecución

### URLs de Verificación

Una vez ejecutado, verifica estos endpoints:

```http
# Health check
GET http://localhost:7777/actuator/health

# Información de la aplicación
GET http://localhost:7777/actuator/info

# Ejemplo de configuración para un servicio
GET http://localhost:7777/servicio-ejemplo/default

# Configuración con perfil específico
GET http://localhost:7777/servicio-ejemplo/dev

# Configuración con label (versión/rama)
GET http://localhost:7777/servicio-ejemplo/prod/main
```

## ✅ Checklist de Verificación

Antes de comenzar el desarrollo, verifica:

- [ ] Java 17+ instalado
- [ ] VS Code con extensiones Java instaladas
- [ ] Proyecto abierto correctamente en VS Code
- [ ] Maven wrapper funciona (`.\\mvnw.cmd -version`)
- [ ] Puerto 7777 disponible
- [ ] Repositorio Git configurado y accesible
- [ ] Archivos de configuración `.vscode` creados
- [ ] Spring Boot Dashboard visible
- [ ] Tests ejecutándose correctamente
- [ ] Config Server respondiendo en endpoints de configuración

---

¡Listo! Ya tienes todo configurado para desarrollar el Config Server desde Visual Studio Code de manera eficiente. 🎉
