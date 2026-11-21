#!/bin/bash
# Despliega la última versión de main en el VPS: trae el código, recompila y reinicia.
# Uso: ./desplegar.sh (correrlo desde /opt/asesorias)
set -e

git config --global --add safe.directory "$(pwd)" 2>/dev/null || true

echo "==> Actualizando el código..."
git pull

echo "==> Compilando el backend..."
cd backend
./mvnw clean package -DskipTests

echo "==> Reiniciando el servicio..."
systemctl restart asesorias

echo "==> Esperando a que arranque..."
sleep 50

systemctl status asesorias --no-pager
echo
echo "==> Probando que responda..."
curl -s -o /dev/null -w "Código de /api/auth/me: %{http_code}\n" http://127.0.0.1:8080/api/auth/me