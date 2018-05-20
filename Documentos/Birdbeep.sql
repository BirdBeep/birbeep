-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 10-05-2018 a las 22:08:15
-- Versión del servidor: 10.1.9-MariaDB
-- Versión de PHP: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `birdbeep`
--
CREATE DATABASE IF NOT EXISTS `birdbeep` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `birdbeep`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `conexiones`
--

CREATE TABLE `conexiones` (
  `IDCONEXION` int(11) NOT NULL,
  `IP` varchar(16) DEFAULT NULL,
  `USUARIO` varchar(100) DEFAULT NULL,
  `ULTIMA_ACTUALIZACION` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `conexiones`
--

INSERT INTO `conexiones` (`IDCONEXION`, `IP`, `USUARIO`, `ULTIMA_ACTUALIZACION`) VALUES
(22, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(23, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(24, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(25, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(26, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(27, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(28, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(29, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(30, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(31, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(32, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(33, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(34, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(35, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(36, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(37, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(38, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(39, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(40, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(41, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(42, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(43, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(44, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(45, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(46, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(47, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(48, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(49, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(50, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(51, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(52, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(53, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(54, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(55, '/192.168.56.1', 'client3', '0001-03-05 00:00:00'),
(56, '/192.168.56.1', 'client3', '0001-03-05 00:00:00'),
(57, '/192.168.56.1', 'client1', '0001-03-05 00:00:00'),
(58, '/192.168.56.1', 'client1', '0001-03-05 00:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `conversaciones`
--

CREATE TABLE `conversaciones` (
  `IDCONVERSACION` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `conversaciones`
--

INSERT INTO `conversaciones` (`IDCONVERSACION`) VALUES
('conv01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mensajes`
--

CREATE TABLE `mensajes` (
  `IDMENSAJE` int(11) NOT NULL,
  `EMISOR` varchar(100) NOT NULL,
  `RECEPTOR` varchar(100) NOT NULL,
  `TEXTO` varchar(65000) NOT NULL,
  `CONVERSACION` varchar(100) DEFAULT NULL,
  `FECHA` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `mensajes`
--

INSERT INTO `mensajes` (`IDMENSAJE`, `EMISOR`, `RECEPTOR`, `TEXTO`, `CONVERSACION`, `FECHA`) VALUES
(22, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-05 12:55:27'),
(23, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-06 15:05:05'),
(24, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-06 15:07:35'),
(25, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 19:26:11'),
(26, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 19:35:05'),
(27, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 19:36:34'),
(28, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 19:37:34'),
(29, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 19:39:34'),
(30, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 19:42:59'),
(31, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 19:45:43'),
(32, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 19:49:10'),
(33, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 19:58:04'),
(34, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 20:03:15'),
(35, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 21:18:04'),
(36, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 21:20:30'),
(37, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 21:21:41'),
(38, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:04:37'),
(39, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:14:25'),
(40, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 22:37:31'),
(41, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:38:31'),
(42, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 22:39:34'),
(43, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:41:59'),
(44, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:44:28'),
(45, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:50:13'),
(46, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:54:02'),
(47, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:54:36'),
(48, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:58:42'),
(49, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 22:59:28'),
(50, 'client1', 'client3', '[B@49e4cb85', 'conv01', '2018-05-07 23:01:23'),
(51, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 23:03:32'),
(52, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 23:09:13'),
(53, 'client1', 'client3', '[B@7c30a502', 'conv01', '2018-05-07 23:13:31'),
(54, 'client1', 'client3', 'ñ/º^¹ûÂ??³{Zl??Á9?¯?-??)?Ù??ïÀÛ''® Â\Z>»?88Nòá ?«ãÂs?PÓÙ\nG?ce3!?ã¿ ÷?õ??CÇ¹¢yéö·`ó8L^?°-ÚC¥Ï5JÔTèém&ªZcPOvÆs£ %ªSßxÞ?QF&?o5µ>eÓo*Íó[Ï>?­?ÝÀóÝ(?í_âR&?¤á?òô?u??³.èóD?úÆ(ú·¬\r?b«¨G0s?z?W`F?¶h?gG¾ÝH?¶?RdGê!¿rÿÄ·¾Üáö?¬ªÖ÷ÂÒ¨s\n÷?x#LßUTP¡h?,¼ý??RHºñ­|Ç£éãX¾Ì?v}ÐgÁsô??M«!\\×ýíB?1çP]??û?-u3@#ï¨:=?ÇéÓ+(ÛZ?{?â,\0]ðOSMòzÌì¿,x?åH¥MA\nîmÊ?J1)?xÓ\nÌ¦á%\\2áÃÇz;lE?âÆ?)ö@ï?=@ª®ù&DäòXÏÖT¨Óo??C®IH@ðëÉÊ?üôs?ñ?ws0ÂÜº??fGUP|Uº?8>wEQIïÚ¥n?Ì?8?ãUì¬°x×rA[ÇÚ=ûÒª?b}Ç|öQ1èÎ YWþÓÝ{[M+âç[Qz', 'conv01', '2018-05-08 19:14:59'),
(55, 'client1', 'client3', 'probando.....', 'conv01', '2018-05-10 22:02:09'),
(56, 'client1', 'client3', 'probando.....', 'conv01', '2018-05-10 22:06:48');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `participantes`
--

CREATE TABLE `participantes` (
  `USER` varchar(100) NOT NULL,
  `IDCONVERSACION` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `participantes`
--

INSERT INTO `participantes` (`USER`, `IDCONVERSACION`) VALUES
('client1', 'conv01'),
('client3', 'conv01');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `IDUSER` varchar(100) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `NOMBRE` varchar(100) DEFAULT NULL,
  `APELLIDOS` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `ULTIMA_CONEXION` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`IDUSER`, `PASSWORD`, `NOMBRE`, `APELLIDOS`, `EMAIL`, `ULTIMA_CONEXION`) VALUES
('client1', 'sebas', 'sebas', NULL, NULL, NULL),
('client3', 'ruben', 'ruben', NULL, NULL, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `conexiones`
--
ALTER TABLE `conexiones`
  ADD PRIMARY KEY (`IDCONEXION`),
  ADD KEY `fk_conexiones_usuarios` (`USUARIO`);

--
-- Indices de la tabla `conversaciones`
--
ALTER TABLE `conversaciones`
  ADD PRIMARY KEY (`IDCONVERSACION`);

--
-- Indices de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  ADD PRIMARY KEY (`IDMENSAJE`),
  ADD KEY `fk_mensaje_usuarios_e` (`EMISOR`),
  ADD KEY `fk_mensaje_usuarios_r` (`RECEPTOR`),
  ADD KEY `fk_mensaje_conversacion` (`CONVERSACION`);

--
-- Indices de la tabla `participantes`
--
ALTER TABLE `participantes`
  ADD PRIMARY KEY (`USER`,`IDCONVERSACION`),
  ADD KEY `fk_participantes_conversacion` (`IDCONVERSACION`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`IDUSER`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `conexiones`
--
ALTER TABLE `conexiones`
  MODIFY `IDCONEXION` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
--
-- AUTO_INCREMENT de la tabla `mensajes`
--
ALTER TABLE `mensajes`
  MODIFY `IDMENSAJE` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `conexiones`
--
ALTER TABLE `conexiones`
  ADD CONSTRAINT `fk_conexiones_usuarios` FOREIGN KEY (`USUARIO`) REFERENCES `usuarios` (`IDUSER`);

--
-- Filtros para la tabla `mensajes`
--
ALTER TABLE `mensajes`
  ADD CONSTRAINT `fk_mensaje_conversacion` FOREIGN KEY (`CONVERSACION`) REFERENCES `conversaciones` (`IDCONVERSACION`),
  ADD CONSTRAINT `fk_mensaje_usuarios_e` FOREIGN KEY (`EMISOR`) REFERENCES `usuarios` (`IDUSER`),
  ADD CONSTRAINT `fk_mensaje_usuarios_r` FOREIGN KEY (`RECEPTOR`) REFERENCES `usuarios` (`IDUSER`);

--
-- Filtros para la tabla `participantes`
--
ALTER TABLE `participantes`
  ADD CONSTRAINT `fk_participantes_conversacion` FOREIGN KEY (`IDCONVERSACION`) REFERENCES `conversaciones` (`IDCONVERSACION`),
  ADD CONSTRAINT `fk_participantes_usuarios_e` FOREIGN KEY (`USER`) REFERENCES `usuarios` (`IDUSER`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
