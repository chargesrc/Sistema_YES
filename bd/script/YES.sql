-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema YES
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `YES` ;

-- -----------------------------------------------------
-- Schema YES
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `YES` DEFAULT CHARACTER SET utf8 ;
USE `YES` ;

-- -----------------------------------------------------
-- Table `YES`.`Persona`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`Persona` ;

CREATE TABLE IF NOT EXISTS `YES`.`Persona` (
  `idPersona` INT NOT NULL AUTO_INCREMENT,
  `dni` VARCHAR(8) NOT NULL,
  `apellido` VARCHAR(40) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `fechaNacimiento` DATE NULL,
  PRIMARY KEY (`idPersona`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`Alumno`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`Alumno` ;

CREATE TABLE IF NOT EXISTS `YES`.`Alumno` (
  `idAlumno` INT NOT NULL AUTO_INCREMENT,
  `idPersona` INT NULL,
  `legajo` VARCHAR(20) NOT NULL,
  `tutor` VARCHAR(45) NOT NULL,
  `cotutor` VARCHAR(45) NULL,
  `celular` VARCHAR(15) NULL,
  `direccion` VARCHAR(50) NULL,
  `borrado` TINYINT(1) NULL,
  `egresado` VARCHAR(15) NULL,
  `nomImagen` VARCHAR(500) NULL,
  `imagen` MEDIUMBLOB NULL,
  PRIMARY KEY (`idAlumno`),
  INDEX `personaAlumno_idx` (`idPersona` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`Profesor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`Profesor` ;

CREATE TABLE IF NOT EXISTS `YES`.`Profesor` (
  `idProfesor` INT NOT NULL AUTO_INCREMENT,
  `idPersona` INT NULL,
  `celular` VARCHAR(18) NULL,
  `direccion` VARCHAR(50) NULL,
  `borrado` TINYINT(1) NULL,
  PRIMARY KEY (`idProfesor`),
  INDEX `personaProfesor_idx` (`idPersona` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`Curso`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`Curso` ;

CREATE TABLE IF NOT EXISTS `YES`.`Curso` (
  `idCurso` INT NOT NULL AUTO_INCREMENT,
  `idProfesor` INT NULL,
  `nombre` VARCHAR(40) NOT NULL,
  `precioInscripcion` FLOAT NULL,
  `cuota` FLOAT NULL,
  `cantidad` INT NULL,
  `fechaInicio` DATE NULL,
  `cantidadAño` INT NULL,
  `interesCurso` FLOAT NULL,
  `aula` VARCHAR(20) NULL,
  PRIMARY KEY (`idCurso`),
  INDEX `cursoProfesor_idx` (`idProfesor` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`cursoAlumno`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`cursoAlumno` ;

CREATE TABLE IF NOT EXISTS `YES`.`cursoAlumno` (
  `idcursoAlumno` INT NOT NULL AUTO_INCREMENT,
  `idCurso` INT NULL,
  `idAlumno` INT NULL,
  `añoLectivo` YEAR NOT NULL,
  `saldoAdeudor` FLOAT NULL,
  `fechaInscripcion` DATE NULL,
  `deudaInscripcion` FLOAT NULL,
  `horario` VARCHAR(20) NULL,
  `añoCurso` VARCHAR(20) NULL,
  `borrarcurso` TINYINT(1) NULL,
  PRIMARY KEY (`idcursoAlumno`),
  INDEX `curso_idx` (`idCurso` ASC),
  INDEX `cursoAlumno_idx` (`idAlumno` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`Cuota`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`Cuota` ;

CREATE TABLE IF NOT EXISTS `YES`.`Cuota` (
  `idCuota` INT NOT NULL AUTO_INCREMENT,
  `idCursoAlumno` INT NULL,
  `mes` INT NULL,
  `fechaVencimiento` DATE NULL,
  `fechaPago` DATE NULL,
  `importe` FLOAT NULL,
  `estado` VARCHAR(10) NULL,
  `interes` FLOAT NULL,
  `faltante` FLOAT NULL,
  `borrarpago` TINYINT(1) NULL,
  `perdonar` TINYINT(1) NULL,
  PRIMARY KEY (`idCuota`),
  INDEX `cursoAlumno_idx` (`idCursoAlumno` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`Horario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`Horario` ;

CREATE TABLE IF NOT EXISTS `YES`.`Horario` (
  `idHorario` INT NOT NULL AUTO_INCREMENT,
  `nomHorario` VARCHAR(45) NOT NULL,
  `disponible` TINYINT(1) NULL,
  PRIMARY KEY (`idHorario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`CursoHorario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`CursoHorario` ;

CREATE TABLE IF NOT EXISTS `YES`.`CursoHorario` (
  `idCursoHorario` INT NOT NULL AUTO_INCREMENT,
  `idCurso` INT NULL,
  `idHorario` INT NULL,
  PRIMARY KEY (`idCursoHorario`),
  INDEX `CursoHorario_idx` (`idCurso` ASC),
  INDEX `HorarioCurso_idx` (`idHorario` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `YES`.`CuotaInscripcion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `YES`.`CuotaInscripcion` ;

CREATE TABLE IF NOT EXISTS `YES`.`CuotaInscripcion` (
  `idPrecioInscripcion` INT NOT NULL AUTO_INCREMENT,
  `idCursoAlumno` INT NOT NULL,
  `idAlumno` INT NOT NULL,
  `fechaPago` DATE NULL,
  `importe` FLOAT NULL,
  `estado` VARCHAR(10) NULL,
  `borrarcuota` TINYINT(1) NULL,
  PRIMARY KEY (`idPrecioInscripcion`),
  INDEX `fk_PrecioInscripcion_cursoAlumno1_idx` (`idCursoAlumno` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
