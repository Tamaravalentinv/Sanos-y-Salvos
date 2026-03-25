-- Create databases for each microservice
CREATE DATABASE IF NOT EXISTS sanosysalvos_usuarios;
CREATE DATABASE IF NOT EXISTS sanosysalvos;
CREATE DATABASE IF NOT EXISTS sanosysalvos_notificaciones;

-- Grant privileges
GRANT ALL PRIVILEGES ON sanosysalvos_usuarios.* TO 'sanosuser'@'%';
GRANT ALL PRIVILEGES ON sanosysalvos.* TO 'sanosuser'@'%';
GRANT ALL PRIVILEGES ON sanosysalvos_notificaciones.* TO 'sanosuser'@'%';

FLUSH PRIVILEGES;
