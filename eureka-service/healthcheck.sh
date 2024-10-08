#!/bin/bash
set -e

host="$(hostname -i || echo '127.0.0.1')"

until curl -f "http://${host}:8761/actuator/health"; do
  echo 'Eureka Server is unavailable - sleeping'
  sleep 1
done

echo 'Eureka Server is up - executing command'
exec "$@"