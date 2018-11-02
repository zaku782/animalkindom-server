-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: 192.168.108.129    Database: animalkindom
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `animal`
--

DROP TABLE IF EXISTS `animal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `animal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `health` int(11) DEFAULT NULL,
  `baseHealth` int(11) DEFAULT NULL,
  `satiety` int(11) DEFAULT NULL,
  `baseSatiety` int(11) DEFAULT NULL,
  `vigour` int(11) DEFAULT NULL,
  `strength` int(11) DEFAULT NULL,
  `intelligence` int(11) DEFAULT NULL,
  `agile` int(11) DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `accountId` int(11) DEFAULT NULL,
  `typeId` int(11) DEFAULT NULL,
  `growLevel` int(11) DEFAULT NULL,
  `maxGrowLevel` int(11) DEFAULT NULL,
  `sleepTime` bigint(13) DEFAULT NULL,
  `exploreTime` bigint(13) DEFAULT NULL,
  `mapDiscovered` varbinary(1000) DEFAULT NULL,
  `currentPos` int(11) DEFAULT NULL,
  `moveTime` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animal`
--

LOCK TABLES `animal` WRITE;
/*!40000 ALTER TABLE `animal` DISABLE KEYS */;
INSERT INTO `animal` VALUES (1,'tiger',500,500,25,250,70,100,50,80,70,1,2,1,40,NULL,NULL,_binary '\�h\�\0\0@\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',21,1541131217551);
/*!40000 ALTER TABLE `animal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `animal_data`
--

DROP TABLE IF EXISTS `animal_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `animal_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `baseHealth` int(11) DEFAULT NULL,
  `baseSatiety` int(11) DEFAULT NULL,
  `baseStrength` int(11) DEFAULT NULL,
  `baseIntelligence` int(11) DEFAULT NULL,
  `baseAgile` int(11) DEFAULT NULL,
  `baseSpeed` int(11) DEFAULT NULL,
  `maxGrowLevel` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animal_data`
--

LOCK TABLES `animal_data` WRITE;
/*!40000 ALTER TABLE `animal_data` DISABLE KEYS */;
INSERT INTO `animal_data` VALUES (1,'lion',450,220,80,50,60,50,30),(2,'tiger',500,250,100,50,80,70,40);
/*!40000 ALTER TABLE `animal_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop` varchar(45) DEFAULT NULL,
  `value` varchar(45) DEFAULT NULL,
  `desc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (1,'maxMapNumber','4000','地图列表大小'),(3,'mapMoveInterval','10','地图之间移动时间间隔,单位秒'),(4,'moveSatietyCost','0.3','地图之间移动消耗的饱食度百分比'),(5,'moveVigourCost','30','地图之间移动消耗精力'),(6,'sleepVigourRecoverInterval','300','睡眠时精力恢复的最小时间单位,单位秒'),(7,'sleepVigourRecover','1','睡眠时每时间单位恢复精力'),(8,'sleepSatietyCost','0.001','睡眠时每时间单位饱食度消耗百分比');
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `food` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `satietyAdding` int(11) DEFAULT NULL,
  `discoverer` int(11) DEFAULT NULL,
  `discoverTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `map`
--

DROP TABLE IF EXISTS `map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `map` (
  `id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `map`
--

LOCK TABLES `map` WRITE;
/*!40000 ALTER TABLE `map` DISABLE KEYS */;
INSERT INTO `map` VALUES (0,'新生之地'),(1,'萤火森林'),(2,'刀锋山'),(5,'暴风沙漠'),(6,'黄金草原'),(7,'魔法森林'),(11,'哭泣海岸'),(13,'银松森林'),(14,'十字路口'),(19,'贫瘠之地'),(21,'灰谷'),(22,'千针石林'),(23,'瘟疫之地'),(46,'尘泥沼泽');
/*!40000 ALTER TABLE `map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `salt` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test','2f01b8630cbd2bf759449add35edf9524a7ad3058a4cac10a3659f8b29fd3952','80a43221fd9bd1beeb1e5094fd86173b60823c3e27adcad1b1e06144a6e683ee');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-02 13:40:25
