package com.main.skillexchangeapi.repositories;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants.Tipo;
import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;
import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.*;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MatchRepositoryTester extends MySQLContainer<MatchRepositoryTester> {
        @Autowired
        private DatabaseConnection databaseConnection;

        @Autowired
        private IMatchServicioRepository repository;

        @Autowired
        private IUsuarioRepository usuarioRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private MockMvc mockMvc;

        private HashMap<UUID, Categoria> categoriasCollection;
        private HashMap<UUID, ModalidadPago> modalidadesPagoCollection;
        private HashMap<UUID, Plan> planesCollection;
        private HashMap<UUID, Skill> skillsCollection;
        private HashMap<UUID, Usuario> usuariosCollection;

        /**
         * Preparar entorno previo a la creación de matchs
         */
        @BeforeEach
        void setup() {
                /**
                 * --- CATEGORÍAS, SUB CATEGORÍAS Y HABILIDADES
                 * Diccionarios:
                 * Categorías: música, educación, tecnología
                 * Subcategorías:
                 * - Música: instrumentos musicales, canto, DJ
                 * - Educación: inicial, primaria, secundaria, preuniversitario
                 * - Tecnología: entornos de desarrollo, servicios en nube, lenguajes de
                 * programación, Frameworks de desarrollo, bases de datos
                 * Habilidades:
                 * - Instrumentos musicales: guitarra, piano, violín
                 * - Canto: ópera, rap, canto urbano
                 * - DJ: mezclas, remixes
                 * - Inicial: lenguaje, matemática, ciencias naturales
                 * - Primaria: lenguaje, comprensión de lectura, biología, historia, geografía,
                 * geometría, aritmética
                 * - Secundaria: lenguaje, literatura, comprensión de lectura, biología,
                 * química, física, historia, geografía, geometría, aritmética, trigonometría,
                 * álgebra
                 * - Preuniversitario: lenguaje, habilidad verbal, química, física, historia,
                 * geografía, geometría, aritmética, trigonometría, álgebra
                 * - Entornos de desarrollo: frontend, backend, desarrollo móvil, fullstack, SAP
                 * - Servicios en nube: AWS, Azure, Google Cloud
                 * - Lenguajes de programación: Java, Python, C++, C#, Go, Javascript,
                 * Typescript, Ruby, PHP
                 * - Frameworks de desarrollo: Spring, Django, Flask, .NET, .NET Core, Angular,
                 * React, Vue, Next.js, Ruby on rails, Laravel
                 * - Bases de datos: SQL Server, Oracle, MySQL, PostgreSQL, MariaDB, SQLite,
                 * procedimientos almacenados, funciones
                 * Modalidades de pago:
                 * - Yape
                 * - Tarjeta de crédito
                 */

                /**
                 * CATEGORÍAS
                 */
                Categoria musica = Categoria.builder()
                                .nombre("Música")
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                                .build();
                Categoria educacion = Categoria.builder()
                                .nombre("Educación")
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                                .build();
                Categoria tecnologia = Categoria.builder()
                                .nombre("Tecnología")
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                                .build();

                /**
                 * SUBCATEGORÍAS
                 */
                // Música
                SubCategoria instrumentosMusicales = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                                .nombre("Instrumentos musicales")
                                .categoria(musica)
                                .build();

                SubCategoria canto = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                                .nombre("Canto")
                                .categoria(musica)
                                .build();

                SubCategoria dj = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                                .nombre("DJ")
                                .categoria(musica)
                                .build();

                // Educación
                SubCategoria inicial = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
                                .nombre("Inicial")
                                .categoria(educacion)
                                .build();

                SubCategoria primaria = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440004"))
                                .nombre("Primaria")
                                .categoria(educacion)
                                .build();

                SubCategoria secundaria = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"))
                                .nombre("Secundaria")
                                .categoria(educacion)
                                .build();

                SubCategoria preuniversitario = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440006"))
                                .nombre("Preuniversitario")
                                .categoria(educacion)
                                .build();

                // Tecnología
                SubCategoria entornosDesarrollo = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440007"))
                                .nombre("Entornos de desarrollo")
                                .categoria(tecnologia)
                                .build();

                SubCategoria serviciosNube = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440008"))
                                .nombre("Servicios en nube")
                                .categoria(tecnologia)
                                .build();

                SubCategoria lenguajesProgramacion = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440009"))
                                .nombre("lenguajes de programación")
                                .categoria(tecnologia)
                                .build();

                SubCategoria frameworksDesarrollo = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440009"))
                                .nombre("Frameworks de desarrollo")
                                .categoria(tecnologia)
                                .build();

                SubCategoria basesDatos = SubCategoria.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440010"))
                                .nombre("Bases de datos")
                                .categoria(tecnologia)
                                .build();

                /**
                 * SKILLS
                 */
                // Instrumentos musicales
                Skill guitarra = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                                .descripcion("Guitarra")
                                .subCategoria(instrumentosMusicales)
                                .build();

                Skill piano = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                                .descripcion("Piano")
                                .subCategoria(instrumentosMusicales)
                                .build();

                Skill violin = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                                .descripcion("Violín")
                                .subCategoria(instrumentosMusicales)
                                .build();

                // Canto
                Skill opera = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"))
                                .descripcion("Ópera")
                                .subCategoria(canto)
                                .build();

                Skill rap = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440004"))
                                .descripcion("Rap")
                                .subCategoria(canto)
                                .build();

                Skill cantoUrbano = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"))
                                .descripcion("Canto urbano")
                                .subCategoria(canto)
                                .build();

                // DJ
                Skill mezclas = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440006"))
                                .descripcion("Mezclas")
                                .subCategoria(dj)
                                .build();

                Skill remixes = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440007"))
                                .descripcion("Remixes")
                                .subCategoria(dj)
                                .build();

                // Inicial
                Skill lenguaje = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440008"))
                                .descripcion("Lenguaje")
                                .subCategoria(inicial)
                                .build();

                Skill matematica = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440009"))
                                .descripcion("Matemática")
                                .subCategoria(inicial)
                                .build();

                Skill cienciasNaturales = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440010"))
                                .descripcion("Ciencias naturales")
                                .subCategoria(inicial)
                                .build();

                // Primaria
                Skill lenguajePrimaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440011"))
                                .descripcion("Lenguaje")
                                .subCategoria(primaria)
                                .build();

                Skill comprensionLectura = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440012"))
                                .descripcion("Comprensión de lectura")
                                .subCategoria(primaria)
                                .build();

                Skill biologia = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440013"))
                                .descripcion("Biología")
                                .subCategoria(primaria)
                                .build();

                Skill historia = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440014"))
                                .descripcion("Historia")
                                .subCategoria(primaria)
                                .build();

                Skill geografia = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440015"))
                                .descripcion("Geografía")
                                .subCategoria(primaria)
                                .build();

                Skill geometria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440016"))
                                .descripcion("Geometría")
                                .subCategoria(primaria)
                                .build();

                Skill aritmetica = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440017"))
                                .descripcion("Aritmética")
                                .subCategoria(primaria)
                                .build();

                // Secundaria
                Skill lenguajeSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440018"))
                                .descripcion("Lenguaje")
                                .subCategoria(secundaria)
                                .build();

                Skill literatura = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440019"))
                                .descripcion("Literatura")
                                .subCategoria(secundaria)
                                .build();

                Skill comprensionLecturaSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440019"))
                                .descripcion("Comprensión de lectura")
                                .subCategoria(secundaria)
                                .build();

                Skill biologíaSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440020"))
                                .descripcion("Biología")
                                .subCategoria(secundaria)
                                .build();

                Skill quimica = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440021"))
                                .descripcion("Química")
                                .subCategoria(secundaria)
                                .build();

                Skill fisica = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440022"))
                                .descripcion("Física")
                                .subCategoria(secundaria)
                                .build();

                Skill historiaSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440023"))
                                .descripcion("Historia")
                                .subCategoria(secundaria)
                                .build();

                Skill geografiaSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440024"))
                                .descripcion("Geografía")
                                .subCategoria(secundaria)
                                .build();

                Skill geometriaSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440025"))
                                .descripcion("Geometría")
                                .subCategoria(secundaria)
                                .build();

                Skill aritmeticaSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440026"))
                                .descripcion("Aritmética")
                                .subCategoria(secundaria)
                                .build();

                Skill trigonometriaSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440027"))
                                .descripcion("Trigonometría")
                                .subCategoria(secundaria)
                                .build();

                Skill algebraSecundaria = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440028"))
                                .descripcion("Álgebra")
                                .subCategoria(secundaria)
                                .build();

                // Preuniversitario
                Skill lenguajePreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440029"))
                                .descripcion("Lenguaje")
                                .subCategoria(preuniversitario)
                                .build();

                Skill habilidadVerbal = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440030"))
                                .descripcion("habilidad verbal")
                                .subCategoria(preuniversitario)
                                .build();

                Skill quimicaPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440031"))
                                .descripcion("Química")
                                .subCategoria(preuniversitario)
                                .build();

                Skill fisicaPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440032"))
                                .descripcion("Física")
                                .subCategoria(preuniversitario)
                                .build();

                Skill historiaPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440033"))
                                .descripcion("Historia")
                                .subCategoria(preuniversitario)
                                .build();

                Skill geografiaPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440034"))
                                .descripcion("Geografía")
                                .subCategoria(preuniversitario)
                                .build();

                Skill geometriaPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440035"))
                                .descripcion("Geometría")
                                .subCategoria(preuniversitario)
                                .build();

                Skill aritmeticaPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440036"))
                                .descripcion("Aritmética")
                                .subCategoria(preuniversitario)
                                .build();

                Skill trigonometriaPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440037"))
                                .descripcion("Trigonometría")
                                .subCategoria(preuniversitario)
                                .build();

                Skill algebraPreuniversitario = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440038"))
                                .descripcion("Álgebra")
                                .subCategoria(preuniversitario)
                                .build();

                // Entornos de desarrollo
                Skill frontend = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440039"))
                                .descripcion("Frontend")
                                .subCategoria(entornosDesarrollo)
                                .build();

                Skill backend = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440040"))
                                .descripcion("Backend")
                                .subCategoria(entornosDesarrollo)
                                .build();

                Skill desarrolloMovil = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440041"))
                                .descripcion("Desarrollo móvil")
                                .subCategoria(entornosDesarrollo)
                                .build();

                Skill fullstack = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440042"))
                                .descripcion("Fullstack")
                                .subCategoria(entornosDesarrollo)
                                .build();

                Skill sap = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440043"))
                                .descripcion("SAP")
                                .subCategoria(entornosDesarrollo)
                                .build();

                // Servicios en nube
                Skill aws = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440044"))
                                .descripcion("AWS")
                                .subCategoria(serviciosNube)
                                .build();

                Skill azure = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440045"))
                                .descripcion("Azure")
                                .subCategoria(serviciosNube)
                                .build();

                Skill googleCloud = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440046"))
                                .descripcion("Google Cloud")
                                .subCategoria(serviciosNube)
                                .build();

                // Lenguajes de programación
                Skill java = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440047"))
                                .descripcion("Java")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill python = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440048"))
                                .descripcion("Python")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill cPlusPlus = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440049"))
                                .descripcion("C++")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill cSharp = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440050"))
                                .descripcion("C#")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill go = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440051"))
                                .descripcion("Go")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill golang = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440051"))
                                .descripcion("Go")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill javascript = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440052"))
                                .descripcion("Javascript")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill typescript = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440053"))
                                .descripcion("Typescript")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill ruby = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440054"))
                                .descripcion("Ruby")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                Skill php = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440055"))
                                .descripcion("PHP")
                                .subCategoria(lenguajesProgramacion)
                                .build();

                // Frameworks
                Skill spring = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440056"))
                                .descripcion("Spring")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill django = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440057"))
                                .descripcion("Django")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill flask = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440058"))
                                .descripcion("Flask")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill dotNet = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440059"))
                                .descripcion(".NET")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill dotNetCore = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440060"))
                                .descripcion(".NET Core")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill angular = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440061"))
                                .descripcion("Angular")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill react = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440062"))
                                .descripcion("React")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill vue = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440063"))
                                .descripcion("Vue")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill nextJs = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440064"))
                                .descripcion("Next.js")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill rubyOnRails = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440065"))
                                .descripcion("Ruby on rails")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                Skill laravel = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440066"))
                                .descripcion("Laravel")
                                .subCategoria(frameworksDesarrollo)
                                .build();

                // Bases de datos
                Skill sqlServer = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440067"))
                                .descripcion("SQL Server")
                                .subCategoria(basesDatos)
                                .build();

                Skill oracle = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440068"))
                                .descripcion("Oracle")
                                .subCategoria(basesDatos)
                                .build();

                Skill mysql = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440069"))
                                .descripcion("MySQL")
                                .subCategoria(basesDatos)
                                .build();

                Skill postgreSql = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440070"))
                                .descripcion("PostgreSQL")
                                .subCategoria(basesDatos)
                                .build();

                Skill mariaDB = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440071"))
                                .descripcion("MariaDB")
                                .subCategoria(basesDatos)
                                .build();

                Skill sqLite = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440072"))
                                .descripcion("SQLite")
                                .subCategoria(basesDatos)
                                .build();

                Skill procedimientosAlmacenados = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440073"))
                                .descripcion("procedimientos almacenados")
                                .subCategoria(basesDatos)
                                .build();

                Skill funciones = Skill.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440074"))
                                .descripcion("Funciones")
                                .subCategoria(basesDatos)
                                .build();

                /**
                 * Usuarios y habilidades
                 */
                Usuario proveedor = Usuario.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                                .tipoDocumento(TipoDocumento.dni)
                                .dni("76543210")
                                .nombres("Piero")
                                .apellidos("Toscano")
                                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                                .clave(passwordEncoder.encode("clave123"))
                                .perfilLinkedin("https://www.linkedin.com/")
                                .perfilFacebook("https://www.facebook.com/")
                                .perfilInstagram("https://www.instagram.com/")
                                .perfilTiktok("https://www.tiktok.com/explore")
                                .introduccion("Ofresco servicios informáticos para tu negocio")
                                .build();

                SkillUsuario skillFullstack = SkillUsuario.builder()
                                .skill(fullstack)
                                .usuario(proveedor)
                                .nivelConocimiento(8)
                                .descripcion("Tengo un portafolio de proyectos")
                                .build();

                SkillUsuario skillFrontend = SkillUsuario.builder()
                                .skill(frontend)
                                .usuario(proveedor)
                                .nivelConocimiento(8)
                                .descripcion("Visita mi web")
                                .build();

                SkillUsuario skillBackend = SkillUsuario.builder()
                                .skill(backend)
                                .usuario(proveedor)
                                .nivelConocimiento(8)
                                .descripcion("Integro sitios web con APIs para manipular datos")
                                .build();

                Usuario cliente = Usuario.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                                .tipoDocumento(TipoDocumento.dni)
                                .dni("87654321")
                                .nombres("Christian")
                                .apellidos("Burgos")
                                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                                .clave(passwordEncoder.encode("clave123"))
                                .perfilLinkedin("https://www.linkedin.com/")
                                .perfilFacebook("https://www.facebook.com/")
                                .perfilInstagram("https://www.instagram.com/")
                                .perfilTiktok("https://www.tiktok.com/explore")
                                .build();

                SkillUsuario skillOracle = SkillUsuario.builder()
                                .skill(oracle)
                                .usuario(cliente)
                                .nivelConocimiento(9)
                                .descripcion("Conecto desde Excel, hasta hacer analítica")
                                .build();

                SkillUsuario skillSqlServer = SkillUsuario.builder()
                                .skill(sqlServer)
                                .usuario(cliente)
                                .nivelConocimiento(9)
                                .descripcion("Generación de reportes y análisis de datos")
                                .build();

                /**
                 * Servicio del proveedor
                 */
                // Servicio
                Servicio servicioBackend = Servicio.builder()
                                .titulo("Creación de APIs para conectar con bases de datos")
                                .descripcion("Te ayudo a crear una API para que tus usuarios puedan interactuar con tu base de datos para el sistema que estás creando")
                                .precio(1500)
                                .build();

                // Recursos multimedia
                RecursoMultimediaServicio linkVideoTiktok = RecursoMultimediaServicio.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                                .medio(Medio.tiktok)
                                .servicio(servicioBackend)
                                .url("https://www.tiktok.com/@pierotoscano1496/video/7065210063484357894?is_from_webapp=1&sender_device=pc&web_id=7320697309019588101")
                                .build();

                RecursoMultimediaServicio linkVideoInstagram = RecursoMultimediaServicio.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                                .medio(Medio.tiktok)
                                .servicio(servicioBackend)
                                .url("https://www.instagram.com/reel/CbCLRhNgPJK/?utm_source=ig_web_copy_link&igsh=MzRlODBiNWFlZA==")
                                .build();

                /**
                 * MODALIDADES DE PAGO
                 */
                ModalidadPago yape = ModalidadPago.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                                .tipo(Tipo.yape)
                                .numeroCelular("987654321")
                                .servicio(servicioBackend)
                                .build();

                ModalidadPago tarjeta = ModalidadPago.builder()
                                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                                .tipo(Tipo.tarjeta)
                                .cuentaBancaria("1122334455667")
                                .servicio(servicioBackend)
                                .build();

                /**
                 * Registrar los objetos en sus tablas respectivas
                 */

        }

        @Container
        private static final MySQLContainer mySqlContainer = new MySQLContainer<>("mysql:8.0.33")
                        .withDatabaseName("skill_exchange_dev")
                        .withUsername("skilleduser")
                        .withPassword("piero1496")
                        .withInitScript("init.sql");
        int port = mySqlContainer.getMappedPort(3306);

        String jdbcUrl = mySqlContainer.getJdbcUrl();

        @DynamicPropertySource
        private static void setupProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
                registry.add("spring.datasource.username", mySqlContainer::getUsername);
                registry.add("spring.datasource.password", mySqlContainer::getPassword);
        }

        @Test
        void testMySQLContainerIsRunning() {
                System.out.println(mySqlContainer.getJdbcUrl());
                Assertions.assertThat(mySqlContainer.isRunning()).isTrue();
        }

        @Test
        void testTableExists() throws SQLException {
                try (Connection connection = databaseConnection.getConnection();
                                ResultSet resultSet = connection.prepareStatement("SHOW TABLES").executeQuery()) {
                        int tablesCount = 0;
                        while (resultSet.next()) {
                                tablesCount++;
                        }

                        Assertions.assertThat(tablesCount).isGreaterThan(0);
                }
        }

        /*
         * @Test
         * void testMatchRegistered(){
         * // Objetos que componendrán los matchs
         * Usuario usuario=new
         * 
         * Match match=Match.builder()
         * 
         * .build();
         * Match = repository.registrar()
         * }
         * 
         */
}
