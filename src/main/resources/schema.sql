CREATE TABLE usuario (
  dni varchar(100) NOT NULL,
  carnet_extranjeria varchar(100) NOT NULL,
  tipo_documento varchar(100) NOT NULL,
  nombres varchar(200) NOT NULL,
  apellidos varchar(200) NOT NULL,
  fecha_nacimiento date NOT NULL,
  perfil_linkedin varchar(500) DEFAULT NULL,
  perfil_facebook varchar(500) DEFAULT NULL,
  perfil_instagram varchar(500) DEFAULT NULL,
  perfil_tiktok varchar(500) DEFAULT NULL,
  clave varchar(60) NOT NULL,
  correo varchar(200) NOT NULL,
  id binary(16) NOT NULL,
  PRIMARY KEY (id)
);

