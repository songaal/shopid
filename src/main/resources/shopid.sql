CREATE DATABASE `shopid` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

CREATE TABLE `user` (
  `id` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `orgId` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `type` char(1) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'U',
  `joinDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_org_id_idx` (`orgId`),
  CONSTRAINT `fk_org_id` FOREIGN KEY (`orgId`) REFERENCES `organization` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

