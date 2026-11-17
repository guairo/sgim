-- ==========================================================
-- SGIM - Esquema de base de datos MySQL 8.x
-- Motor: InnoDB, charset utf8mb4_spanish_ci
-- ==========================================================

CREATE DATABASE IF NOT EXISTS sgim
    CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE sgim;

CREATE TABLE dependencia (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(250),
  ubicacion VARCHAR(150),
  activa TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE categoria (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(80) NOT NULL UNIQUE,
  descripcion VARCHAR(250),
  vida_util_meses INT,
  activa TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE proveedor (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nit VARCHAR(20) NOT NULL UNIQUE,
  razon_social VARCHAR(150) NOT NULL,
  telefono VARCHAR(20),
  correo VARCHAR(120),
  direccion VARCHAR(200),
  activo TINYINT(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE funcionario (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  documento VARCHAR(20) NOT NULL UNIQUE,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  cargo VARCHAR(80),
  correo VARCHAR(120),
  telefono VARCHAR(20),
  dependencia_id BIGINT NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  CONSTRAINT fk_func_dep FOREIGN KEY (dependencia_id) REFERENCES dependencia(id)
) ENGINE=InnoDB;

CREATE TABLE bien (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  placa VARCHAR(25) NOT NULL UNIQUE,
  nombre VARCHAR(150) NOT NULL,
  descripcion VARCHAR(500),
  marca VARCHAR(60),
  modelo VARCHAR(60),
  serial VARCHAR(60),
  valor_compra DECIMAL(15,2) NOT NULL,
  fecha_compra DATE,
  estado VARCHAR(20) NOT NULL,
  categoria_id BIGINT NOT NULL,
  proveedor_id BIGINT,
  dependencia_id BIGINT NOT NULL,
  funcionario_id BIGINT,
  fecha_registro DATETIME,
  fecha_actualizacion DATETIME,
  CONSTRAINT fk_bien_cat FOREIGN KEY (categoria_id)   REFERENCES categoria(id),
  CONSTRAINT fk_bien_prv FOREIGN KEY (proveedor_id)   REFERENCES proveedor(id),
  CONSTRAINT fk_bien_dep FOREIGN KEY (dependencia_id) REFERENCES dependencia(id),
  CONSTRAINT fk_bien_fnc FOREIGN KEY (funcionario_id) REFERENCES funcionario(id),
  INDEX idx_bien_estado (estado)
) ENGINE=InnoDB;

CREATE TABLE movimiento (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tipo VARCHAR(20) NOT NULL,
  bien_id BIGINT NOT NULL,
  dependencia_origen_id BIGINT,
  dependencia_destino_id BIGINT,
  funcionario_origen_id BIGINT,
  funcionario_destino_id BIGINT,
  observaciones VARCHAR(500),
  fecha_movimiento DATETIME NOT NULL,
  usuario_registro VARCHAR(60),
  CONSTRAINT fk_mov_bien FOREIGN KEY (bien_id) REFERENCES bien(id),
  CONSTRAINT fk_mov_do   FOREIGN KEY (dependencia_origen_id)  REFERENCES dependencia(id),
  CONSTRAINT fk_mov_dd   FOREIGN KEY (dependencia_destino_id) REFERENCES dependencia(id),
  CONSTRAINT fk_mov_fo   FOREIGN KEY (funcionario_origen_id)  REFERENCES funcionario(id),
  CONSTRAINT fk_mov_fd   FOREIGN KEY (funcionario_destino_id) REFERENCES funcionario(id)
) ENGINE=InnoDB;

CREATE TABLE mantenimiento (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  bien_id BIGINT NOT NULL,
  tipo VARCHAR(20) NOT NULL,
  estado VARCHAR(20) NOT NULL,
  fecha_programada DATE,
  fecha_ejecucion DATE,
  descripcion VARCHAR(500),
  costo DECIMAL(15,2),
  proveedor_id BIGINT,
  observaciones VARCHAR(500),
  CONSTRAINT fk_mant_bien FOREIGN KEY (bien_id) REFERENCES bien(id),
  CONSTRAINT fk_mant_prv  FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
) ENGINE=InnoDB;

CREATE TABLE usuario_sistema (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  nombre_completo VARCHAR(100) NOT NULL,
  correo VARCHAR(120) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  rol VARCHAR(20) NOT NULL,
  activo TINYINT(1) NOT NULL DEFAULT 1,
  ultimo_acceso DATETIME
) ENGINE=InnoDB;

CREATE TABLE auditoria (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  accion VARCHAR(20) NOT NULL,
  usuario VARCHAR(60) NOT NULL,
  entidad VARCHAR(60) NOT NULL,
  entidad_id VARCHAR(40),
  detalle VARCHAR(500),
  ip_origen VARCHAR(45),
  fecha DATETIME NOT NULL,
  INDEX idx_audit_fecha (fecha),
  INDEX idx_audit_usuario (usuario)
) ENGINE=InnoDB;
