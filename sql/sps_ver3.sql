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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `content` varchar(200) NOT NULL,
  `write_time` datetime NOT NULL,
  `choice` tinyint(4) NOT NULL DEFAULT '0' COMMENT '원게시글의 유형이 프로젝트 내부-이슈인 경우',
  PRIMARY KEY (`comment_id`),
  KEY `fk_COMMENT_USER1_idx` (`user_id`),
  KEY `fk_COMMENT_POST1_idx` (`post_id`),
  CONSTRAINT `fk_COMMENT_POST1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_COMMENT_USER1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common`
--

DROP TABLE IF EXISTS `common`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `common` (
  `cmn_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '공통코드 아이디',
  `cmn_name` varchar(45) NOT NULL COMMENT '공통 코드 이름\n예시)\n학부/학과 -1\n사용언어-2\n게시글유형 -3\n사용자유형 -4\n과목 -5',
  PRIMARY KEY (`cmn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common`
--

LOCK TABLES `common` WRITE;
/*!40000 ALTER TABLE `common` DISABLE KEYS */;
/*!40000 ALTER TABLE `common` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail`
--

DROP TABLE IF EXISTS `detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `detail` (
  `det_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '공통-상세 아이디\n',
  `cmn_id` int(11) NOT NULL COMMENT '공통코드 외래키',
  `cmn_det_id` int(11) NOT NULL COMMENT '공통코드의 상세코드',
  `det_name` varchar(45) NOT NULL COMMENT '상세정보명',
  PRIMARY KEY (`det_id`),
  KEY `fk_common_idx` (`cmn_id`),
  CONSTRAINT `fk_common` FOREIGN KEY (`cmn_id`) REFERENCES `common` (`cmn_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail`
--

LOCK TABLES `detail` WRITE;
/*!40000 ALTER TABLE `detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `file` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL COMMENT '제출물인 경우 프로젝트 게시글과 연결됨\n일반파일의 경우 일반 게시글과 연결됨\n',
  `name` varchar(1000) NOT NULL,
  `submit_time` datetime NOT NULL,
  PRIMARY KEY (`file_id`),
  KEY `fk_FILE_POST1_idx` (`post_id`),
  CONSTRAINT `fk_FILE_POST1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `post` (
  `post_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `write_time` datetime NOT NULL,
  `det_type_id` int(11) NOT NULL COMMENT '게시글 유형',
  `deadline_time` datetime DEFAULT NULL COMMENT '유형이 공지인 경우',
  `extention_time` datetime DEFAULT NULL COMMENT '유형이 공지인 경우',
  `project_id` int(11) DEFAULT NULL COMMENT '유형이 프로젝트 내부인 경우',
  PRIMARY KEY (`post_id`),
  KEY `fk_POST_USER1_idx` (`user_id`),
  KEY `fk_POST_DETAIL1_idx` (`det_type_id`),
  KEY `fk_POST_PROJECT1_idx` (`project_id`),
  CONSTRAINT `fk_POST_DETAIL1` FOREIGN KEY (`det_type_id`) REFERENCES `detail` (`det_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_POST_PROJECT1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_POST_USER1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `post_like` (
  `like_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  PRIMARY KEY (`like_id`),
  KEY `fk_POST_LIKE_USER1_idx` (`user_id`),
  KEY `fk_POST_LIKE_POST1_idx` (`post_id`),
  CONSTRAINT `fk_POST_LIKE_POST1` FOREIGN KEY (`post_id`) REFERENCES `post` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_POST_LIKE_USER1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_like`
--

LOCK TABLES `post_like` WRITE;
/*!40000 ALTER TABLE `post_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `project` (
  `project_id` int(11) NOT NULL AUTO_INCREMENT,
  `cap_id` int(11) NOT NULL COMMENT '프로젝트 대표 학생 학번',
  `project_name` varchar(45) NOT NULL COMMENT '프로젝트이름',
  `start_date` datetime NOT NULL COMMENT '프로젝트 생성 날짜',
  `prog_state` tinyint(4) NOT NULL COMMENT '진행중/끝남',
  `rcrt_state` tinyint(4) NOT NULL COMMENT '모집중/모집안함',
  `det_language_id` int(11) NOT NULL COMMENT '사용언어 여러개',
  `contest` tinyint(1) NOT NULL DEFAULT '0' COMMENT '경진대회 참여 여부',
  `pin` tinyint(4) NOT NULL DEFAULT '0' COMMENT '고정한 프로젝트 여부',
  `github` varchar(45) DEFAULT NULL,
  `det_subject_id` int(11) DEFAULT NULL COMMENT '과목 프로젝트\n디테일에 과목 없음 추가',
  `year` int(11) DEFAULT NULL COMMENT '경진대회 출품작-참여연도',
  `semester` int(11) DEFAULT NULL COMMENT '경진대회 출품작-참여학기',
  PRIMARY KEY (`project_id`),
  KEY `fk_PROJECT_DETAIL1_idx` (`det_subject_id`),
  KEY `fk_PROJECT_DETAIL2_idx` (`det_language_id`),
  KEY `fk_PROJECT_USER1_idx` (`cap_id`),
  CONSTRAINT `fk_PROJECT_DETAIL1` FOREIGN KEY (`det_subject_id`) REFERENCES `detail` (`det_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_PROJECT_DETAIL2` FOREIGN KEY (`det_language_id`) REFERENCES `detail` (`det_id`),
  CONSTRAINT `fk_PROJECT_USER1` FOREIGN KEY (`cap_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_join`
--

DROP TABLE IF EXISTS `project_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `project_join` (
  `join_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `project_id` int(11) NOT NULL,
  `join_time` datetime NOT NULL,
  `state` int(11) NOT NULL COMMENT '참여자 상태\n대기/승인/거절',
  `color` varchar(45) NOT NULL DEFAULT '#000000' COMMENT '조원별 투두리스트 배경색',
  PRIMARY KEY (`join_id`),
  KEY `fk_PROJECT_JOIN_USER1_idx` (`user_id`),
  KEY `fk_PROJECT_JOIN_PROJECT1_idx` (`project_id`),
  CONSTRAINT `fk_PROJECT_JOIN_PROJECT1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_PROJECT_JOIN_USER1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_join`
--

LOCK TABLES `project_join` WRITE;
/*!40000 ALTER TABLE `project_join` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_join` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_star`
--

DROP TABLE IF EXISTS `project_star`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `project_star` (
  `star_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '프로젝트 좋아요',
  `user_id` int(11) NOT NULL,
  `project_id` int(11) NOT NULL,
  PRIMARY KEY (`star_id`),
  KEY `fk_PROJECT_STAR_USER1_idx` (`user_id`),
  KEY `fk_PROJECT_STAR_PROJECT1_idx` (`project_id`),
  CONSTRAINT `fk_PROJECT_STAR_PROJECT1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_PROJECT_STAR_USER1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_star`
--

LOCK TABLES `project_star` WRITE;
/*!40000 ALTER TABLE `project_star` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_star` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `score` (
  `score_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `comment` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`score_id`),
  KEY `fk_SCORE_USER1_idx` (`user_id`),
  KEY `fk_SCORE_POST1_idx` (`post_id`),
  CONSTRAINT `fk_SCORE_POST1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_SCORE_USER1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todo`
--

DROP TABLE IF EXISTS `todo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `todo` (
  `todo_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `weekly_id` int(11) NOT NULL,
  `detail` varchar(200) NOT NULL COMMENT '목표 상세\n',
  `prog_state` int(11) NOT NULL COMMENT '시작 안함/진행중/완료\n',
  `create_time` datetime NOT NULL COMMENT '생성날짜',
  PRIMARY KEY (`todo_id`),
  KEY `fk_TODO_USER1_idx` (`user_id`),
  KEY `fk_TODO_WEEKLY1_idx` (`weekly_id`),
  CONSTRAINT `fk_TODO_USER1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `fk_TODO_WEEKLY1` FOREIGN KEY (`weekly_id`) REFERENCES `weekly` (`weekly_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todo`
--

LOCK TABLES `todo` WRITE;
/*!40000 ALTER TABLE `todo` DISABLE KEYS */;
/*!40000 ALTER TABLE `todo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_num` int(11) NOT NULL COMMENT '학생-학번\n교수-교번',
  `name` varchar(45) NOT NULL COMMENT '학생 이름',
  `grade` int(11) NOT NULL COMMENT '학생 학년(직접 지정)',
  `email` varchar(45) NOT NULL COMMENT '인증용 이메일',
  `password` varchar(45) NOT NULL COMMENT '1차 릴리즈: 디비에 저장',
  `timeline_time` datetime NOT NULL COMMENT '타임라인 확인시간\n초기값은 유저 생성 시간',
  `det_dept_id` int(11) DEFAULT NULL COMMENT '학과 상세 id',
  `user_type` tinyint(1) NOT NULL COMMENT '교수/학생 구분',
  `github` varchar(200) DEFAULT NULL,
  `intro` varchar(200) DEFAULT NULL,
  `det_language_id` int(11) DEFAULT NULL,
  `email_check` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `사용자고유번호_UNIQUE` (`user_num`),
  UNIQUE KEY `이메일_UNIQUE` (`email`),
  KEY `fk_USER_DETAIL1_idx` (`det_dept_id`),
  KEY `fk_USER_DETAIL2_idx` (`det_language_id`),
  CONSTRAINT `fk_USER_DETAIL1` FOREIGN KEY (`det_dept_id`) REFERENCES `detail` (`det_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_USER_DETAIL2` FOREIGN KEY (`det_language_id`) REFERENCES `detail` (`det_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weekly`
--

DROP TABLE IF EXISTS `weekly`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `weekly` (
  `weekly_id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `start_time` datetime NOT NULL COMMENT '********\n진행중인 목표가 있을 때 새 목표 생성 시 현재 진행중인 목표 종료일의 다음날로부터 7일간의 목표 생성\n진행중인 목표가 없을 때 새 목표 생성 시 시작 날짜를 기준으로 7일간의 목표 생성',
  `detail` varchar(200) DEFAULT NULL COMMENT '목표 상세',
  `week` int(11) NOT NULL COMMENT '몇주차의 목표인지',
  PRIMARY KEY (`weekly_id`),
  KEY `fk_WEEKLY_PROJECT1_idx` (`project_id`),
  CONSTRAINT `fk_WEEKLY_PROJECT1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weekly`
--

LOCK TABLES `weekly` WRITE;
/*!40000 ALTER TABLE `weekly` DISABLE KEYS */;
/*!40000 ALTER TABLE `weekly` ENABLE KEYS */;
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

-- Dump completed on 2020-04-24 21:43:02
