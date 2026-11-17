# SGIM — Sistema de Gestión para Inventario Municipal

Proyecto formativo **SENA ADSO** — aplicación web para la gestión del inventario de bienes muebles de un municipio de sexta categoría.

## 1. Arquitectura

Aplicación web monolítica en **Spring Boot 3** con arquitectura por capas:

```
┌──────────────────────────────────────────┐
│  Vista (Thymeleaf + Bootstrap 5)         │  <- templates/
├──────────────────────────────────────────┤
│  Controlador (Spring MVC)                │  <- controller/
├──────────────────────────────────────────┤
│  Servicio (reglas de negocio + @Trans.)  │  <- service/
├──────────────────────────────────────────┤
│  Repositorio (Spring Data JPA)           │  <- repository/
├──────────────────────────────────────────┤
│  Modelo (Entidades JPA / Hibernate)      │  <- model/
├──────────────────────────────────────────┤
│  Base de datos (MySQL 8 / H2 en dev)     │
└──────────────────────────────────────────┘
```

**Patrones aplicados:** MVC, Repository, Service Layer, DTO (parcial), Dependency Injection, Front Controller (DispatcherServlet), Template Method (Thymeleaf fragments).

## 2. Módulos del sistema (9)

| Módulo              | Paquete                      | Descripción                                          |
|---------------------|------------------------------|------------------------------------------------------|
| Dependencias        | `controller/service/...`     | Oficinas responsables de los bienes                  |
| Categorías          | idem                         | Clasificación de bienes                              |
| Proveedores         | idem                         | Entidades que suministran los bienes                 |
| Funcionarios        | idem                         | Personal al que se asignan bienes                    |
| **Bienes**          | idem                         | Módulo central: CRUD + asignación + estado           |
| Movimientos         | idem                         | Historial de traslados / asignaciones / bajas        |
| Mantenimientos      | idem                         | Programación y ejecución                             |
| Usuarios del sistema| idem                         | Cuentas de acceso con rol                            |
| Auditoría           | idem                         | Trazabilidad de operaciones críticas                 |

## 3. Requisitos

- **JDK 17+**
- **Maven 3.9+**
- **MySQL 8.x** (sólo para perfil `prod`; dev usa H2 en memoria)

## 4. Ejecución en desarrollo (H2)

```bash
mvn spring-boot:run
```

- URL: `http://localhost:8080`
- Consola H2: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:sgimdb`)

**Usuarios de prueba** (creados automáticamente por `DataInitializer`):

| Usuario    | Contraseña  | Rol        |
|------------|-------------|------------|
| `admin`    | `admin123`  | ADMIN      |
| `operador` | `oper123`   | OPERADOR   |
| `consulta` | `cons123`   | CONSULTA   |

## 5. Ejecución en producción (MySQL)

1. Crear la base de datos con `docs/schema.sql`:
   ```bash
   mysql -u root -p < docs/schema.sql
   ```
2. Exportar variables:
   ```bash
   export DB_URL="jdbc:mysql://localhost:3306/sgim?useSSL=false&serverTimezone=America/Bogota"
   export DB_USER="sgim"
   export DB_PASS="tu_password"
   ```
3. Empaquetar y ejecutar:
   ```bash
   mvn clean package -DskipTests
   java -jar target/sgim.jar --spring.profiles.active=prod
   ```

## 6. Pruebas unitarias

```bash
mvn test
```

Se incluyen pruebas con **JUnit 5 + Mockito + AssertJ** para la capa de servicios:

- `BienServiceTest` — alta duplicada, creación válida, asignación, liberación, estado `BAJA`.
- `UsuarioSistemaServiceTest` — encriptación BCrypt, unicidad de username.
- `SgimApplicationTests` — smoke test de arranque de contexto.

## 7. Seguridad

- Autenticación por formulario con Spring Security.
- Contraseñas con **BCrypt** (cost 10).
- Autorización basada en roles: `ADMIN`, `OPERADOR`, `CONSULTA`.
- Protección CSRF habilitada en formularios.
- Auditoría de operaciones sensibles (crear / actualizar / eliminar).

## 8. Librerías y frameworks

| Capa          | Tecnología                         |
|---------------|------------------------------------|
| Core          | Spring Boot 3.3                    |
| Web / MVC     | Spring Web                         |
| Vista         | Thymeleaf, Bootstrap 5, Bootstrap Icons |
| Persistencia  | Spring Data JPA + Hibernate        |
| BD            | MySQL 8 (prod), H2 (dev)           |
| Seguridad     | Spring Security 6 + BCrypt         |
| Validación    | Jakarta Bean Validation            |
| Boilerplate   | Lombok                             |
| Pruebas       | JUnit 5, Mockito, AssertJ, Spring Security Test |

## 9. Mapa de navegación

```
/login
  └─ /dashboard
      ├─ /bienes          (listar | nuevo | editar | asignar | liberar | eliminar)
      ├─ /movimientos     (listar | nuevo)
      ├─ /mantenimientos  (listar | programar | iniciar | finalizar)
      ├─ /dependencias    (CRUD)
      ├─ /categorias      (CRUD)
      ├─ /proveedores     (CRUD)
      ├─ /funcionarios    (CRUD)
      ├─ /usuarios        (CRUD, solo ADMIN)
      └─ /auditoria       (consulta, solo ADMIN)
```

## 10. Buenas prácticas aplicadas

- Nomenclatura en español para dominio, inglés para infraestructura.
- Separación de responsabilidades (controlador delgado, lógica en servicios).
- Transaccionalidad declarativa (`@Transactional`).
- Manejo global de excepciones (`GlobalExceptionHandler`).
- Validación en capa de modelo con Bean Validation.
- Paginación en listados de alto volumen (bienes, movimientos, auditoría).
- Reutilización de plantillas con fragmentos Thymeleaf.
- Configuración externa por perfiles (`application-dev`, `application-prod`).

## 11. Repositorio / control de versiones

```bash
git init
git add .
git commit -m "feat: estructura inicial SGIM Spring Boot"
git branch -M main
git remote add origin https://github.com/guairo/sgim.git
git push -u origin main
```

---
Autor: **Jonhatan Rivadeneira Pacheco** — SENA ADSO.
