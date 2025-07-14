-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `ekuamarket` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `ekuamarket`;

-- Table `roles`
CREATE TABLE IF NOT EXISTS `roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre` (`nombre` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `usuarios`
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rol_id` INT NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) NOT NULL,
  `contraseña` VARCHAR(200) NOT NULL,
  `creado_en` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `inactivo` TINYINT(1) NOT NULL DEFAULT '0',
  `imagen_perfil` VARCHAR(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email` (`email` ASC),
  INDEX `rol_id` (`rol_id` ASC),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_id`)
    REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `carritos`
CREATE TABLE IF NOT EXISTS `carritos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario_id` INT NOT NULL,
  `creado_en` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `usuario_id` (`usuario_id` ASC),
  CONSTRAINT `carritos_ibfk_1` FOREIGN KEY (`usuario_id`)
    REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `vendedores`
CREATE TABLE IF NOT EXISTS `vendedores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario_id` INT NOT NULL,
  `nombre_tienda` VARCHAR(150) NOT NULL,
  `descripcion` TEXT DEFAULT NULL,
  `calificacion_promedio` DECIMAL(3,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  INDEX `usuario_id` (`usuario_id` ASC),
  CONSTRAINT `vendedores_ibfk_1` FOREIGN KEY (`usuario_id`)
    REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `categorias`
CREATE TABLE IF NOT EXISTS `categorias` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `parent_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `parent_id` (`parent_id` ASC),
  CONSTRAINT `categorias_ibfk_1` FOREIGN KEY (`parent_id`)
    REFERENCES `categorias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `productos`
CREATE TABLE IF NOT EXISTS `productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `vendedor_id` INT NOT NULL,
  `categoria_id` INT NOT NULL,
  `titulo` VARCHAR(200) NOT NULL,
  `descripcion` TEXT DEFAULT NULL,
  `creado_en` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `activo` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  INDEX `vendedor_id` (`vendedor_id` ASC),
  INDEX `categoria_id` (`categoria_id` ASC),
  CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`vendedor_id`)
    REFERENCES `vendedores` (`id`) ON DELETE CASCADE,
  CONSTRAINT `productos_ibfk_2` FOREIGN KEY (`categoria_id`)
    REFERENCES `categorias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `variantes_producto`
CREATE TABLE IF NOT EXISTS `variantes_producto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `sku` VARCHAR(100) NOT NULL,
  `precio` DECIMAL(10,2) NOT NULL,
  `stock` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `sku` (`sku` ASC),
  INDEX `producto_id` (`producto_id` ASC),
  CONSTRAINT `variantes_producto_ibfk_1` FOREIGN KEY (`producto_id`)
    REFERENCES `productos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `carrito_items`
CREATE TABLE IF NOT EXISTS `carrito_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `carrito_id` INT NOT NULL,
  `variante_id` INT NOT NULL,
  `cantidad` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `carrito_id` (`carrito_id` ASC),
  INDEX `variante_id` (`variante_id` ASC),
  CONSTRAINT `carrito_items_ibfk_1` FOREIGN KEY (`carrito_id`)
    REFERENCES `carritos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `carrito_items_ibfk_2` FOREIGN KEY (`variante_id`)
    REFERENCES `variantes_producto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `cupones`
CREATE TABLE IF NOT EXISTS `cupones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `codigo` VARCHAR(50) NOT NULL,
  `descuento_pct` INT NOT NULL,
  `fecha_expiracion` DATETIME NOT NULL,
  `uso_maximo` INT NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `codigo` (`codigo` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `direcciones`
CREATE TABLE IF NOT EXISTS `direcciones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario_id` INT NOT NULL,
  `alias` VARCHAR(50) DEFAULT NULL,
  `direccion` TEXT NOT NULL,
  `ciudad` VARCHAR(100) NOT NULL,
  `provincia` VARCHAR(100) NOT NULL,
  `canton` VARCHAR(100) DEFAULT NULL,
  `codigo_postal` VARCHAR(20) DEFAULT NULL,
  `telefono_contacto` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `usuario_id` (`usuario_id` ASC),
  CONSTRAINT `direcciones_ibfk_1` FOREIGN KEY (`usuario_id`)
    REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `ordenes`
CREATE TABLE IF NOT EXISTS `ordenes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario_id` INT NOT NULL,
  `estado` ENUM('pendiente','pagada','enviado','entregado','cancelada') NOT NULL DEFAULT 'pendiente',
  `total_bruto` DECIMAL(12,2) NOT NULL,
  `total_descuento` DECIMAL(12,2) NOT NULL DEFAULT '0.00',
  `fecha_creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `usuario_id` (`usuario_id` ASC),
  CONSTRAINT `ordenes_ibfk_1` FOREIGN KEY (`usuario_id`)
    REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `envios`
CREATE TABLE IF NOT EXISTS `envios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orden_id` INT NOT NULL,
  `direccion_id` INT NOT NULL,
  `empresa_envio` VARCHAR(100) DEFAULT NULL,
  `codigo_tracking` VARCHAR(100) DEFAULT NULL,
  `fecha_envio` DATETIME DEFAULT NULL,
  `fecha_entrega_estimada` DATETIME DEFAULT NULL,
  `fecha_entrega_real` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `codigo_tracking` (`codigo_tracking` ASC),
  INDEX `orden_id` (`orden_id` ASC),
  INDEX `direccion_id` (`direccion_id` ASC),
  CONSTRAINT `envios_ibfk_1` FOREIGN KEY (`orden_id`)
    REFERENCES `ordenes` (`id`),
  CONSTRAINT `envios_ibfk_2` FOREIGN KEY (`direccion_id`)
    REFERENCES `direcciones` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `imagenes_producto`
CREATE TABLE IF NOT EXISTS `imagenes_producto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `url` VARCHAR(300) NOT NULL,
  `es_principal` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `producto_id` (`producto_id` ASC),
  CONSTRAINT `imagenes_producto_ibfk_1` FOREIGN KEY (`producto_id`)
    REFERENCES `productos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `listas_deseos`
CREATE TABLE IF NOT EXISTS `listas_deseos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario_id` INT NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `creado_en` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `usuario_id` (`usuario_id` ASC),
  CONSTRAINT `listas_deseos_ibfk_1` FOREIGN KEY (`usuario_id`)
    REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `lista_deseos_items`
CREATE TABLE IF NOT EXISTS `lista_deseos_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `lista_deseos_id` INT NOT NULL,
  `variante_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `lista_deseos_id` (`lista_deseos_id` ASC),
  INDEX `variante_id` (`variante_id` ASC),
  CONSTRAINT `lista_deseos_items_ibfk_1` FOREIGN KEY (`lista_deseos_id`)
    REFERENCES `listas_deseos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `lista_deseos_items_ibfk_2` FOREIGN KEY (`variante_id`)
    REFERENCES `variantes_producto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `ofertas`
CREATE TABLE IF NOT EXISTS `ofertas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `variante_id` INT NOT NULL,
  `precio_descuento` DECIMAL(10,2) NOT NULL,
  `fecha_inicio` DATETIME NOT NULL,
  `fecha_fin` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `variante_id` (`variante_id` ASC),
  CONSTRAINT `ofertas_ibfk_1` FOREIGN KEY (`variante_id`)
    REFERENCES `variantes_producto` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `orden_items`
CREATE TABLE IF NOT EXISTS `orden_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orden_id` INT NOT NULL,
  `variante_id` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `precio_unitario` DECIMAL(10,2) NOT NULL,
  `precio_descuento` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  INDEX `orden_id` (`orden_id` ASC),
  INDEX `variante_id` (`variante_id` ASC),
  CONSTRAINT `orden_items_ibfk_1` FOREIGN KEY (`orden_id`)
    REFERENCES `ordenes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `orden_items_ibfk_2` FOREIGN KEY (`variante_id`)
    REFERENCES `variantes_producto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `pagos`
CREATE TABLE IF NOT EXISTS `pagos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orden_id` INT NOT NULL,
  `metodo_pago` VARCHAR(50) DEFAULT NULL,
  `monto` DECIMAL(12,2) NOT NULL,
  `fecha_pago` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `orden_id` (`orden_id` ASC),
  CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`orden_id`)
    REFERENCES `ordenes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table `reseñas`
CREATE TABLE IF NOT EXISTS `reseñas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `producto_id` INT NOT NULL,
  `usuario_id` INT NOT NULL,
  `rating` INT NOT NULL,
  `comentario` TEXT DEFAULT NULL,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `producto_id` (`producto_id` ASC),
  INDEX `usuario_id` (`usuario_id` ASC),
  CONSTRAINT `reseñas_ibfk_1` FOREIGN KEY (`producto_id`)
    REFERENCES `productos` (`id`),
  CONSTRAINT `reseñas_ibfk_2` FOREIGN KEY (`usuario_id`)
    REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER $$
CREATE TRIGGER `variantes_producto_chk` BEFORE INSERT ON `variantes_producto`
FOR EACH ROW
BEGIN
  IF NEW.stock < 0 THEN
     SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El stock no puede ser negativo';
  END IF;
  IF NEW.precio <= 0 THEN
     SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El precio debe ser mayor a 0';
  END IF;
END$$
DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
