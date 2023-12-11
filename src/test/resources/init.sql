CREATE TABLE categoria (
  id binary(16) NOT NULL,
  nombre varchar(200) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE info_app_transaccion (
  id binary(16) NOT NULL,
  cod_usuario blob NOT NULL,
  medio varchar(100) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE info_bancaria (
  id binary(16) NOT NULL,
  cod_interbancario blob NOT NULL,
  num_tarjeta blob NOT NULL,
  banco varchar(200) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE modalidad_pago (
  id binary(16) NOT NULL,
  nombre varchar(100) NOT NULL,
  descripcion varchar(200) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE plan (
  id binary(16) NOT NULL,
  tipo varchar(100) NOT NULL,
  is_free tinyint(1) NOT NULL,
  codigo varchar(100) NOT NULL,
  nombre varchar(100) NOT NULL,
  monto_basico decimal(6,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE usuario (
  id binary(16) NOT NULL,
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
  PRIMARY KEY (id)
);

CREATE TABLE info_financiera (
  id binary(16) NOT NULL,
  clave blob NOT NULL,
  id_usuario binary(16) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario) REFERENCES usuario (id)
);

CREATE TABLE plan_usuario (
  id_plan binary(16) NOT NULL,
  id_usuario binary(16) NOT NULL,
  is_active tinyint(1) NOT NULL,
  monto decimal(6,2) NOT NULL,
  moneda varchar(100) NOT NULL,
  PRIMARY KEY (id_plan, id_usuario),
  FOREIGN KEY (id_plan) REFERENCES plan (id),
  FOREIGN KEY (id_usuario) REFERENCES usuario (id)
);

CREATE TABLE skill (
  id binary(16) NOT NULL,
  nombre varchar(200) NOT NULL,
  id_categoria binary(16) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);

CREATE TABLE skill_usuario (
  id_skill binary(16) NOT NULL,
  id_usuario binary(16) NOT NULL,
  nivel_conocimiento int(11) NOT NULL,
  PRIMARY KEY (id_skill,id_usuario),
  FOREIGN KEY (id_skill) REFERENCES skill(id),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

CREATE TABLE servicio (
  id binary(16) NOT NULL,
  descripcion varchar(500) NOT NULL,
  precio decimal(20,2) NOT NULL,
  id_usuario binary(16) NOT NULL,
  id_modalidad_pago binary(16) NOT NULL,
  id_skill binary(16) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_usuario) REFERENCES usuario (id),
  FOREIGN KEY (id_modalidad_pago) REFERENCES modalidad_pago (id),
  FOREIGN KEY (id_skill) REFERENCES skill (id)
);

CREATE TABLE match_servicio (
  id binary(16) NOT NULL,
  fecha datetime NOT NULL,
  fecha_inicio datetime NOT NULL,
  fecha_cierre datetime DEFAULT NULL,
  estado int(11) NOT NULL,
  puntuacion int(11) NOT NULL,
  costo decimal(20,2) NOT NULL,
  id_servicio binary(16) NOT NULL,
  id_cliente binary(16) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_servicio) REFERENCES servicio (id),
  FOREIGN KEY (id_cliente) REFERENCES usuario (id)
);

CREATE TABLE recurso_multimedia_servicio (
  id binary(16) NOT NULL,
  url varchar(1000) NOT NULL,
  medio varchar(100) NOT NULL,
  id_servicio binary(16) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_servicio) REFERENCES servicio (id)
);

