CREATE TABLE CATEGORIA (
  NOMBRE VARCHAR(200) NOT NULL,
  ID BINARY(16) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE INFO_APP_TRANSACCION (
  COD_USUARIO BLOB NOT NULL,
  MEDIO VARCHAR(100) NOT NULL,
  ID BINARY(16) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE INFO_BANCARIA (
  COD_INTERBANCARIO BLOB NOT NULL,
  NUM_TARJETA BLOB NOT NULL,
  BANCO VARCHAR(200) NOT NULL,
  ID BINARY(16) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE MODALIDAD_PAGO (
  NOMBRE VARCHAR(100) NOT NULL,
  DESCRIPCION VARCHAR(200) NOT NULL,
  ID BINARY(16) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE PLAN (
  TIPO VARCHAR(100) NOT NULL,
  IS_FREE TINYINT(1) NOT NULL,
  CODIGO VARCHAR(100) NOT NULL,
  NOMBRE VARCHAR(100) NOT NULL,
  MONTO_BASICO DECIMAL(6,2) NOT NULL,
  ID BINARY(16) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE SKILL_USUARIO (
  NIVEL_CONOCIMIENTO INT(11) NOT NULL,
  ID_USUARIO BINARY(16) NOT NULL,
  ID_SKILL BINARY(16) NOT NULL,
  PRIMARY KEY (ID_SKILL,ID_USUARIO)
);

CREATE TABLE USUARIO (
  DNI VARCHAR(100),
  CARNET_EXTRANJERIA VARCHAR(100),
  TIPO_DOCUMENTO VARCHAR(100) NOT NULL,
  NOMBRES VARCHAR(200) NOT NULL,
  APELLIDOS VARCHAR(200) NOT NULL,
  FECHA_NACIMIENTO DATE NOT NULL,
  PERFIL_LINKEDIN VARCHAR(500) DEFAULT NULL,
  PERFIL_FACEBOOK VARCHAR(500) DEFAULT NULL,
  PERFIL_INSTAGRAM VARCHAR(500) DEFAULT NULL,
  PERFIL_TIKTOK VARCHAR(500) DEFAULT NULL,
  CLAVE VARCHAR(60) NOT NULL,
  CORREO VARCHAR(200) NOT NULL,
  ID BINARY(16) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE INFO_FINANCIERA (
  CLAVE BLOB NOT NULL,
  ID BINARY(16) NOT NULL,
  ID_USUARIO BINARY(16) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID)
);

CREATE TABLE PLAN_USUARIO (
  IS_ACTIVE TINYINT(1) NOT NULL,
  MONTO DECIMAL(6,2) NOT NULL,
  MONEDA VARCHAR(100) NOT NULL,
  ID_PLAN BINARY(16) NOT NULL,
  ID_USUARIO BINARY(16) NOT NULL,
  PRIMARY KEY (ID_PLAN,ID_USUARIO),
  FOREIGN KEY (ID_PLAN) REFERENCES PLAN (ID),
  FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID)
);

CREATE TABLE SKILL (
  NOMBRE VARCHAR(200) NOT NULL,
  ID BINARY(16) NOT NULL,
  ID_CATEGORIA BINARY(16) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_CATEGORIA) REFERENCES CATEGORIA (ID)
);

CREATE TABLE SERVICIO (
  DESCRIPCION VARCHAR(500) NOT NULL,
  PRECIO DECIMAL(20,2) NOT NULL,
  ID BINARY(16) NOT NULL,
  ID_USUARIO BINARY(16) NOT NULL,
  ID_SKILL BINARY(16) NOT NULL,
  ID_MODALIDAD_PAGO BINARY(16) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID),
  FOREIGN KEY (ID_MODALIDAD_PAGO) REFERENCES MODALIDAD_PAGO (ID),
  FOREIGN KEY (ID_SKILL) REFERENCES SKILL (ID)
);

CREATE TABLE MATCH_SERVICIO (
  FECHA DATETIME NOT NULL,
  FECHA_INICIO DATETIME NOT NULL,
  FECHA_CIERRE DATETIME DEFAULT NULL,
  ESTADO INT(11) NOT NULL,
  PUNTUACION INT(11) NOT NULL,
  COSTO DECIMAL(20,2) NOT NULL,
  ID BINARY(16) NOT NULL,
  ID_SERVICIO BINARY(16) NOT NULL,
  ID_CLIENTE BINARY(16) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_SERVICIO) REFERENCES SERVICIO (ID),
  FOREIGN KEY (ID_CLIENTE) REFERENCES USUARIO (ID)
);

CREATE TABLE RECURSO_MULTIMEDIA_SERVICIO (
  URL VARCHAR(1000) NOT NULL,
  MEDIO VARCHAR(100) NOT NULL,
  ID BINARY(16) NOT NULL,
  ID_SERVICIO BINARY(16) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (ID_SERVICIO) REFERENCES SERVICIO (ID)
);

-- STORED PROCEDURES

DROP PROCEDURE IF EXISTS get_usercred_by_correo;

CREATE PROCEDURE get_usercred_by_correo(
	IN p_correo VARCHAR(200)
)
BEGIN
	SELECT CORREO, CLAVE, NOMBRES, APELLIDOS FROM USUARIO WHERE CORREO = p_correo;
END;

DROP PROCEDURE IF EXISTS login;

CREATE PROCEDURE login(
	IN p_correo VARCHAR(200),
	IN p_clave VARCHAR(256)
)
BEGIN
	SELECT * FROM USUARIO WHERE CLAVE = p_clave AND CORREO = p_correo;
END;

DROP PROCEDURE IF EXISTS obtener_planes;

CREATE PROCEDURE obtener_planes()
BEGIN
	SELECT * FROM PLAN;
END;

DROP PROCEDURE IF EXISTS obtener_plan_by_codigo;

CREATE PROCEDURE obtener_plan_by_codigo(
    IN p_codigo VARCHAR(100)
)
BEGIN
	SELECT * FROM PLAN WHERE CODIGO = p_codigo;
END;

DROP PROCEDURE IF EXISTS obtener_skills_from_usuario;

CREATE PROCEDURE obtener_skills_from_usuario(
    IN p_id_usuario INT
)
BEGIN
	SELECT S.*,
	C.NOMBRE AS NOMBRE_CATEGORIA,
	SU.NIVEL_CONOCIMIENTO
	FROM SKILL_USUARIO SU
	INNER JOIN SKILL S ON SU.ID_SKILL = S.ID
	INNER JOIN CATEGORIA C ON C.ID = S.ID_CATEGORIA
	WHERE SU.ID_USUARIO = p_id_usuario;
END;

DROP PROCEDURE IF EXISTS registrar_plan_usuario;

CREATE PROCEDURE registrar_plan_usuario(
	IN p_id_plan INT,
	IN p_id_usuario INT,
	IN p_is_active BOOLEAN,
	IN p_monto DECIMAL(6,2),
	IN p_moneda VARCHAR(100)
)
BEGIN
	INSERT INTO PLAN_USUARIO (ID_PLAN, ID_USUARIO, IS_ACTIVE, MONTO, MONEDA)
	VALUES (p_id_plan, p_id_usuario, p_is_active, p_monto, p_moneda);

	SELECT * FROM PLAN_USUARIO WHERE ID_PLAN = p_id_plan AND ID_USUARIO = p_id_usuario;
END;

DROP PROCEDURE IF EXISTS registrar_skill;

CREATE PROCEDURE registrar_skill(
    IN p_nombre VARCHAR(200),
    IN p_id_categoria INT
)
BEGIN
	INSERT INTO SKILL(NOMBRE, ID_CATEGORIA) VALUES(p_nombre, p_id_categoria);

	SELECT * FROM SKILL WHERE ID = LAST_INSERT_ID();
END;

DROP PROCEDURE IF EXISTS registrar_skill_usuario;

CREATE PROCEDURE registrar_skill_usuario(
    IN p_id_skill INT,
    IN p_id_usuario INT,
    IN p_nivel_conocimiento INT
)
BEGIN
	INSERT INTO SKILL_USUARIO(ID_SKILL, ID_USUARIO, NIVEL_CONOCIMIENTO) VALUES(p_id_skill, p_id_usuario, p_nivel_conocimiento);

	SELECT * FROM SKILL_USUARIO WHERE ID_SKILL = p_id_skill AND ID_USUARIO = p_id_usuario;
END;

DROP PROCEDURE IF EXISTS registrar_usuario;

CREATE PROCEDURE registrar_usuario(
    IN p_id BINARY(16),
	IN p_dni VARCHAR(100),
	IN p_carnet_extranjeria VARCHAR(100),
	IN p_tipo_documento VARCHAR(100),
    IN p_correo VARCHAR(200),
	IN p_nombres VARCHAR(200),
	IN p_apellidos VARCHAR(200),
	IN p_fecha_nacimiento DATE,
	IN p_perfil_linkedin VARCHAR(500),
	IN p_perfil_facebook VARCHAR(500),
	IN p_perfil_instagram VARCHAR(500),
	IN p_perfil_tiktok VARCHAR(500),
	IN p_clave VARCHAR(500),
	OUT status_message VARCHAR(500)
)
BEGIN
    DECLARE documento_existente INT;

    IF (p_tipo_documento IS NOT NULL) THEN

        IF (p_tipo_documento = 'dni') THEN
            SELECT COUNT(*) INTO documento_existente FROM USUARIO WHERE DNI = p_dni AND TIPO_DOCUMENTO = 'dni';
        ELSE -- carnet_extranjeria
            SELECT COUNT(*) INTO documento_existente FROM USUARIO WHERE CARNET_EXTRANJERIA = p_carnet_extranjeria AND TIPO_DOCUMENTO = 'carnet_extranjeria';
        END IF;

        IF (documento_existente = 0) THEN
            INSERT INTO USUARIO (ID, DNI, CARNET_EXTRANJERIA, TIPO_DOCUMENTO, CORREO, NOMBRES, APELLIDOS, FECHA_NACIMIENTO, PERFIL_LINKEDIN, PERFIL_FACEBOOK, PERFIL_INSTAGRAM, PERFIL_TIKTOK, CLAVE)
            VALUES (p_id, p_dni, p_carnet_extranjeria, p_tipo_documento, p_correo, p_nombres, p_apellidos, p_fecha_nacimiento, p_perfil_linkedin, p_perfil_facebook, p_perfil_instagram, p_perfil_tiktok, p_clave);

            SELECT * FROM USUARIO WHERE ID = p_id;
            SET STATUS_MESSAGE = 'Usuario registrado';
        ELSE
            SET STATUS_MESSAGE = 'Usuario ya existe';
        END IF;
    ELSE
        SET STATUS_MESSAGE = 'No hay tipo de documento';
    END IF;
END;

DROP PROCEDURE IF EXISTS ver_cosas;

CREATE PROCEDURE ver_cosas()
BEGIN
	SELECT * FROM HEROKU_CD849EC8E70361A.USUARIO;
END;

DROP PROCEDURE IF EXISTS whatever;

CREATE PROCEDURE whatever(
    IN ID INT
)
BEGIN
	SELECT * FROM USUARIO WHERE ID=ID;
END;