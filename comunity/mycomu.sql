-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: mycomu
-- ------------------------------------------------------
-- Server version	8.4.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bbs`
--

DROP TABLE IF EXISTS `bbs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bbs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bbsid` int DEFAULT NULL COMMENT 'bbs_admin id와 연동',
  `ref` bigint DEFAULT '0' COMMENT '그룹',
  `step` int DEFAULT '0' COMMENT '그룹의 순번',
  `depth` int DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `writer` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `userid` varchar(45) DEFAULT 'GUEST',
  `content` text,
  `comment` int DEFAULT '0',
  `wdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `sec` tinyint DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bbs`
--

LOCK TABLES `bbs` WRITE;
/*!40000 ALTER TABLE `bbs` DISABLE KEYS */;
/*!40000 ALTER TABLE `bbs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bbs_admin`
--

DROP TABLE IF EXISTS `bbs_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bbs_admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bbstitle` varchar(45) DEFAULT NULL COMMENT '게시판제목',
  `skin` varchar(45) DEFAULT 'bbs' COMMENT 'bbs, gallery, article, section',
  `category` tinyint DEFAULT '0' COMMENT '0:카테고리 사용안함 1:카테고리 사용함',
  `listcount` tinyint DEFAULT '15',
  `pagecount` tinyint DEFAULT '15',
  `lgrade` tinyint DEFAULT '0' COMMENT '0 모두보기 ',
  `rgrade` tinyint DEFAULT '0',
  `fgrade` tinyint DEFAULT '0',
  `regrade` tinyint DEFAULT '0',
  `comgrade` tinyint DEFAULT '0',
  `filesize` int DEFAULT '1000000' COMMENT '1M',
  `allfilesize` int DEFAULT '3000000' COMMENT '3M',
  `thimgsize` varchar(255) DEFAULT '150|',
  `imgsize` varchar(255) DEFAULT '800|',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bbs_admin`
--

LOCK TABLES `bbs_admin` WRITE;
/*!40000 ALTER TABLE `bbs_admin` DISABLE KEYS */;
INSERT INTO `bbs_admin` VALUES (1,'공지사항','bbs',1,15,15,2,99,1,0,1,0,0,NULL,NULL),(2,'자유게시판','bbs',1,15,15,0,0,-1,-1,0,0,0,NULL,NULL);
/*!40000 ALTER TABLE `bbs_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bbsid` int DEFAULT NULL,
  `categorytext` varchar(45) DEFAULT NULL,
  `categorynum` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,1,'공지',1),(2,1,'긴급',2),(3,1,'이벤트',3),(4,1,'테스트',4),(6,2,'수다떨기',1),(7,2,'가입인사',2),(9,2,'인사말씀',4),(10,2,'진실',3);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userid` varchar(45) NOT NULL,
  `userpass` varchar(120) NOT NULL,
  `username` varchar(45) NOT NULL,
  `useremail` varchar(100) NOT NULL,
  `usertel` varchar(20) DEFAULT NULL,
  `zipcode` int DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `detail_address` varchar(255) DEFAULT NULL,
  `extra_address` varchar(100) DEFAULT NULL,
  `userimg` varchar(255) DEFAULT NULL,
  `oruserimg` varchar(255) DEFAULT NULL,
  `userprofile` text,
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `edit_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `userip` varchar(20) DEFAULT NULL,
  `grade` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES (1,'admin','$2a$10$YvIjjV9TcDrlo7QrsGpKf.e6wIql3Epdrvk/PIKkWkymEh4OUBQ9m','관리자','webmaster@email.com','010-1234-5454',0,'','','','1728024366487-members.png','avatar.png','관리자입니다.','2024-10-04 15:46:07','2024-10-04 15:46:07','0:0:0:0:0:0:0:1',99),(2,'guest','$2a$10$6tWLip4F0GELIV2cWYXTcOZeAAtZ0yTyl2goMqwSL.Q1ebC3G3EWK','일반회원1','webmaster@email.com','010-1234-5453',10091,'경기 김포시 김포대로 1147-63','123',' (장기동) 아파트','1728024490011-members.png','coconut-drink.png','손님입니다.','2024-10-04 15:48:10','2024-10-04 15:48:10','0:0:0:0:0:0:0:1',0),(3,'gues3','$2a$10$KNYeUOvHSiTvimOwa/IwpO9EdQ1aA4BHqbVNz9VsPf1culK4QTQ22','게시트3','eami3@email.com','010-1234-5453',0,'','','',NULL,NULL,'','2024-10-10 16:13:11','2024-10-10 16:13:11','0:0:0:0:0:0:0:1',0),(4,'guest4','$2a$10$ckIHyWyGazdyftaZUy1V.uzLtzTg2Kfv2P3WycdK1xBASfHAxatBm','게시트4','eami3@email.com','010-1234-5454',0,'','','',NULL,NULL,'','2024-10-10 17:11:09','2024-10-10 17:11:09','0:0:0:0:0:0:0:1',0),(5,'guest5','$2a$10$hmE.P9CYKjvE63BhvZ74u.XJUC6qcz3MTowoK2X3NMfuV8Nt5n7GS','게시트5','webmaster3@email.com','010-1234-5454',0,'','','',NULL,NULL,'','2024-10-10 17:22:51','2024-10-10 17:22:51','0:0:0:0:0:0:0:1',0);
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members_role`
--

DROP TABLE IF EXISTS `members_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `membersid` int DEFAULT NULL,
  `user_role` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members_role`
--

LOCK TABLES `members_role` WRITE;
/*!40000 ALTER TABLE `members_role` DISABLE KEYS */;
INSERT INTO `members_role` VALUES (1,1,'ROLE_ADMIN'),(2,2,'ROLE_USER'),(3,3,'ROLE_USER'),(4,4,'ROLE_USER'),(5,5,'ROLE_USER');
/*!40000 ALTER TABLE `members_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-25 15:52:46
