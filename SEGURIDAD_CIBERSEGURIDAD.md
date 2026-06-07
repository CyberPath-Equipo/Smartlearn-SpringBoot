# 🔐 Guía Completa de Ciberseguridad - SmartLearn Spring Boot

## Tabla de Contenidos
1. [Introducción](#introducción)
2. [JWT - JSON Web Tokens](#jwt---json-web-tokens)
3. [Autenticación y Autorización](#autenticación-y-autorización)
4. [Autenticación de Dos Factores (2FA)](#autenticación-de-dos-factores-2fa)
5. [Protección de Endpoints](#protección-de-endpoints)
6. [Bloqueo de Navegadores](#bloqueo-de-navegadores)
7. [Encriptación y Cifrado](#encriptación-y-cifrado)
8. [Gestión de Contraseñas](#gestión-de-contraseñas)
9. [Validación y Sanitización](#validación-y-sanitización)
10. [Configuración de Seguridad](#configuración-de-seguridad)

---

## Introducción

SmartLearn implementa múltiples capas de seguridad para proteger los datos y la integridad del sistema:

- **Autenticación basada en JWT** para acceso sin estado
- **Autenticación de dos factores (2FA)** para mayor seguridad
- **Encriptación de datos sensibles**
- **Protección contra acceso desde navegadores**
- **Control de acceso basado en roles (RBAC)**
- **Validación de contraseñas con BCrypt**
- **Gestión de dispositivos confiables**

---

## JWT - JSON Web Tokens

### ¿Qué es JWT?

JWT es un estándar de seguridad que permite autenticar usuarios de forma stateless. El servidor genera un token criptográficamente firmado que el cliente envía en cada solicitud para probar su identidad.

### Implementación en SmartLearn

**Archivo principal:** `com.cyberpath.smartlearn.configuracion.seguridad.jwt.JwtService`

#### Características implementadas:

1. **Generación de Tokens**
   - Algoritmo: **HS256 (HMAC with SHA-256)**
   - Clave secreta: Mínimo 256 bits (32 bytes)
   - Tokens con expiración configurable

2. **Tipos de Tokens**
   ```
   - Access Token: Corta duración (24 horas por defecto)
   - Refresh Token: Larga duración (15 días por defecto)
   ```

3. **Información almacenada en JWT**
   - `subject`: Correo del usuario
   - `tokenType`: Indica si es ACCESS o REFRESH
   - `iat`: Timestamp de emisión
   - `exp`: Timestamp de expiración
   - `jti`: ID único del token

### Flujo de Autenticación con JWT

```
1. Usuario proporciona credenciales (correo + contraseña)
2. Servidor valida credenciales
3. Servidor genera Access Token y Refresh Token
4. Cliente almacena tokens
5. Cliente envía Access Token en header: "Authorization: ******"
6. Servidor valida token en cada solicitud
7. Cuando Access Token expira, cliente usa Refresh Token para obtener uno nuevo
```

### Código de Generación

```java
// Generar Access Token (24 horas)
String token = jwtService.generarToken(correoUsuario);

// Generar Refresh Token (15 días)
String refreshToken = jwtService.generarRefreshToken(correoUsuario);

// Validar token
boolean esValido = jwtService.isAccessTokenValid(token);

// Verificar si está expirado
boolean expirado = jwtService.isTokenExpired(token);
```

### Configuración de Seguridad

**Archivo:** `application.properties`

```properties
# Clave secreta (DEBE tener 256 bits mínimo)
jwt.secret=miClaveSuperSecretaParaJWT2026SmartLearnQueDebeSerMuyLargaYCompleja123456789

# Expiración del Access Token (en milisegundos)
jwt.expiration=86400000  # 24 horas

# Expiración del Refresh Token
jwt.refresh-expiration=1296000000  # 15 días
```

### Validación y Manejo de Excepciones

El servicio maneja múltiples excepciones de JWT:
- `ExpiredJwtException`: Token vencido
- `UnsupportedJwtException`: Algoritmo no soportado
- `MalformedJwtException`: Formato incorrecto
- `SecurityException`: Error de seguridad
- `JwtException`: Error genérico

---

## Autenticación y Autorización

### SecurityConfig - Configuración Centralizada de Seguridad

**Archivo:** `com.cyberpath.smartlearn.configuracion.seguridad.jwt.SecurityConfig`

#### Configuraciones principales:

1. **Password Encoder - BCrypt**
   ```java
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   ```
   - Las contraseñas se encriptan con BCrypt
   - Cada contraseña tiene un salt único
   - Imposible obtener la contraseña original

2. **Session Management**
   ```
   SessionCreationPolicy.IF_REQUIRED
   ```
   - Solo se crean sesiones cuando es necesario
   - JWT es stateless (no requiere sesiones)

3. **CORS (Cross-Origin Resource Sharing)**
   - Permite solicitudes desde cualquier origen
   - Métodos habilitados: GET, POST, PUT, DELETE, PATCH, OPTIONS
   - Expone header `X-New-Access-Token` para renovación de tokens

### Control de Acceso

**Endpoints públicos (sin autenticación):**
```
/smartlearn/api/usuario/login
/smartlearn/api/usuario/login/docente
/smartlearn/api/usuario/token/refresh
/smartlearn/api/usuario/registro
/smartlearn/api/usuario/registro/verificar
/smartlearn/api/usuario/registro/reenviar
/smartlearn/api/usuario/2fa/verify
/smartlearn/api/usuario/2fa/resend
/smartlearn/api/test
/smartlearn/api/lsm/**
```

**Endpoints protegidos (requieren autenticación):**
```
GET /smartlearn/api/usuario → ADMIN solo
Todos los demás → Requieren rol autenticado
```

---

## Autenticación de Dos Factores (2FA)

### ¿Qué es 2FA?

La autenticación de dos factores requiere dos métodos de verificación:
1. **Factor 1**: Credenciales (correo + contraseña)
2. **Factor 2**: Código de verificación adicional

### Métodos Soportados

1. **EMAIL** - Código verificación por correo (Por defecto)
2. **TOTP** - Time-based One-Time Password (Google Authenticator, Authy)
3. **SMS** - Actualmente redirige a EMAIL (compatible legado)

### Archivo Principal

**Ubicación:** `com.cyberpath.smartlearn.servicio.implementacion.usuario.TwoFactorServicioImpl`

### Flujo de Configuración 2FA

#### 1. Iniciar Setup de 2FA
```
POST /smartlearn/api/usuario/2fa/setup
Body: {
  "password": "contraseña_actual",
  "method": "EMAIL" | "TOTP" | "SMS"
}

Respuesta:
{
  "transactionId": "uuid-único",
  "secret": "SECRET_KEY" (solo para TOTP),
  "provisioningUri": "otpauth://..." (QR para TOTP),
  "recoveryCodes": ["CODE1", "CODE2", ...]
}
```

#### 2. Confirmar Setup 2FA
```
POST /smartlearn/api/usuario/2fa/confirm-setup
Body: {
  "transactionId": "uuid",
  "code": "código_verificación",
  "tempSecret": "SECRET_KEY" (solo para TOTP)
}
```

#### 3. Verificar 2FA en Login
```
POST /smartlearn/api/usuario/2fa/verify
Body: {
  "transactionId": "uuid",
  "code": "código_2fa",
  "rememberDevice": true,
  "deviceInfo": "Mozilla/5.0..."
}
```

### Método EMAIL - Paso a Paso

1. Usuario solicita setup
2. Sistema genera código verificación de 6 dígitos
3. Código se encripta con BCrypt y se guarda
4. Correo se envía con el código
5. Usuario ingresa código en la app
6. Sistema compara con hash guardado
7. Si coincide, 2FA se activa

```java
// Generación de código
private String generateVerificationCode() {
    SecureRandom random = new SecureRandom();
    int code = 100000 + random.nextInt(900000);
    return String.valueOf(code);
}

// Almacenamiento seguro
verificationCodeHash = passwordEncoder.encode(verificationCode);

// Validación
if (passwordEncoder.matches(userInput, verificationCodeHash)) {
    // Código correcto
}
```

### Método TOTP - Google Authenticator

1. Sistema genera secret TOTP (base32)
2. Se genera URI de provisión (QR code)
3. Usuario escanea QR con Google Authenticator/Authy
4. Usuario confirma con código generado por su dispositivo
5. Sistema verifica con GoogleAuthenticator library

```java
// Generación de secret TOTP
GoogleAuthenticator gAuth = new GoogleAuthenticator();
GoogleAuthenticatorKey key = gAuth.createCredentials();
String secret = key.getKey();

// URI para QR
String provisioningUri = String.format(
    "otpauth://totp/%s:%s?secret=%s&issuer=%s",
    issuer, email, secret, issuer
);

// Validación de código TOTP
if (gAuth.authorize(secret, Integer.parseInt(code))) {
    // Código correcto
}
```

### Códigos de Recuperación

- Se generan 10 códigos de 12 caracteres cada uno
- Se encriptan con BCrypt antes de guardar
- Pueden usarse si el dispositivo 2FA se pierde
- Cada código solo puede usarse una vez

```java
// Generación de códigos
private List<String> generateRecoveryCodes() {
    List<String> codes = new ArrayList<>();
    SecureRandom random = new SecureRandom();
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    for (int i = 0; i < 10; i++) {
        StringBuilder code = new StringBuilder();
        for (int j = 0; j < 12; j++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        codes.add(code.toString());
    }
    return codes;
}
```

### Dispositivos Confiables

Cuando un usuario activa "Recordar dispositivo" durante 2FA:

1. Se genera token único del dispositivo
2. Se almacena con información del dispositivo y fecha de expiración (30 días)
3. En futuros logins, se puede usar este token para saltar 2FA
4. Usuario puede revocar dispositivos en cualquier momento

```java
TrustedDevice device = TrustedDevice.builder()
    .usuario(usuario)
    .deviceToken(generatedToken)
    .deviceInfo(userAgent)
    .expiresAt(LocalDateTime.now().plusDays(30))
    .revoked(false)
    .build();
```

### Transacciones 2FA

- Cada operación de 2FA se registra con un transactionId único
- Las transacciones expiran en 5 minutos
- No pueden reutilizarse (only-once)
- Seguro contra replay attacks

```java
// Validaciones de transacción
if (LocalDateTime.now().isAfter(transaction.getExpiresAt())) {
    throw new RuntimeException("Transacción expirada");
}

if (transaction.getUsed()) {
    throw new RuntimeException("Transacción ya usada");
}
```

### Desactivar 2FA

```
POST /smartlearn/api/usuario/2fa/disable
Body: {
  "password": "contraseña",
  "code": "código_2fa_o_recovery"
}
```

Al desactivar 2FA:
- Se revoca automáticamente todos los dispositivos confiables
- Se eliminan todos los códigos de recuperación
- Se limpia el secret TOTP

---

## Protección de Endpoints

### JwtAuthFilter - Filtro de Validación JWT

**Ubicación:** `com.cyberpath.smartlearn.configuracion.seguridad.jwt.JwtAuthFilter`

El filtro se ejecuta en cada solicitud HTTP:

#### 1. Verificación del Authorization Header
```
Authorization: ******
```

#### 2. Validación del Token
- Valida firma criptográfica
- Verifica expiración
- Comprueba tipo (ACCESS vs REFRESH)

#### 3. Renovación Automática de Tokens
Si el Access Token está expirado pero el Refresh Token es válido:
- Genera nuevo Access Token
- Lo envía en header `X-New-Access-Token`
- El cliente lo almacena automáticamente

```java
// Lógica de renovación
if (jwtService.isTokenExpired(token)) {
    String refreshToken = request.getHeader("X-Refresh-Token");
    if (jwtService.isRefreshTokenValid(refreshToken)) {
        String newAccessToken = jwtService.generarToken(correo);
        response.setHeader("X-New-Access-Token", newAccessToken);
        // Continuar con solicitud
    } else {
        // Token expirado sin refresh válido
        sendJsonError(response, 401, "Token expirado");
    }
}
```

#### 4. Autenticación del Usuario
```java
// Se extrae correo del token
String correo = jwtService.obtenerSubject(token);

// Se busca usuario en BD
Usuario usuario = usuarioServicio.findByCorreo(correo);

// Se crea Authentication y se asigna a SecurityContext
UsernamePasswordAuthenticationToken auth = 
    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    
SecurityContextHolder.getContext().setAuthentication(auth);
```

#### 5. Rutas Excluidas del Filtro
```
/smartlearn/api/usuario/login
/smartlearn/api/usuario/login/docente
/smartlearn/api/usuario/token/refresh
/smartlearn/api/usuario/registro
/smartlearn/api/usuario/2fa/verify
/smartlearn/api/usuario/2fa/resend
```

#### 6. Manejo de Errores
Todas las respuestas de error son JSON:
```json
{
  "error": "Descripción del error"
}
```

---

## Bloqueo de Navegadores

### BrowserBlockFilter - Restricción de Acceso desde Navegadores

**Ubicación:** `com.cyberpath.smartlearn.configuracion.seguridad.jwt.BrowserBlockFilter`

### Propósito

Prevenir acceso directo a APIs desde navegadores web. La API está diseñada solo para aplicaciones móviles o clientes con JWT válido.

### Mecanismo de Detección

Se detecta navegador analizando el User-Agent Header:

```java
private boolean isBrowserUserAgent(HttpServletRequest request) {
    String ua = request.getHeader("User-Agent");
    ua = ua.toLowerCase();
    return ua.contains("mozilla") || ua.contains("chrome") 
        || ua.contains("safari") || ua.contains("firefox")
        || ua.contains("edge") || ua.contains("opera");
}
```

### Excepciones

1. **Token válido**: Si Authorization header tiene token Bearer, se permite
2. **Header X-Client-Type**: Si es "mobile", se ignora detección
3. **Ruta permitida**: GET a `/smartlearn/api/usuario` está permitido

### Comportamiento

Cuando se detecta acceso desde navegador sin token:
1. Se retorna HTTP 403 (Forbidden)
2. Se devuelve página HTML con mensaje legible

```html
<!doctype html>
<html>
<head>
  <title>Acceso Restringido</title>
</head>
<body>
  <div class="card">
    <h1>Acceso Restringido</h1>
    <p>No tienes permiso para acceder a esta página 
       o tu sesión ha expirado.</p>
  </div>
</body>
</html>
```

### Implementación en Cliente

Para omitir el bloqueo, el cliente debe:

1. **Opción 1: Usar token JWT válido**
```
GET /smartlearn/api/usuario/materias
Authorization: ******
```

2. **Opción 2: Enviar header X-Client-Type**
```
GET /smartlearn/api/recurso/imagen
X-Client-Type: mobile
```

---

## Encriptación y Cifrado

### Estrategias de Encriptación Implementadas

#### 1. BCrypt para Contraseñas

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**Características:**
- Algoritmo: bcrypt (derivado de Blowfish)
- Salt: Generado automáticamente (log rounds = 10)
- Longitud: $2a$10$... (formato estándar)
- Imposible revertir a contraseña original

**Ejemplo de hash:**
```
Contraseña: "mi_password_123"
Hash: $2a$10$7J7H8k9L2m3N4o5P6q7R8s.tU.vWxYzAbCdEfGhIjKlMnOpQrStUvW
```

**Validación:**
```java
if (passwordEncoder.matches(inputPassword, storedHash)) {
    // Contraseña correcta
}
```

#### 2. JWT - Firma HMAC-SHA256

Aunque JWT no cifra datos, los firma para garantizar integridad:

```
Header: {"alg":"HS256","typ":"JWT"}
Payload: {"sub":"usuario@email.com","exp":1717845745,...}
Signature: HMACSHA256(base64(header)+"."+base64(payload), secret)
```

Si alguien modifica el payload, la firma será inválida.

#### 3. Secretos TOTP Encriptados (TODO)

Actualmente está en modo TODO:
```java
private String encryptSecret(String secret) {
    return secret; // TODO: implementar cifrado real
}
```

**Recomendación para producción:**
- Usar AES-256-GCM
- Implementar Key Management Service (KMS)
- Guardar claves de encriptación separadas

---

## Gestión de Contraseñas

### Registro de Usuario

```
POST /smartlearn/api/usuario/registro
```

1. Se valida que la contraseña sea válida
2. Se encripta con BCrypt
3. Se requiere verificación por correo (2FA)
4. Usuario solo se activa después de verificar correo

### Cambio de Contraseña

**Archivo:** `CambioPasswordDto`

```
PUT /smartlearn/api/usuario/{id}/cambiar-contrasena
Body: {
  "contrasenaActual": "...",
  "contrasenaNueva": "..."
}
```

**Seguridad:**
1. Se verifica contraseña actual
2. Se valida que nueva sea diferente
3. Se encripta nueva contraseña
4. Se actualiza en BD

### Recuperación de Contraseña (si existe)

Generalmente se hace a través de:
1. Verificación por correo
2. Enlace con token temporal
3. Set de nueva contraseña

---

## Validación y Sanitización

### Validación de Entrada

Se valida en múltiples niveles:

1. **DTOs con validación**
```java
@Data
public class LoginRequest {
    @NotBlank
    private String correo;
    
    @NotBlank
    private String contrasena;
}
```

2. **Lógica de negocio**
```java
// En UsuarioControlador
if (request.getTransactionId() == null || 
    request.getTransactionId().isBlank()) {
    return ResponseEntity.badRequest().body(...);
}
```

3. **Querys parametrizadas (Hibernat/JPA)**
```java
// SEGURO - Sin SQL injection
Usuario usuario = usuarioRepositorio.findByCorreo(email);

// NO hacer esto:
// Query q = session.createQuery("FROM Usuario WHERE correo = '" + email + "'");
```

### Sanitización JSON

Para prevenir XSS en respuestas JSON:

```java
private String escapeJson(String text) {
    return text == null ? "" : text.replace("\"", "\\\"");
}

// Uso
response.getWriter().write("{\"error\":\"" + escapeJson(message) + "\"}");
```

### CSRF (Cross-Site Request Forgery)

```java
.csrf(csrf -> csrf.disable())
```

**Nota:** CSRF está deshabilitado porque la API es stateless (JWT). En aplicaciones con sesiones, se debe habilitar CSRF.

---

## Configuración de Seguridad

### application.properties

```properties
# ==================== DATASOURCE ====================
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/smartlearn
spring.datasource.username=root
spring.datasource.******

# ==================== JWT ====================
jwt.secret=miClaveSuperSecretaParaJWT2026SmartLearnQueDebeSerMuyLargaYCompleja123456789
jwt.expiration=86400000        # 24 horas en ms
jwt.refresh-expiration=1296000000  # 15 días en ms

# ==================== MAIL ====================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=cyberpathcontacto@gmail.com
spring.mail.******
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# ==================== LOGGING ====================
logging.level.com.cyberpath=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Recomendaciones de Seguridad para Producción

1. **Variables de Entorno**
   - Usar variables de entorno para secretos
   - NO commitear credenciales en código

```properties
# En archivo .env (NO en git)
JWT_SECRET=${JWT_SECRET}
DB_PASSWORD=${DB_PASSWORD}
MAIL_PASSWORD=${MAIL_PASSWORD}
```

2. **Aumentar Clave JWT**
   - Mínimo 32 bytes (256 bits)
   - Usar generador criptográfico fuerte

3. **HTTPS**
   - Todas las conexiones deben ser HTTPS
   - Certificados válidos y actualizados

4. **Implementar Encriptación Real**
   - AES-256 para secrets TOTP
   - KMS para gestión de claves

5. **Logging y Monitoreo**
   - Registrar intentos fallidos
   - Alertas para patrones sospechosos
   - Auditoría de cambios sensibles

6. **Rate Limiting**
   - Limitar intentos de login
   - Proteger contra fuerza bruta

7. **Actualizar Dependencias**
   - Mantener Spring Security actualizado
   - Parches de seguridad regulares

---

## Resumen de Medidas de Seguridad

| Medida | Implementación | Ubicación |
|--------|-----------------|-----------|
| Autenticación JWT | HS256 | JwtService |
| Autorización | RBAC por roles | SecurityConfig |
| 2FA | Email/TOTP/SMS | TwoFactorServicioImpl |
| Contraseñas | BCrypt | SecurityConfig |
| Bloqueo navegador | User-Agent | BrowserBlockFilter |
| Tokens expiración | 24h + 15d refresh | application.properties |
| Recovery codes | 10 códigos de 12 chars | TwoFactorServicioImpl |
| Dispositivos confiables | Token + 30 días | TwoFactorServicioImpl |
| CORS | Habilitado controlado | SecurityConfig |
| Validación input | DTOs + lógica | Controllers |
| Sanitización JSON | Escape caracteres | JwtAuthFilter |

---

## Flujo Completo de Login Seguro

```
1. Usuario ingresa correo + contraseña
   ↓
2. Sistema valida contra BD (BCrypt)
   ↓
3. ¿Verificado? Si no → Error 403
   ↓
4. ¿2FA habilitado? 
   ├─ Si → Enviar código por email → Esperar verificación
   │   ├─ Usuario verifica código
   │   ├─ Sistema genera Access + Refresh token
   │   ├─ ¿Recordar dispositivo? → Guardar device token
   │   └─ Retornar tokens + info usuario
   │
   └─ No → Generar tokens directamente
       └─ Retornar Access + Refresh token
       
5. Cliente almacena tokens
   ↓
6. Cliente envía requests con "Authorization: ******"
   ↓
7. JwtAuthFilter valida token en cada request
   ↓
8. Si token expirado + refresh válido → Renovar automáticamente
   ↓
9. Usuario accede a recursos protegidos
```

---

## Conclusión

SmartLearn implementa un sistema de seguridad multicapa que protege:

✅ **Autenticación**: JWT con tokens de corta y larga duración  
✅ **Verificación de identidad**: 2FA mediante email, TOTP o SMS  
✅ **Encriptación**: BCrypt para contraseñas, HMAC para JWT  
✅ **Control de acceso**: RBAC basado en roles  
✅ **Bloqueo de navegadores**: Prevención de acceso no autorizado  
✅ **Gestión de dispositivos**: Tokens confiables con expiración  
✅ **Códigos de recuperación**: Para casos de pérdida de 2FA  
✅ **Validación robusta**: Sanitización y escape de caracteres  

El sistema es robusto pero recomendamos revisar y mejorar las áreas marcadas como TODO, especialmente la encriptación de secretos TOTP, para un entorno de producción.
