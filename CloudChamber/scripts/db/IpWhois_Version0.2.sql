CREATE DATABASE  IF NOT EXISTS `whoisdev` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `whoisdev`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: whoisdev
-- ------------------------------------------------------
-- Server version	5.6.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ip_whois`
--

DROP TABLE IF EXISTS `ip_whois`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ip_whois` (
  `ip_start_address` bigint(20) NOT NULL,
  `ip_end_address` bigint(20) NOT NULL,
  `origin_as` varchar(200) DEFAULT NULL,
  `net_name` varchar(200) DEFAULT NULL,
  `net_handle` varchar(200) DEFAULT NULL,
  `parent` varchar(200) DEFAULT NULL,
  `net_type` varchar(200) DEFAULT NULL,
  `net_ref` varchar(200) DEFAULT NULL,
  `data_source` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `reg_date` timestamp NULL DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  `org_name` varchar(200) DEFAULT NULL,
  `org_id` varchar(200) DEFAULT NULL,
  `org_phone` varchar(200) DEFAULT NULL,
  `org_fax` varchar(200) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `state` varchar(200) DEFAULT NULL,
  `country` varchar(200) DEFAULT NULL,
  `postal_code` varchar(200) DEFAULT NULL,
  `org_reg_date` timestamp NULL DEFAULT NULL,
  `org_updated_date` timestamp NULL DEFAULT NULL,
  `org_ref` varchar(200) DEFAULT NULL,
  `is_current_data` tinyint(1) DEFAULT '1',
  `last_updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `raw_response` longtext,
  `route_description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ip_start_address`,`ip_end_address`),
  KEY `IPWHOIS_PK` (`ip_start_address`,`ip_end_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ip_whois`
--

LOCK TABLES `ip_whois` WRITE;
/*!40000 ALTER TABLE `ip_whois` DISABLE KEYS */;
INSERT INTO `ip_whois` VALUES (692258816,692259327,'','Rebat-SUIN','AFRINIC-HM-MNT','41.67.0.0-41.67.63.255','Assigned PA',NULL,'AFRINIC#FILTERED','Rebat University',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'2014-02-28 09:35:55',NULL,NULL),(3231054848,3231055103,'AS10745','ARIN-NET','NET-192-149-252-0-1','NET-192-0-0-0-0','Direct Assignment',NULL,'ARIN',NULL,'2012-09-06 13:00:00','2012-09-18 13:00:00','ARIN Operations','ARINOPS',NULL,NULL,'Chantilly','VA','US','20151','2012-09-06 13:00:00','2012-09-18 13:00:00','http://whois.arin.net/rest/org/ARINOPS',1,'2014-02-28 06:48:21',NULL,NULL);
/*!40000 ALTER TABLE `ip_whois` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_whois_contact`
--

DROP TABLE IF EXISTS `ip_whois_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ip_whois_contact` (
  `ip_start_address` bigint(20) NOT NULL,
  `ip_end_address` bigint(20) NOT NULL,
  `contact_type` varchar(200) NOT NULL,
  `handle` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `phone` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `fax` varchar(200) DEFAULT NULL,
  `ref` varchar(200) DEFAULT NULL,
  `is_current_data` tinyint(1) DEFAULT '1',
  `last_updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `CONTACT_FK_idx` (`ip_start_address`,`ip_end_address`),
  CONSTRAINT `CONTACT_FK` FOREIGN KEY (`ip_start_address`, `ip_end_address`) REFERENCES `ip_whois` (`ip_start_address`, `ip_end_address`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ip_whois_contact`
--

LOCK TABLES `ip_whois_contact` WRITE;
/*!40000 ALTER TABLE `ip_whois_contact` DISABLE KEYS */;
INSERT INTO `ip_whois_contact` VALUES (3231054848,3231055103,'ADMIN','CHRIS167-ARIN','Christensen,Tim','','1-703-227-0660','timc@arin.net','','http://whois.arin.net/rest/poc/CHRIS167-ARIN',1,'2014-02-28 07:04:43'),(692258816,692259327,'TECH','TYA1-AFRINIC','Tarig Y.Adam','Khartoum,Sudan','+249925659149','tarig@suin.edu.sd','+2499780295','',1,'2014-02-28 09:47:43'),(692258816,692259327,'ABUSE','IAM1-AFRINIC','Iman Abuel Maaly','Khartoum,Sudan','+249 155 150931','imaaly@gmail.com','','',1,'2014-02-28 09:49:40');
/*!40000 ALTER TABLE `ip_whois_contact` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-03-05 17:10:32
