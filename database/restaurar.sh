#!/bin/bash
# Restaura un respaldo completo (estructura + datos) en una base nueva.
# Uso: ./restaurar.sh asesorias
set -e

DB="${1:-asesorias}"

createdb -U postgres "$DB" 2>/dev/null || echo "La base '$DB' ya existe, se usa tal cual."
psql -U postgres -d "$DB" -f backup.sql

echo "Listo: '$DB' restaurada desde backup.sql."