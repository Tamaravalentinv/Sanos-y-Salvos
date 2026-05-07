-- ============================================================================
-- SANOS Y SALVOS - SCRIPT SQL COMPLETO PARA XAMPP (phpMyAdmin)
-- Compatible con MySQL 5.7+ / MariaDB 10.3+
-- Incluye: ms-usuarios, ms-reportes, ms-geolocalizacion,
--          ms-coincidencias, ms-notificaciones, ms-proyectos, ms-recursos-humanos
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================================
-- 1. BASE DE DATOS: sanosysalvos_usuarios
--    Microservicio: ms-usuarios (puerto 8084)
-- ============================================================================
CREATE DATABASE IF NOT EXISTS sanosysalvos_usuarios
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sanosysalvos_usuarios;

-- Tabla: roles
CREATE TABLE IF NOT EXISTS roles (
  id     BIGINT       NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50)  NOT NULL UNIQUE,
  descripcion VARCHAR(255),
  PRIMARY KEY (id)
);

-- Tabla: tipos_organizacion
CREATE TABLE IF NOT EXISTS tipos_organizacion (
  id     BIGINT       NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(255),
  PRIMARY KEY (id)
);

-- Tabla: contactos
-- Usada por usuarios y organizaciones (OneToOne)
CREATE TABLE IF NOT EXISTS contactos (
  id            BIGINT       NOT NULL AUTO_INCREMENT,
  telefono      VARCHAR(20),
  email         VARCHAR(150) UNIQUE,
  direccion     VARCHAR(255),
  ciudad        VARCHAR(100),
  region        VARCHAR(100),
  codigo_postal VARCHAR(20),
  PRIMARY KEY (id)
);

-- Tabla: organizaciones
CREATE TABLE IF NOT EXISTS organizaciones (
  id                  BIGINT       NOT NULL AUTO_INCREMENT,
  nombre              VARCHAR(200) NOT NULL,
  tipo_organizacion_id BIGINT      NOT NULL,
  descripcion         VARCHAR(255),
  contacto_id         BIGINT       UNIQUE,
  rut                 VARCHAR(50),
  estado              VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
  fecha_registro      DATETIME,
  numero_empleados    INT,
  sitio_web           VARCHAR(255),
  es_verificada       TINYINT(1)   DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (tipo_organizacion_id) REFERENCES tipos_organizacion(id),
  FOREIGN KEY (contacto_id) REFERENCES contactos(id)
);

-- Tabla: usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id                    BIGINT       NOT NULL AUTO_INCREMENT,
  username              VARCHAR(100) NOT NULL UNIQUE,
  email                 VARCHAR(150) NOT NULL UNIQUE,
  password              VARCHAR(255) NOT NULL,
  nombre                VARCHAR(100),
  apellido              VARCHAR(100),
  rol_id                BIGINT       NOT NULL,
  contacto_id           BIGINT       UNIQUE,
  estado                VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion        DATETIME,
  fecha_ultima_conexion DATETIME,
  organizacion_id       BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (rol_id) REFERENCES roles(id),
  FOREIGN KEY (contacto_id) REFERENCES contactos(id),
  FOREIGN KEY (organizacion_id) REFERENCES organizaciones(id)
);

-- Datos iniciales: roles
INSERT INTO roles (nombre, descripcion) VALUES
  ('CIUDADANO',    'Usuario ciudadano estándar'),
  ('CLINICA',      'Clínica veterinaria'),
  ('REFUGIO',      'Refugio de animales'),
  ('MUNICIPALIDAD','Institución municipal'),
  ('ADMIN',        'Administrador del sistema')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Datos iniciales: tipos de organización
INSERT INTO tipos_organizacion (nombre, descripcion) VALUES
  ('Clínica Veterinaria', 'Clínica de atención veterinaria'),
  ('Refugio',             'Centro de acogida de animales'),
  ('Municipalidad',       'Institución municipal'),
  ('ONG',                 'Organización no gubernamental')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- Índices
CREATE INDEX idx_usuarios_rol          ON usuarios(rol_id);
CREATE INDEX idx_usuarios_organizacion ON usuarios(organizacion_id);
CREATE INDEX idx_organizaciones_tipo   ON organizaciones(tipo_organizacion_id);
CREATE INDEX idx_organizaciones_estado ON organizaciones(estado);

-- ============================================================================
-- 2. BASE DE DATOS: sanosysalvos
--    Microservicios: ms-reportes (8083), ms-geolocalizacion (8081),
--                   ms-coincidencias (8082), ms-proyectos (8086),
--                   ms-recursos-humanos (8087)
-- ============================================================================
CREATE DATABASE IF NOT EXISTS sanosysalvos
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sanosysalvos;

-- ============================================================================
-- MS REPORTES (puerto 8083)
-- ============================================================================

-- Tabla: caracteristica_mascota
CREATE TABLE IF NOT EXISTS caracteristica_mascota (
  id                  BIGINT        NOT NULL AUTO_INCREMENT,
  tamanio             VARCHAR(50),
  sexo                VARCHAR(20),
  edad_aproximada     VARCHAR(100),
  descripcion_fisica  VARCHAR(255),
  peso                DECIMAL(5,2),
  senas_particulares  VARCHAR(255),
  vacunado            TINYINT(1)    DEFAULT 0,
  esterilizado        TINYINT(1)    DEFAULT 0,
  created_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

-- Tabla: foto_mascota
CREATE TABLE IF NOT EXISTS foto_mascota (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  url_foto    LONGTEXT,
  descripcion VARCHAR(255),
  es_principal TINYINT(1)  DEFAULT 0,
  orden       INT,
  created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

-- Tabla: mascota
CREATE TABLE IF NOT EXISTS mascota (
  id                BIGINT       NOT NULL AUTO_INCREMENT,
  nombre            VARCHAR(100),
  tipo              VARCHAR(50),
  raza              VARCHAR(100),
  color             VARCHAR(100),
  caracteristica_id BIGINT       UNIQUE,
  foto_principal_id BIGINT,
  created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (caracteristica_id) REFERENCES caracteristica_mascota(id)
);

-- Tabla: reporte
CREATE TABLE IF NOT EXISTS reporte (
  id                   BIGINT       NOT NULL AUTO_INCREMENT,
  tipo                 VARCHAR(50)  NOT NULL,
  estado               VARCHAR(50)  NOT NULL DEFAULT 'ABIERTO',
  mascota_id           BIGINT       UNIQUE,
  usuario_id           BIGINT,
  organizacion_id      BIGINT,
  ubicacion            VARCHAR(255),
  latitud              DOUBLE,
  longitud             DOUBLE,
  descripcion          LONGTEXT,
  prioridad            INT          DEFAULT 3,
  es_cachorro          TINYINT(1)   DEFAULT 0,
  num_visualizaciones  INT          DEFAULT 0,
  reportes_coincidentes JSON,
  telefono_contacto    VARCHAR(20),
  email_contacto       VARCHAR(100),
  fecha_incidente      DATETIME,
  fecha_creacion       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_resolucion     DATETIME,
  updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (mascota_id) REFERENCES mascota(id)
);

-- Índices de reportes
CREATE INDEX idx_reporte_tipo       ON reporte(tipo);
CREATE INDEX idx_reporte_estado     ON reporte(estado);
CREATE INDEX idx_reporte_usuario    ON reporte(usuario_id);
CREATE INDEX idx_reporte_org        ON reporte(organizacion_id);
CREATE INDEX idx_reporte_fecha      ON reporte(fecha_creacion);
CREATE INDEX idx_reporte_coords     ON reporte(latitud, longitud);

-- ============================================================================
-- MS GEOLOCALIZACION (puerto 8081)
-- ============================================================================

-- Tabla: ubicacion
CREATE TABLE IF NOT EXISTS ubicacion (
  id            BIGINT       NOT NULL AUTO_INCREMENT,
  reporte_id    BIGINT,
  calle         VARCHAR(255),
  numero        VARCHAR(20),
  comuna        VARCHAR(100),
  ciudad        VARCHAR(100),
  region        VARCHAR(100),
  codigo_postal VARCHAR(10),
  latitud       DOUBLE       NOT NULL,
  longitud      DOUBLE       NOT NULL,
  tipo_evento   VARCHAR(100),
  accuracy      INT,
  fecha_registro TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (reporte_id) REFERENCES reporte(id)
);

-- Tabla: zona_incidencia
CREATE TABLE IF NOT EXISTS zona_incidencia (
  id                BIGINT        NOT NULL AUTO_INCREMENT,
  nombre            VARCHAR(255)  NOT NULL,
  latitud_centro    DOUBLE        NOT NULL,
  longitud_centro   DOUBLE        NOT NULL,
  radio_km          DECIMAL(10,2),
  num_incidencias   INT           DEFAULT 0,
  num_perdidas      INT           DEFAULT 0,
  num_encontradas   INT           DEFAULT 0,
  tasa_recuperacion DECIMAL(5,2),
  nivel_riesgo      VARCHAR(50)   DEFAULT 'BAJO',
  observaciones     LONGTEXT,
  es_activa         TINYINT(1)    DEFAULT 1,
  created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

-- Tabla: historial_ubicacion
CREATE TABLE IF NOT EXISTS historial_ubicacion (
  id                  BIGINT       NOT NULL AUTO_INCREMENT,
  reporte_id          BIGINT       NOT NULL,
  latitud             DOUBLE       NOT NULL,
  longitud            DOUBLE       NOT NULL,
  tipo_evento         VARCHAR(100),
  fuente_informacion  VARCHAR(100),
  confiabilidad       INT,
  quien_reporta_id    BIGINT,
  fecha_evento        DATETIME,
  detalles            LONGTEXT,
  comprobado          TINYINT(1)   DEFAULT 0,
  created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (reporte_id) REFERENCES reporte(id)
);

-- Índices de geolocalización
CREATE INDEX idx_ubicacion_reporte ON ubicacion(reporte_id);
CREATE INDEX idx_ubicacion_coords  ON ubicacion(latitud, longitud);
CREATE INDEX idx_ubicacion_comuna  ON ubicacion(comuna);
CREATE INDEX idx_zona_coords       ON zona_incidencia(latitud_centro, longitud_centro);
CREATE INDEX idx_zona_riesgo       ON zona_incidencia(nivel_riesgo);
CREATE INDEX idx_historial_reporte ON historial_ubicacion(reporte_id);

-- ============================================================================
-- MS COINCIDENCIAS (puerto 8082)
-- ============================================================================

-- Tabla: coincidencia
CREATE TABLE IF NOT EXISTS coincidencia (
  id                     BIGINT        NOT NULL AUTO_INCREMENT,
  reporte_perdido_id     BIGINT        NOT NULL,
  reporte_encontrado_id  BIGINT        NOT NULL,
  puntaje_total          DECIMAL(5,2),
  detalles_puntaje       JSON,
  estado                 VARCHAR(50)   NOT NULL DEFAULT 'PENDIENTE_REVISION',
  fecha_analisis         DATETIME,
  fecha_confirmacion     DATETIME,
  usuario_quien_confirmo BIGINT,
  motivo_rechazo         VARCHAR(255),
  version                INT           DEFAULT 0,
  created_at             TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at             TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (reporte_perdido_id)    REFERENCES reporte(id),
  FOREIGN KEY (reporte_encontrado_id) REFERENCES reporte(id)
);

-- Índices de coincidencias
CREATE INDEX idx_coincidencia_reportes ON coincidencia(reporte_perdido_id, reporte_encontrado_id);
CREATE INDEX idx_coincidencia_estado   ON coincidencia(estado);
CREATE INDEX idx_coincidencia_puntaje  ON coincidencia(puntaje_total);

-- ============================================================================
-- MS PROYECTOS (puerto 8086)
-- ============================================================================

-- Tabla: proyectos
CREATE TABLE IF NOT EXISTS proyectos (
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  nombre         VARCHAR(255) NOT NULL,
  descripcion    LONGTEXT,
  estado         VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
  fecha_inicio   DATE         NOT NULL,
  fecha_fin      DATE,
  presupuesto    INT          NOT NULL,
  responsable_id BIGINT       NOT NULL,
  created_at     DATETIME     NOT NULL,
  updated_at     DATETIME,
  PRIMARY KEY (id)
);

-- Tabla: tareas
CREATE TABLE IF NOT EXISTS tareas (
  id               BIGINT       NOT NULL AUTO_INCREMENT,
  titulo           VARCHAR(255) NOT NULL,
  descripcion      LONGTEXT,
  proyecto_id      BIGINT       NOT NULL,
  asignado_id      BIGINT       NOT NULL,
  estado           VARCHAR(50)  NOT NULL DEFAULT 'PENDIENTE',
  prioridad        INT          NOT NULL DEFAULT 2,
  fecha_vencimiento DATE,
  created_at       DATETIME     NOT NULL,
  updated_at       DATETIME,
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyectos(id)
);

-- Índices de proyectos
CREATE INDEX idx_proyectos_estado       ON proyectos(estado);
CREATE INDEX idx_proyectos_responsable  ON proyectos(responsable_id);
CREATE INDEX idx_tareas_proyecto        ON tareas(proyecto_id);
CREATE INDEX idx_tareas_asignado        ON tareas(asignado_id);
CREATE INDEX idx_tareas_estado          ON tareas(estado);

-- ============================================================================
-- MS RECURSOS HUMANOS (puerto 8087)
-- ============================================================================

-- Tabla: departamentos
CREATE TABLE IF NOT EXISTS departamentos (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  nombre      VARCHAR(255) NOT NULL UNIQUE,
  descripcion LONGTEXT,
  gerent_id   BIGINT       NOT NULL,
  estado      VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
  created_at  DATETIME     NOT NULL,
  updated_at  DATETIME,
  PRIMARY KEY (id)
);

-- Tabla: empleados
CREATE TABLE IF NOT EXISTS empleados (
  id                  BIGINT        NOT NULL AUTO_INCREMENT,
  nombre              VARCHAR(255)  NOT NULL,
  apellido            VARCHAR(255)  NOT NULL,
  email               VARCHAR(255)  NOT NULL UNIQUE,
  cedula              VARCHAR(50)   NOT NULL,
  departamento_id     BIGINT        NOT NULL,
  cargo               VARCHAR(255)  NOT NULL,
  salario             DOUBLE        NOT NULL,
  fecha_contratacion  DATE          NOT NULL,
  estado              VARCHAR(50)   NOT NULL DEFAULT 'ACTIVO',
  created_at          DATETIME      NOT NULL,
  updated_at          DATETIME,
  PRIMARY KEY (id),
  FOREIGN KEY (departamento_id) REFERENCES departamentos(id)
);

-- Tabla: permisos
CREATE TABLE IF NOT EXISTS permisos (
  id           BIGINT      NOT NULL AUTO_INCREMENT,
  empleado_id  BIGINT      NOT NULL,
  tipo         VARCHAR(50) NOT NULL,
  fecha_inicio DATE        NOT NULL,
  fecha_fin    DATE        NOT NULL,
  descripcion  LONGTEXT,
  estado       VARCHAR(50) NOT NULL DEFAULT 'SOLICITADO',
  created_at   DATETIME    NOT NULL,
  updated_at   DATETIME,
  PRIMARY KEY (id),
  FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

-- Índices de recursos humanos
CREATE INDEX idx_empleados_departamento ON empleados(departamento_id);
CREATE INDEX idx_empleados_estado       ON empleados(estado);
CREATE INDEX idx_permisos_empleado      ON permisos(empleado_id);
CREATE INDEX idx_permisos_estado        ON permisos(estado);

-- ============================================================================
-- 3. BASE DE DATOS: sanosysalvos_notificaciones
--    Microservicio: ms-notificaciones (puerto 8085)
-- ============================================================================
CREATE DATABASE IF NOT EXISTS sanosysalvos_notificaciones
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sanosysalvos_notificaciones;

-- Tabla: notificacion
CREATE TABLE IF NOT EXISTS notificacion (
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  usuario_id     BIGINT       NOT NULL,
  tipo           VARCHAR(50)  NOT NULL,
  asunto         VARCHAR(255),
  contenido      LONGTEXT,
  destinatario   VARCHAR(100),
  estado         VARCHAR(50)  NOT NULL DEFAULT 'PENDIENTE',
  evento_tipo    VARCHAR(100),
  evento_id      BIGINT,
  intentos_envio INT          DEFAULT 0,
  mensaje_error  LONGTEXT,
  created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

-- Índices de notificaciones
CREATE INDEX idx_notificacion_usuario ON notificacion(usuario_id);
CREATE INDEX idx_notificacion_tipo    ON notificacion(tipo);
CREATE INDEX idx_notificacion_estado  ON notificacion(estado);
CREATE INDEX idx_notificacion_evento  ON notificacion(evento_tipo, evento_id);

-- ============================================================================
SET FOREIGN_KEY_CHECKS = 1;
-- ============================================================================
-- FIN DEL SCRIPT
-- Bases de datos creadas:
--   sanosysalvos_usuarios   → ms-usuarios  (puerto 8084)
--   sanosysalvos            → ms-reportes  (8083), ms-geolocalizacion (8081),
--                             ms-coincidencias (8082), ms-proyectos (8086),
--                             ms-recursos-humanos (8087)
--   sanosysalvos_notificaciones → ms-notificaciones (8085)
-- ============================================================================
