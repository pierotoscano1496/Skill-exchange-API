#!/usr/bin/env bash
set -euo pipefail

# === Ajustes según tu docker-compose.yml ===
PROJECT_NAME="skillexchange"          # <── forzamos el nombre del proyecto
COMPOSE_FILE_REL="docker-compose.yml" # ruta del compose desde la raíz
COMPOSE_SERVICE="mysql-db"            # service en docker-compose.yml
CONTAINER_NAME="mysql_skillexchange"  # container_name del servicio
VOLUME_LOGICAL_NAME="mysql_data"      # nombre lógico del volumen
# ==========================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
BACKUP_DIR="$SCRIPT_DIR/backup_mysql"
COMPOSE_FILE="$PROJECT_ROOT/$COMPOSE_FILE_REL"
REAL_VOLUME="${PROJECT_NAME}_${VOLUME_LOGICAL_NAME}"

# Usaremos siempre -p skillexchange para que todo apunte al mismo project
COMPOSE_ARGS=(-p "$PROJECT_NAME" -f "$COMPOSE_FILE")

# Pre-flight
command -v docker >/dev/null || { echo "ERROR: Docker no está disponible"; exit 1; }
[ -f "$COMPOSE_FILE" ] || { echo "ERROR: No se encontró $COMPOSE_FILE"; exit 1; }
[ -d "$BACKUP_DIR" ] || { echo "ERROR: Falta carpeta de backups: $BACKUP_DIR"; exit 1; }

IMAGE_TAR="$(ls -1t "${BACKUP_DIR}"/image_*.tar 2>/dev/null | head -n1 || true)"
DATA_TGZ="$(ls -1t "${BACKUP_DIR}/${VOLUME_LOGICAL_NAME}"_*.tgz 2>/dev/null | head -n1 || true)"
if [[ -z "$IMAGE_TAR" || -z "$DATA_TGZ" ]]; then
  echo "ERROR: Requiero: image_*.tar y ${VOLUME_LOGICAL_NAME}_*.tgz en $BACKUP_DIR"; exit 1;
fi

echo "[i] Proyecto forzado: $PROJECT_NAME"
echo "[i] Volumen objetivo: $REAL_VOLUME"
echo "[i] Imagen TAR: $(basename "$IMAGE_TAR")"
echo "[i] Datos TGZ : $(basename "$DATA_TGZ")"

# 1) Detener y limpiar contenedor/servicio
docker compose "${COMPOSE_ARGS[@]}" stop backend || true
docker compose "${COMPOSE_ARGS[@]}" stop "$COMPOSE_SERVICE" || true
docker compose "${COMPOSE_ARGS[@]}" rm -f "$COMPOSE_SERVICE" || true
docker rm -f "$CONTAINER_NAME" 2>/dev/null || true

# 2) Borrar volumen del proyecto (si existiera)
docker volume rm "$REAL_VOLUME" 2>/dev/null || true

# 3) Cargar imagen desde .tar
LOADED_OUTPUT="$(docker load -i "$IMAGE_TAR")"
echo "$LOADED_OUTPUT"
HELPER_IMAGE="$(echo "$LOADED_OUTPUT" | awk '/Loaded image:/ {print $3}' | tail -n1)"
if [[ -z "$HELPER_IMAGE" ]]; then
  HELPER_IMAGE="$(docker images --format '{{.Repository}}:{{.Tag}}' | grep -E '^mysql:' | head -n1 || true)"
fi
[ -n "$HELPER_IMAGE" ] || { echo "ERROR: No pude determinar una imagen MySQL para restaurar"; exit 1; }

# 4) Crear volumen exacto y restaurar datos
echo "[i] Creando volumen: $REAL_VOLUME"
docker volume create "$REAL_VOLUME" >/dev/null

echo "[i] Restaurando datos al volumen $REAL_VOLUME…"
docker run --rm \
  -v "${REAL_VOLUME}:/to" \
  -v "$DATA_TGZ:/from/data.tgz:ro" \
  "$HELPER_IMAGE" sh -lc "cd /to && tar -xzf /from/data.tgz"

# 5) Levantar servicio con -p skillexchange (para que use ese volumen)
echo "[i] Levantando servicio…"
docker compose "${COMPOSE_ARGS[@]}" up -d "$COMPOSE_SERVICE"

echo "[ok] Restore limpio completo. Verifica:"
docker compose "${COMPOSE_ARGS[@]}" logs -n 80 "$COMPOSE_SERVICE"
echo "[i] Montaje actual:"
docker inspect "$CONTAINER_NAME" --format '{{range .Mounts}}{{.Name}} -> {{.Destination}}{{end}}'
