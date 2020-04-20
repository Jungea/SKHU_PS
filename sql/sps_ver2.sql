-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: sps
-- ------------------------------------------------------
-- Server version	8.0.13

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
-- Table structure for table `it경진대회출품작`
--

DROP TABLE IF EXISTS `it경진대회출품작`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `it경진대회출품작` (
  `출품작id` int(11) NOT NULL AUTO_INCREMENT,
  `프로젝트id` int(11) NOT NULL,
  `출품 학기` varchar(45) NOT NULL,
  PRIMARY KEY (`출품작id`),
  KEY `프로젝트id_idx` (`프로젝트id`),
  CONSTRAINT `프로젝트id` FOREIGN KEY (`프로젝트id`) REFERENCES `프로젝트` (`프로젝트id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `it경진대회출품작`
--

LOCK TABLES `it경진대회출품작` WRITE;
/*!40000 ALTER TABLE `it경진대회출품작` DISABLE KEYS */;
/*!40000 ALTER TABLE `it경진대회출품작` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todolist`
--

DROP TABLE IF EXISTS `todolist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `todolist` (
  `목표id` int(11) NOT NULL AUTO_INCREMENT,
  `수행자id` int(11) NOT NULL,
  `주차별목표id` int(11) NOT NULL,
  `목표상세` varchar(45) NOT NULL COMMENT '목표 상세',
  `상태` int(11) NOT NULL COMMENT '시작 안함/진행중/완료\n',
  `시간` datetime NOT NULL,
  PRIMARY KEY (`목표id`),
  KEY `수행자fk_idx` (`수행자id`),
  KEY `주차별목표fk_idx` (`주차별목표id`),
  CONSTRAINT `수행자fk` FOREIGN KEY (`수행자id`) REFERENCES `학생` (`학생id`),
  CONSTRAINT `주차별목표fk` FOREIGN KEY (`주차별목표id`) REFERENCES `주차별목표` (`주차별목표id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todolist`
--

LOCK TABLES `todolist` WRITE;
/*!40000 ALTER TABLE `todolist` DISABLE KEYS */;
/*!40000 ALTER TABLE `todolist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `게시글 즐겨찾기`
--

DROP TABLE IF EXISTS `게시글 즐겨찾기`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `게시글 즐겨찾기` (
  `게시글즐찾id` int(11) NOT NULL AUTO_INCREMENT,
  `즐찾한사람id` int(11) NOT NULL,
  `즐찾된게시글id` int(11) NOT NULL,
  PRIMARY KEY (`게시글즐찾id`),
  KEY `즐찾한사람id_idx` (`즐찾한사람id`),
  KEY `즐찾된글id_idx` (`즐찾된게시글id`),
  CONSTRAINT `즐찾된글id` FOREIGN KEY (`즐찾된게시글id`) REFERENCES `일반 게시글` (`일반게시글id`),
  CONSTRAINT `즐찾한사람id` FOREIGN KEY (`즐찾한사람id`) REFERENCES `학생` (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `게시글 즐겨찾기`
--

LOCK TABLES `게시글 즐겨찾기` WRITE;
/*!40000 ALTER TABLE `게시글 즐겨찾기` DISABLE KEYS */;
/*!40000 ALTER TABLE `게시글 즐겨찾기` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `과목`
--

DROP TABLE IF EXISTS `과목`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `과목` (
  `과목id` int(11) NOT NULL AUTO_INCREMENT,
  `교수id` int(11) NOT NULL,
  `상태` tinyint(4) NOT NULL COMMENT '강의중/종강됨\n',
  PRIMARY KEY (`과목id`),
  KEY `교수fk_idx` (`교수id`),
  CONSTRAINT `교수fk` FOREIGN KEY (`교수id`) REFERENCES `교수` (`교수id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `과목`
--

LOCK TABLES `과목` WRITE;
/*!40000 ALTER TABLE `과목` DISABLE KEYS */;
/*!40000 ALTER TABLE `과목` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `과목-프로젝트`
--

DROP TABLE IF EXISTS `과목-프로젝트`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `과목-프로젝트` (
  `과목-프로젝트id` int(11) NOT NULL AUTO_INCREMENT,
  `과목id` int(11) NOT NULL,
  `프로젝트 id` int(11) NOT NULL,
  PRIMARY KEY (`과목-프로젝트id`),
  KEY `과목fk_idx` (`과목id`),
  KEY `플젝fk_idx` (`프로젝트 id`),
  CONSTRAINT `과목fk` FOREIGN KEY (`과목id`) REFERENCES `과목` (`과목id`),
  CONSTRAINT `플젝fkA` FOREIGN KEY (`프로젝트 id`) REFERENCES `프로젝트` (`프로젝트id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `과목-프로젝트`
--

LOCK TABLES `과목-프로젝트` WRITE;
/*!40000 ALTER TABLE `과목-프로젝트` DISABLE KEYS */;
/*!40000 ALTER TABLE `과목-프로젝트` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `과목공지`
--

DROP TABLE IF EXISTS `과목공지`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `과목공지` (
  `공지id` int(11) NOT NULL AUTO_INCREMENT,
  `제목` varchar(45) NOT NULL,
  `작성자id` int(11) NOT NULL,
  `과목id` int(11) NOT NULL,
  `내용` varchar(1000) NOT NULL,
  `작성시간` datetime NOT NULL,
  PRIMARY KEY (`공지id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `과목공지`
--

LOCK TABLES `과목공지` WRITE;
/*!40000 ALTER TABLE `과목공지` DISABLE KEYS */;
/*!40000 ALTER TABLE `과목공지` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `교수`
--

DROP TABLE IF EXISTS `교수`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `교수` (
  `교수id` int(11) NOT NULL AUTO_INCREMENT,
  `교번` int(11) NOT NULL,
  `이름` varchar(45) NOT NULL,
  `이메일` varchar(45) NOT NULL,
  `비밀번호` varchar(45) NOT NULL,
  `깃허브주소` varchar(45) DEFAULT NULL,
  `한 줄 소개` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`교수id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `교수`
--

LOCK TABLES `교수` WRITE;
/*!40000 ALTER TABLE `교수` DISABLE KEYS */;
/*!40000 ALTER TABLE `교수` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `사용언어`
--

DROP TABLE IF EXISTS `사용언어`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `사용언어` (
  `사용언어id` int(11) NOT NULL AUTO_INCREMENT COMMENT '사용언어 여러개일때 만들 임시 테이블',
  `언어명` varchar(45) NOT NULL COMMENT '백준에서 지원하는 언어 기준',
  PRIMARY KEY (`사용언어id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `사용언어`
--

LOCK TABLES `사용언어` WRITE;
/*!40000 ALTER TABLE `사용언어` DISABLE KEYS */;
/*!40000 ALTER TABLE `사용언어` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `일반 게시글`
--

DROP TABLE IF EXISTS `일반 게시글`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `일반 게시글` (
  `일반게시글id` int(11) NOT NULL AUTO_INCREMENT,
  `게시자id` int(11) NOT NULL,
  `제목` varchar(45) NOT NULL,
  `내용` varchar(1000) NOT NULL,
  `작성시간` datetime NOT NULL,
  PRIMARY KEY (`일반게시글id`),
  KEY `게시자id_idx` (`게시자id`),
  CONSTRAINT `게시자fkA` FOREIGN KEY (`게시자id`) REFERENCES `학생` (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `일반 게시글`
--

LOCK TABLES `일반 게시글` WRITE;
/*!40000 ALTER TABLE `일반 게시글` DISABLE KEYS */;
/*!40000 ALTER TABLE `일반 게시글` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `일반 댓글`
--

DROP TABLE IF EXISTS `일반 댓글`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `일반 댓글` (
  `댓글id` int(11) NOT NULL AUTO_INCREMENT,
  `작성자id` int(11) NOT NULL,
  `내용` varchar(200) NOT NULL,
  `작성시간` datetime NOT NULL,
  `원글ｉｄ` int(11) NOT NULL,
  PRIMARY KEY (`댓글id`),
  KEY `작성자id_idx` (`작성자id`),
  KEY `원글id_idx` (`원글ｉｄ`),
  CONSTRAINT `원글id` FOREIGN KEY (`원글ｉｄ`) REFERENCES `일반 게시글` (`일반게시글id`),
  CONSTRAINT `작성자id` FOREIGN KEY (`작성자id`) REFERENCES `학생` (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `일반 댓글`
--

LOCK TABLES `일반 댓글` WRITE;
/*!40000 ALTER TABLE `일반 댓글` DISABLE KEYS */;
/*!40000 ALTER TABLE `일반 댓글` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `점수`
--

DROP TABLE IF EXISTS `점수`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `점수` (
  `점수id` int(11) NOT NULL AUTO_INCREMENT,
  `게시글id` int(11) NOT NULL,
  PRIMARY KEY (`점수id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `점수`
--

LOCK TABLES `점수` WRITE;
/*!40000 ALTER TABLE `점수` DISABLE KEYS */;
/*!40000 ALTER TABLE `점수` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `주차별목표`
--

DROP TABLE IF EXISTS `주차별목표`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `주차별목표` (
  `주차별목표id` int(11) NOT NULL AUTO_INCREMENT,
  `프로젝트id` int(11) NOT NULL,
  `시작날짜` datetime NOT NULL COMMENT '********\n진행중인 목표가 있을 때 새 목표 생성 시 현재 진행중인 목표 종료일의 다음날로부터 7일간의 목표 생성\n진행중인 목표가 없을 때 새 목표 생성 시 시작 날짜를 기준으로 7일간의 목표 생성',
  `목표상세` varchar(45) NOT NULL,
  `주차` int(11) NOT NULL COMMENT '몇주차의 목표인지',
  PRIMARY KEY (`주차별목표id`),
  KEY `프로젝트fk_idx` (`프로젝트id`),
  CONSTRAINT `프로젝트fkB` FOREIGN KEY (`프로젝트id`) REFERENCES `프로젝트` (`프로젝트id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `주차별목표`
--

LOCK TABLES `주차별목표` WRITE;
/*!40000 ALTER TABLE `주차별목표` DISABLE KEYS */;
/*!40000 ALTER TABLE `주차별목표` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `참여신청`
--

DROP TABLE IF EXISTS `참여신청`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `참여신청` (
  `참여신청id` int(11) NOT NULL AUTO_INCREMENT,
  `신청자id` int(11) NOT NULL,
  `프로젝트id` int(11) NOT NULL,
  `상태` int(11) NOT NULL COMMENT '대기/승인/거절',
  PRIMARY KEY (`참여신청id`),
  KEY `신청자fk_idx` (`신청자id`),
  KEY `프로젝트fk_idx` (`프로젝트id`),
  CONSTRAINT `신청자fk` FOREIGN KEY (`신청자id`) REFERENCES `학생` (`학생id`),
  CONSTRAINT `프로젝트fkC` FOREIGN KEY (`프로젝트id`) REFERENCES `프로젝트` (`프로젝트id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `참여신청`
--

LOCK TABLES `참여신청` WRITE;
/*!40000 ALTER TABLE `참여신청` DISABLE KEYS */;
/*!40000 ALTER TABLE `참여신청` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `파일`
--

DROP TABLE IF EXISTS `파일`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `파일` (
  `파일id` int(11) NOT NULL,
  `파일명` varchar(45) NOT NULL,
  `제출자id` int(11) NOT NULL,
  `제출게시글 id` int(11) NOT NULL COMMENT '제출물인 경우 프로젝트 게시글과 연결됨\n일반파일의 경우 일반 게시글과 연결됨\n',
  `제출시간` datetime NOT NULL,
  `일반게시글id` int(11) NOT NULL COMMENT '제출물인 경우 프로젝트 게시글과 연결됨\n일반파일의 경우 일반 게시글과 연결됨',
  PRIMARY KEY (`파일id`),
  KEY `제출자fk_idx` (`제출자id`),
  KEY `게시글fk_idx` (`제출게시글 id`),
  KEY `일반게시글fk_idx` (`일반게시글id`),
  CONSTRAINT `일반게시글fk` FOREIGN KEY (`일반게시글id`) REFERENCES `일반 게시글` (`일반게시글id`),
  CONSTRAINT `제출자fk` FOREIGN KEY (`제출자id`) REFERENCES `학생` (`학생id`),
  CONSTRAINT `플젝게시글fk` FOREIGN KEY (`제출게시글 id`) REFERENCES `프로젝트 게시글` (`게시글id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `파일`
--

LOCK TABLES `파일` WRITE;
/*!40000 ALTER TABLE `파일` DISABLE KEYS */;
/*!40000 ALTER TABLE `파일` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `프로젝트`
--

DROP TABLE IF EXISTS `프로젝트`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `프로젝트` (
  `프로젝트id` int(11) NOT NULL AUTO_INCREMENT,
  `프로젝트명` varchar(45) NOT NULL COMMENT '프로젝트이름',
  `조장(대표)` int(11) NOT NULL COMMENT '프로젝트 대표 학생 학번',
  `생성날짜` datetime NOT NULL COMMENT '프로젝트 생성 날짜',
  `진행상태` tinyint(4) NOT NULL COMMENT '진행중/끝남',
  `모집여부` tinyint(4) NOT NULL COMMENT '모집중/모집안함',
  `사용언어` varchar(45) NOT NULL COMMENT '사용언어 여러개',
  `레포지토리주소` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`프로젝트id`),
  KEY `조장fk_idx` (`조장(대표)`),
  CONSTRAINT `조장fk` FOREIGN KEY (`조장(대표)`) REFERENCES `학생` (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `프로젝트`
--

LOCK TABLES `프로젝트` WRITE;
/*!40000 ALTER TABLE `프로젝트` DISABLE KEYS */;
/*!40000 ALTER TABLE `프로젝트` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `프로젝트 게시글`
--

DROP TABLE IF EXISTS `프로젝트 게시글`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `프로젝트 게시글` (
  `게시글id` int(11) NOT NULL AUTO_INCREMENT,
  `프로젝트id` int(11) NOT NULL,
  `게시자id` int(11) NOT NULL,
  `게시글 유형` int(11) NOT NULL COMMENT '토론/자유/공지\n',
  `제목` varchar(45) NOT NULL,
  `내용` varchar(1000) NOT NULL,
  `작성시간` datetime NOT NULL,
  `주차설정` int(11) NOT NULL COMMENT '유형이 공지일 때만 값이 들어감\n1주차~n주차로 표시',
  `마감일` datetime NOT NULL COMMENT '유형이 공지일 때만 값이 들어감',
  `연장기한` datetime NOT NULL COMMENT '유형이 공지일 때만 값이 들어감',
  PRIMARY KEY (`게시글id`),
  KEY `게시자fk_idx` (`게시자id`),
  KEY `프로젝트fk_idx` (`프로젝트id`),
  CONSTRAINT `게시자fk` FOREIGN KEY (`게시자id`) REFERENCES `학생` (`학생id`),
  CONSTRAINT `프로젝트fkD` FOREIGN KEY (`프로젝트id`) REFERENCES `프로젝트` (`프로젝트id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `프로젝트 게시글`
--

LOCK TABLES `프로젝트 게시글` WRITE;
/*!40000 ALTER TABLE `프로젝트 게시글` DISABLE KEYS */;
/*!40000 ALTER TABLE `프로젝트 게시글` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `프로젝트 댓글`
--

DROP TABLE IF EXISTS `프로젝트 댓글`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `프로젝트 댓글` (
  `댓글id` int(11) NOT NULL AUTO_INCREMENT,
  `작성자id` int(11) NOT NULL,
  `원게시글id` int(11) NOT NULL,
  `내용` varchar(45) NOT NULL,
  `채택여부` tinyint(4) NOT NULL COMMENT '댓글이 채택이 됐으면 1',
  `작성시간` datetime NOT NULL,
  PRIMARY KEY (`댓글id`),
  KEY `작성자fk_idx` (`작성자id`),
  KEY `원게시글fk_idx` (`원게시글id`),
  CONSTRAINT `원게시글fk` FOREIGN KEY (`원게시글id`) REFERENCES `프로젝트 게시글` (`게시글id`),
  CONSTRAINT `작성자fk` FOREIGN KEY (`작성자id`) REFERENCES `학생` (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `프로젝트 댓글`
--

LOCK TABLES `프로젝트 댓글` WRITE;
/*!40000 ALTER TABLE `프로젝트 댓글` DISABLE KEYS */;
/*!40000 ALTER TABLE `프로젝트 댓글` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `프로젝트-사용언어`
--

DROP TABLE IF EXISTS `프로젝트-사용언어`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `프로젝트-사용언어` (
  `프로젝트언어id` int(11) NOT NULL AUTO_INCREMENT,
  `프로젝트id` int(11) NOT NULL,
  `사용언어id` int(11) NOT NULL,
  PRIMARY KEY (`프로젝트언어id`),
  KEY `사용언어fk_idx` (`사용언어id`),
  KEY `프로젝트fk_idx` (`프로젝트id`),
  CONSTRAINT `사용언어fk` FOREIGN KEY (`사용언어id`) REFERENCES `사용언어` (`사용언어id`),
  CONSTRAINT `프로젝트fkA` FOREIGN KEY (`프로젝트id`) REFERENCES `프로젝트` (`프로젝트id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `프로젝트-사용언어`
--

LOCK TABLES `프로젝트-사용언어` WRITE;
/*!40000 ALTER TABLE `프로젝트-사용언어` DISABLE KEYS */;
/*!40000 ALTER TABLE `프로젝트-사용언어` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `프로젝트-즐겨찾기`
--

DROP TABLE IF EXISTS `프로젝트-즐겨찾기`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `프로젝트-즐겨찾기` (
  `즐겨찾기id` int(11) NOT NULL AUTO_INCREMENT,
  `즐찾한학생id` int(11) NOT NULL,
  `즐찾된플젝id` int(11) NOT NULL,
  PRIMARY KEY (`즐겨찾기id`),
  KEY `즐찾학생fk_idx` (`즐찾한학생id`),
  KEY `즐찾플젝fk_idx` (`즐찾된플젝id`),
  CONSTRAINT `즐찾플젝fk` FOREIGN KEY (`즐찾된플젝id`) REFERENCES `프로젝트` (`프로젝트id`),
  CONSTRAINT `즐찾학생fk` FOREIGN KEY (`즐찾한학생id`) REFERENCES `학생` (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `프로젝트-즐겨찾기`
--

LOCK TABLES `프로젝트-즐겨찾기` WRITE;
/*!40000 ALTER TABLE `프로젝트-즐겨찾기` DISABLE KEYS */;
/*!40000 ALTER TABLE `프로젝트-즐겨찾기` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `프로젝트참여`
--

DROP TABLE IF EXISTS `프로젝트참여`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `프로젝트참여` (
  `참여id` int(11) NOT NULL AUTO_INCREMENT,
  `참여자id` int(11) NOT NULL,
  `프로젝트id` int(11) NOT NULL,
  `참여 시간` datetime NOT NULL,
  PRIMARY KEY (`참여id`),
  KEY `학생fk_idx` (`참여자id`),
  KEY `플젝fk_idx` (`프로젝트id`),
  CONSTRAINT `플젝fkB` FOREIGN KEY (`프로젝트id`) REFERENCES `프로젝트` (`프로젝트id`),
  CONSTRAINT `학생fkB` FOREIGN KEY (`참여자id`) REFERENCES `학생` (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `프로젝트참여`
--

LOCK TABLES `프로젝트참여` WRITE;
/*!40000 ALTER TABLE `프로젝트참여` DISABLE KEYS */;
/*!40000 ALTER TABLE `프로젝트참여` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `학생`
--

DROP TABLE IF EXISTS `학생`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `학생` (
  `학생id` int(11) NOT NULL AUTO_INCREMENT,
  `학번` int(11) NOT NULL,
  `이름` varchar(45) NOT NULL COMMENT '학생 이름',
  `학년` int(11) NOT NULL COMMENT '학생 학년(직접 지정)',
  `이메일` varchar(45) NOT NULL COMMENT '인증용 이메일',
  `비밀번호` varchar(45) NOT NULL COMMENT '1차 릴리즈: 디비에 저장',
  `타임라인확인시간` datetime NOT NULL,
  `학부/학과` varchar(45) DEFAULT NULL COMMENT '후에 테이블을 만들 수도 있음',
  `깃허브주소` varchar(45) DEFAULT NULL,
  `한 줄 소개` varchar(45) DEFAULT NULL,
  `주요언어` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`학생id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `학생`
--

LOCK TABLES `학생` WRITE;
/*!40000 ALTER TABLE `학생` DISABLE KEYS */;
/*!40000 ALTER TABLE `학생` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'sps'
--

--
-- Dumping routines for database 'sps'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-20 22:04:40
