#!/bin/sh
HOST="postgres"
PORT="5432"
TIMEOUT=15
# Wait for it miniscript to wait for postgres to be up
if ! command -v nc > /dev/null 2>&1; then
	echo "nc not installed, can't check if postgres is up.. sleeping ${TIMEOUT} just in case and yolo"
	sleep $TIMEOUT
else
	START_TIME=$(date +%s)
	while ! nc -z "$HOST" "$PORT" >/dev/null 2>&1; do
		CURRENT_TIME=$(date +%s)
		ELAPSE_TIME=$((CURRENT_TIME - START_TIME))
		if [ $ELAPSED_TIME -ge $TIMEOUT ]; then
			echo "Waiting timed out after ${TIMEOUT} seconds."
			exit 1
		fi
		sleep 1
	done
	echo "${HOST}:${PORT} is available"
fi

# Setup backend
export SPRING_DATASOURCE_PASSWORD=$(cat /run/secrets/db_password)
export SECRET_KEY=$(cat /run/secrets/secret_key)
export ENCRYPTION_SECRET=$(cat /run/secrets/encryption_secret)
export SPRING_DATASOURCE_USERNAME=exerciselog_user
export SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/exerciselog

exec java -jar app.jar
