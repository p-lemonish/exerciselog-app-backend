#!/bin/sh
export SPRING_DATASOURCE_PASSWORD=$(cat /run/secrets/db_password)
export SECRET_KEY=$(cat /run/secrets/secret_key)
export ENCRYPTION_SECRET=$(cat /run/secrets/encryption_secret)
export SPRING_DATASOURCE_USERNAME=exerciselog_user
export SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/exerciselog

exec java -jar app.jar

