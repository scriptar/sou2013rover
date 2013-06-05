--Database used by the ROGO website for storing programs created in web-mode

--delimiter $$

--CREATE DATABASE `usr_rogo_0` /*!40100 DEFAULT CHARACTER SET latin1 */$$

--DROP TABLE IF EXISTS `srctags`;
--DROP TABLE IF EXISTS `tags`;
--DROP TABLE IF EXISTS `src`;
--DROP TABLE IF EXISTS `requests`;

delimiter $$

CREATE TABLE IF NOT EXISTS `requests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) DEFAULT NULL,
  `Email` varchar(150) DEFAULT NULL,
  `Message` text,
  `SubmitTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE IF NOT EXISTS `src` (
  `idsrc` int(11) NOT NULL AUTO_INCREMENT,
  `hash` char(32) NOT NULL,
  `source` text,
  `savetime` datetime DEFAULT '0000-00-00 00:00:00',
  `host` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idsrc`),
  KEY `hash` (`hash`),
  KEY `savetime` (`savetime`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE IF NOT EXISTS `tags` (
  `idtags` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(100) NOT NULL,
  PRIMARY KEY (`idtags`),
  KEY `tag` (`tag`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE IF NOT EXISTS `srctags` (
  `idsrctags` int(11) NOT NULL AUTO_INCREMENT,
  `idsrc` int(11) NOT NULL,
  `idtags` int(11) NOT NULL,
  PRIMARY KEY (`idsrctags`),
  KEY `src_FK` (`idsrc`),
  KEY `tags_FK` (`idtags`),
  CONSTRAINT `src_FK` FOREIGN KEY (`idsrc`) REFERENCES `src` (`idsrc`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tags_FK` FOREIGN KEY (`idtags`) REFERENCES `tags` (`idtags`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1$$
