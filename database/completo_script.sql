-- ============================================================================
-- SANOS Y SALVOS - SCRIPT SQL COMPLETO
-- Contiene todas las tablas necesarias para los 6 microservicios
-- Copiar y pegar en phpMyAdmin
-- ============================================================================

-- ============================================================================
-- 1. BASE DE DATOS: sanosysalvos_usuarios
-- ============================================================================
CREATE DATABASE IF NOT EXISTS sanosysalvos_usuarios;
USE sanosysalvos_usuarios;

-- Tabla: rol
CREATE TABLE IF NOT EXISTS rol (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL UNIQUE,
  descripcion VARCHAR(255),
  CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: tipo_organizacion
CREATE TABLE IF NOT EXISTS tipo_organizacion (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(255),
  CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: organizacion
CREATE TABLE IF NOT EXISTS organizacion (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  rut VARCHAR(50) UNIQUE,
  tipo_id BIGINT NOT NULL,
  telefono VARCHAR(20),
  email VARCHAR(100),
  direccion VARCHAR(255),
  ciudad VARCHAR(100),
  region VARCHAR(100),
  codigo_postal VARCHAR(10),
  estado VARCHAR(50) DEFAULT 'ACTIVO',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (tipo_id) REFERENCES tipo_organizacion(id)
);

-- Tabla: usuario
CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nombre VARCHAR(100),
  apellido VARCHAR(100),
  rol_id BIGINT NOT NULL,
  organizacion_id BIGINT,
  telefono VARCHAR(20),
  direccion VARCHAR(255),
  ciudad VARCHAR(100),
  region VARCHAR(100),
  codigo_postal VARCHAR(10),
  estado VARCHAR(50) DEFAULT 'ACTIVO',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (rol_id) REFERENCES rol(id),
  FOREIGN KEY (organizacion_id) REFERENCES organizacion(id)
);

-- Insertar roles por defecto
INSERT INTO rol (nombre, descripcion) VALUES
('CIUDADANO', 'Usuario ciudadano'),
('CLINICA', 'Clínica veterinaria'),
('REFUGIO', 'Refugio de animales'),
('MUNICIPALIDAD', 'Municipalidad'),
('ADMIN', 'Administrador del sistema');

-- Insertar tipos de organización
INSERT INTO tipo_organizacion (nombre, descripcion) VALUES
('Clínica Veterinaria', 'Clínica de atención veterinaria'),
('Refugio', 'Centro de acogida de animales'),
('Municipalidad', 'Institución municipal'),
('ONG', 'Organización no gubernamental');

-- ============================================================================
-- 2. BASE DE DATOS: sanosysalvos (Reportes, Geolocalizacion, Coincidencias)
-- ============================================================================
CREATE DATABASE IF NOT EXISTS sanosysalvos;
USE sanosysalvos;

-- ============================================================================
-- TABLAS MS REPORTES
-- ============================================================================

-- Tabla: caracteristica_mascota
CREATE TABLE IF NOT EXISTS caracteristica_mascota (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tamaño VARCHAR(50),
  sexo VARCHAR(20),
  edad_aproximada VARCHAR(100),
  descripcion_fisica VARCHAR(255),
  peso DECIMAL(5,2),
  senas_particulares VARCHAR(255),
  vacunado BOOLEAN DEFAULT FALSE,
  esterilizado BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: foto_mascota
CREATE TABLE IF NOT EXISTS foto_mascota (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  url_foto LONGTEXT,
  descripcion VARCHAR(255),
  es_principal BOOLEAN DEFAULT FALSE,
  orden INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: mascota
CREATE TABLE IF NOT EXISTS mascota (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  tipo VARCHAR(50),
  raza VARCHAR(100),
  color VARCHAR(100),
  caracteristica_id BIGINT UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (caracteristica_id) REFERENCES caracteristica_mascota(id)
);

-- Tabla: reporte
CREATE TABLE IF NOT EXISTS reporte (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tipo VARCHAR(50) NOT NULL,
  estado VARCHAR(50) DEFAULT 'ABIERTO',
  mascota_id BIGINT UNIQUE,
  usuario_id BIGINT,
  organizacion_id BIGINT,
  ubicacion VARCHAR(255),
  latitud DOUBLE,
  longitud DOUBLE,
  descripcion LONGTEXT,
  prioridad INT DEFAULT 3,
  es_cachorro BOOLEAN DEFAULT FALSE,
  num_visualizaciones INT DEFAULT 0,
  reportes_coincidentes JSON,
  telefono_contacto VARCHAR(20),
  email_contacto VARCHAR(100),
  fecha_incidente DATETIME,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_resolucion DATETIME,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (mascota_id) REFERENCES mascota(id)
);

-- Agregar relación de fotos en mascota después de crear la tabla
ALTER TABLE mascota ADD COLUMN foto_principal_id BIGINT;

-- ============================================================================
-- TABLAS MS GEOLOCALIZACION
-- ============================================================================

-- Tabla: ubicacion
CREATE TABLE IF NOT EXISTS ubicacion (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reporte_id BIGINT,
  calle VARCHAR(255),
  numero VARCHAR(20),
  comuna VARCHAR(100),
  ciudad VARCHAR(100),
  region VARCHAR(100),
  codigo_postal VARCHAR(10),
  latitud DOUBLE NOT NULL,
  longitud DOUBLE NOT NULL,
  tipo_evento VARCHAR(100),
  accuracy INT,
  fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (reporte_id) REFERENCES reporte(id)
);

-- Tabla: zona_incidencia
CREATE TABLE IF NOT EXISTS zona_incidencia (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  latitud_centro DOUBLE NOT NULL,
  longitud_centro DOUBLE NOT NULL,
  radio_km DECIMAL(10,2),
  num_incidencias INT DEFAULT 0,
  num_perdidas INT DEFAULT 0,
  num_encontradas INT DEFAULT 0,
  tasa_recuperacion DECIMAL(5,2),
  nivel_riesgo VARCHAR(50) DEFAULT 'BAJO',
  observaciones LONGTEXT,
  es_activa BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla: historial_ubicacion
CREATE TABLE IF NOT EXISTS historial_ubicacion (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reporte_id BIGINT NOT NULL,
  latitud DOUBLE NOT NULL,
  longitud DOUBLE NOT NULL,
  tipo_evento VARCHAR(100),
  fuente_informacion VARCHAR(100),
  confiabilidad INT,
  quien_reporta_id BIGINT,
  fecha_evento DATETIME,
  detalles LONGTEXT,
  comprobado BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (reporte_id) REFERENCES reporte(id)
);

-- ============================================================================
-- TABLAS MS COINCIDENCIAS
-- ============================================================================

-- Tabla: puntaje_coincidencia (se guarda como JSON en coincidencia)
-- Esta es solo para referencia en consultas

-- Tabla: coincidencia
CREATE TABLE IF NOT EXISTS coincidencia (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reporte_perdido_id BIGINT NOT NULL,
  reporte_encontrado_id BIGINT NOT NULL,
  puntaje_total DECIMAL(5,2),
  detalles_puntaje JSON,
  estado VARCHAR(50) DEFAULT 'PENDIENTE_REVISION',
  fecha_analisis DATETIME,
  fecha_confirmacion DATETIME,
  usuario_quien_confirmo BIGINT,
  motivo_rechazo VARCHAR(255),
  version INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (reporte_perdido_id) REFERENCES reporte(id),
  FOREIGN KEY (reporte_encontrado_id) REFERENCES reporte(id)
);

-- ============================================================================
-- 3. BASE DE DATOS: sanosysalvos_notificaciones
-- ============================================================================
CREATE DATABASE IF NOT EXISTS sanosysalvos_notificaciones;
USE sanosysalvos_notificaciones;

-- Tabla: notificacion
CREATE TABLE IF NOT EXISTS notificacion (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  usuario_id BIGINT NOT NULL,
  tipo VARCHAR(50) NOT NULL,
  asunto VARCHAR(255),
  contenido LONGTEXT,
  destinatario VARCHAR(100),
  estado VARCHAR(50) DEFAULT 'PENDIENTE',
  evento_tipo VARCHAR(100),
  evento_id BIGINT,
  intentos_envio INT DEFAULT 0,
  mensaje_error LONGTEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================================
-- ÍNDICES PARA OPTIMIZAR QUERIES
-- ============================================================================

USE sanosysalvos;

-- Índices en reporte
CREATE INDEX idx_reporte_tipo ON reporte(tipo);
CREATE INDEX idx_reporte_estado ON reporte(estado);
CREATE INDEX idx_reporte_usuario ON reporte(usuario_id);
CREATE INDEX idx_reporte_organizacion ON reporte(organizacion_id);
CREATE INDEX idx_reporte_fecha ON reporte(fecha_creacion);
CREATE INDEX idx_reporte_ubicacion ON reporte(latitud, longitud);

-- Índices en ubicacion
CREATE INDEX idx_ubicacion_reporte ON ubicacion(reporte_id);
CREATE INDEX idx_ubicacion_coords ON ubicacion(latitud, longitud);
CREATE INDEX idx_ubicacion_comuna ON ubicacion(comuna);

-- Índices en zona_incidencia
CREATE INDEX idx_zona_ubicacion ON zona_incidencia(latitud_centro, longitud_centro);
CREATE INDEX idx_zona_riesgo ON zona_incidencia(nivel_riesgo);

-- Índices en coincidencia
CREATE INDEX idx_coincidencia_reportes ON coincidencia(reporte_perdido_id, reporte_encontrado_id);
CREATE INDEX idx_coincidencia_estado ON coincidencia(estado);
CREATE INDEX idx_coincidencia_puntaje ON coincidencia(puntaje_total);

-- Índices en notificacion
USE sanosysalvos_notificaciones;
CREATE INDEX idx_notificacion_usuario ON notificacion(usuario_id);
CREATE INDEX idx_notificacion_tipo ON notificacion(tipo);
CREATE INDEX idx_notificacion_estado ON notificacion(estado);
CREATE INDEX idx_notificacion_evento ON notificacion(evento_tipo, evento_id);

-- Índices en usuario y organizacion
USE sanosysalvos_usuarios;
CREATE INDEX idx_usuario_username ON usuario(username);
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_rol ON usuario(rol_id);
CREATE INDEX idx_usuario_organizacion ON usuario(organizacion_id);
CREATE INDEX idx_organizacion_tipo ON organizacion(tipo_id);
CREATE INDEX idx_organizacion_estado ON organizacion(estado);

-- ============================================================================
-- FIN DEL SCRIPT
-- ============================================================================
