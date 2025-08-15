# Usa la imagen oficial de MySQL como base
FROM mysql:latest

# Copia tu script SQL al directorio de inicialización del contenedor
COPY init.sql /docker-entrypoint-initdb.d/

# Define variables de entorno (opcional, también se pueden pasar con docker run)
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=solicitudes

# Expone el puerto por defecto de MySQL
EXPOSE 3306