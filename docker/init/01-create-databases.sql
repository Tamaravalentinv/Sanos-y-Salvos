-- Create all databases for Sanos y Salvos microservices
CREATE DATABASE IF NOT EXISTS sanosysalvos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS sanosysalvos_usuarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS sanosysalvos_notificaciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Grant permissions to sanosuser on all databases
GRANT ALL PRIVILEGES ON sanosysalvos.* TO 'sanosuser'@'%';
GRANT ALL PRIVILEGES ON sanosysalvos_usuarios.* TO 'sanosuser'@'%';
GRANT ALL PRIVILEGES ON sanosysalvos_notificaciones.* TO 'sanosuser'@'%';
FLUSH PRIVILEGES;
