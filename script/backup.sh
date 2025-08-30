#!/usr/bin/env bash
set -euo pipefail

# === Ajustes para tu compose ===
COMPOSE_SERVICE="mysql-db"            # service en docker-compose.yml
CONTAINER_NAME="mysql_skillexchange"  # container_name en docker-compose.yml
VOLUME_LOGICAL_NAME="mysql_data"      # volumen lógico en docker-compose.yml
# =================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
BACKUP_DIR="$SCRIPT_DIR/backup_mysql"
mkdir -p "$BACKUP_DIR"

PROJECT_NAME="${COMPOSE_PROJECT_NAME:-$(basename "$PROJECT_ROOT")}"

# 1) Resolver contenedor
CONTAINER_ID="$(docker ps -q --filter "name=^/${CONTAINER_NAME}$" | head -n1 || true)"
if [[ -z "$CONTAINER_ID" ]]; then
  CONTAINER_ID="$(docker ps -q \
    --filter "label=com.docker.compose.project=$PROJECT_NAME" \
    --filter "label=com.docker.compose.service=$COMPOSE_SERVICE" | head -n1 || true)"
fi
if [[ -z "$CONTAINER_ID" ]]; then
  echo "ERROR: No se encontró el contenedor ($CONTAINER_NAME / service $COMPOSE_SERVICE). ¿Está levantado?" >&2
  exit 1
fi

IMAGE="$(docker inspect -f '{{.Config.Image}}' "$CONTAINER_ID")"
TS="$(date +%Y%m%d_%H%M%S)"

echo "[i] Proyecto: $PROJECT_NAME"
echo "[i] Contenedor: $CONTAINER_ID  Imagen: $IMAGE"

# 2) Detectar mount de /var/lib/mysql
MOUNT_INFO="$(docker inspect -f '{{range .Mounts}}{{if eq .Destination "/var/lib/mysql"}}{{.Type}}|{{.Name}}|{{.Source}}{{end}}{{end}}' "$CONTAINER_ID" || true)"
MOUNT_TYPE="$(echo "$MOUNT_INFO" | cut -d'|' -f1)"
VOL_NAME="$(echo "$MOUNT_INFO" | cut -d'|' -f2)"
BIND_PATH="$(echo "$MOUNT_INFO" | cut -d'|' -f3)"

echo "[i] Parando contenedor para snapshot consistente…"
docker stop "$CONTAINER_ID"

# 3) Respaldar datos SIN descargar imágenes (usamos la imagen de MySQL ya local)
if [[ "$MOUNT_TYPE" == "volume" ]]; then
  REAL_VOLUME="${VOL_NAME:-${PROJECT_NAME}_${VOLUME_LOGICAL_NAME}}"
  echo "[i] Volumen detectado: $REAL_VOLUME"
  docker run --rm \
    -v "${REAL_VOLUME}:/from:ro" \
    -v "${BACKUP_DIR}:/to" \
    "$IMAGE" sh -lc "cd /from && tar -czf /to/${VOLUME_LOGICAL_NAME}_${TS}.tgz ."
elif [[ "$MOUNT_TYPE" == "bind" ]]; then
  echo "[i] Bind mount detectado: $BIND_PATH"
  tar -czf "${BACKUP_DIR}/${VOLUME_LOGICAL_NAME}_${TS}.tgz" -C "$BIND_PATH" .
else
  echo "ERROR: No se pudo detectar el mount de /var/lib/mysql." >&2
  docker start "$CONTAINER_ID" || true
  exit 1
fi

# 4) Respaldar imagen (para restaurar igualito en destino)
echo "[i] Guardando imagen: $IMAGE"
docker save -o "${BACKUP_DIR}/image_${TS}.tar" "$IMAGE"

echo "[i] Arrancando de nuevo el contenedor…"
docker start "$CONTAINER_ID"

echo "[ok] Archivos generados en $BACKUP_DIR:"
ls -lh "$BACKUP_DIR"
