CREATE DATABASE  IF NOT EXISTS `prod` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `prod`;
-- MySQL dump 10.13  Distrib 8.0.42, for macos15 (x86_64)
--
-- Host: labo-db.cj600i0q0va6.eu-west-3.rds.amazonaws.com    Database: prod
-- ------------------------------------------------------
-- Server version	8.0.42

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `bookmarks_listings`
--

DROP TABLE IF EXISTS `bookmarks_listings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmarks_listings` (
  `user_id` int NOT NULL,
  `trip_id` int NOT NULL,
  KEY `FKsn381t6nuwvrupc9cp7wfr82m` (`trip_id`),
  KEY `FK7n43ypb6wr3bkfe97wisner07` (`user_id`),
  CONSTRAINT `FK7n43ypb6wr3bkfe97wisner07` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKsn381t6nuwvrupc9cp7wfr82m` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmarks_listings`
--

LOCK TABLES `bookmarks_listings` WRITE;
/*!40000 ALTER TABLE `bookmarks_listings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookmarks_listings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_info`
--

DROP TABLE IF EXISTS `card_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_info` (
  `card_info_id` int NOT NULL AUTO_INCREMENT,
  `card_holder` varchar(255) NOT NULL,
  `card_number` varchar(255) NOT NULL,
  `secret_number` int NOT NULL,
  PRIMARY KEY (`card_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_info`
--

LOCK TABLES `card_info` WRITE;
/*!40000 ALTER TABLE `card_info` DISABLE KEYS */;
INSERT INTO `card_info` VALUES (1,'M test toto','5555555555555555',123),(2,'M Test test','30203020777799',0),(3,'lobjd','',0),(4,'Toto','1234578451214646',123);
/*!40000 ALTER TABLE `card_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_attempts`
--

DROP TABLE IF EXISTS `login_attempts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_attempts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attempts` int NOT NULL,
  `locked_until` datetime(6) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKajutjg7907bedeugbud0jf7mf` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login_attempts`
--

LOCK TABLES `login_attempts` WRITE;
/*!40000 ALTER TABLE `login_attempts` DISABLE KEYS */;
INSERT INTO `login_attempts` VALUES (2,1,NULL,'username'),(4,1,NULL,'dsg'),(5,1,NULL,'vsdvsd'),(9,5,'2025-06-17 08:54:21.452234','lio_b');
/*!40000 ALTER TABLE `login_attempts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `options` (
  `optionid` int NOT NULL AUTO_INCREMENT,
  `category` enum('ACCOMMODATION','ACTIVITIES','EVENT','LUXURY','MEALS','OTHERS','PACKAGE','SERVICES','TRANSPORT','TRAVEL','WELLNESS') DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `prix` double NOT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`optionid`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
INSERT INTO `options` VALUES (1,'ACCOMMODATION','Surclassement chambre supérieure',89.99,0),(2,'ACCOMMODATION','Suite avec vue mer',150,0),(3,'ACCOMMODATION','Villa privée avec piscine',299.99,0),(4,'ACCOMMODATION','Chambre avec balcon privé',75,0),(5,'ACCOMMODATION','Appartement avec cuisine équipée',125,0),(6,'ACCOMMODATION','Bungalow sur pilotis',189.99,0),(7,'ACCOMMODATION','Riad traditionnel authentique',145,0),(8,'TRAVEL','Surclassement classe affaires',899.99,0),(9,'TRAVEL','Surclassement première classe',1599.99,0),(10,'TRAVEL','Vol direct sans escale',299.99,0),(11,'TRAVEL','Bagage supplémentaire 23kg',45,0),(12,'TRAVEL','Choix du siège prioritaire',25,0),(13,'TRAVEL','Assurance voyage premium',39.99,0),(14,'LUXURY','Chauffeur privé journée complète',189.99,0),(15,'LUXURY','Service de conciergerie 24h/24',89.99,0),(16,'LUXURY','Majordome personnel',299.99,0),(17,'LUXURY','Limousine avec chauffeur',249.99,0),(18,'LUXURY','Hélicoptère privé transfer',599.99,0),(19,'LUXURY','Yacht privé demi-journée',899.99,0),(20,'EVENT','Dîner spectacle folklorique',79.99,0),(21,'EVENT','Concert de musique classique',65,0),(22,'EVENT','Soirée gala avec animation',125,0),(23,'EVENT','Participation festival local',55,0),(24,'EVENT','Célébration anniversaire privée',189.99,0),(25,'EVENT','Cérémonie traditionnelle locale',68,0),(26,'MEALS','Petit-déjeuner continental inclus',25,0),(27,'MEALS','Demi-pension (petit-déjeuner + dîner)',65,0),(28,'MEALS','Pension complète (tous repas inclus)',95,0),(29,'MEALS','Dîner gastronomique restaurant étoilé',165,0),(30,'MEALS','Dégustation de vins locaux',48,0),(31,'MEALS','Barbecue sur la plage au coucher du soleil',72,0),(32,'MEALS','Pique-nique gourmet',28,0),(33,'MEALS','Cours de cuisine locale',68,0),(34,'MEALS','Brunch dominical illimité',42,0),(35,'WELLNESS','Accès spa et centre de bien-être',45,0),(36,'WELLNESS','Massage relaxant (1h)',79.99,0),(37,'WELLNESS','Massage aux pierres chaudes',95,0),(38,'WELLNESS','Massage en couple',159.99,0),(39,'WELLNESS','Soins du visage premium',89.99,0),(40,'WELLNESS','Hammam traditionnel',35,0),(41,'WELLNESS','Sauna finlandais privatisé',55,0),(42,'WELLNESS','Séance de yoga privée',68,0),(43,'WELLNESS','Méditation guidée au lever du soleil',25,0),(44,'TRANSPORT','Location de voiture économique',35,0),(45,'TRANSPORT','Location de voiture premium',75,0),(46,'TRANSPORT','Transfert aéroport privé',55,0),(47,'TRANSPORT','Location de moto/scooter',25,0),(48,'TRANSPORT','Vélos électriques pour la durée du séjour',45,0),(49,'TRANSPORT','Pass transport public illimité',28,0),(50,'TRANSPORT','Taxi privé disponible 24h/24',125,0),(51,'OTHERS','Wi-Fi haut débit illimité',15,0),(52,'OTHERS','Service de blanchisserie express',25,0),(53,'OTHERS','Service de bagagerie',18,0),(54,'OTHERS','Téléphone local avec données',35,0),(55,'OTHERS','Parking privé sécurisé',18,0),(56,'OTHERS','Accès lounge VIP aéroport',45,0),(57,'OTHERS','Service de réveil personnalisé',8,0),(58,'ACTIVITIES','Visite guidée de la ville historique',42,0),(59,'ACTIVITIES','Safari photo en 4x4',125,0),(60,'ACTIVITIES','Plongée sous-marine (baptême)',85,0),(61,'ACTIVITIES','Tour en hélicoptère (30min)',295,0),(62,'ACTIVITIES','Randonnée guidée en montagne',55,0),(63,'ACTIVITIES','Excursion en bateau avec déjeuner',89,0),(64,'ACTIVITIES','Vol en montgolfière au lever du soleil',189.99,0),(65,'ACTIVITIES','Kayak en eaux vives',65,0),(66,'ACTIVITIES','Parapente tandem',149.99,0),(67,'ACTIVITIES','Équitation en pleine nature',78,0),(68,'ACTIVITIES','Cours de surf avec matériel',89.99,0),(69,'SERVICES','Guide privé multilingue',199.99,0),(70,'SERVICES','Photographe professionnel (demi-journée)',149.99,0),(71,'SERVICES','Photographe professionnel (journée)',259.99,0),(72,'SERVICES','Audioguide en français',15,0),(73,'SERVICES','Traducteur personnel',125,0),(74,'SERVICES','Assistance médicale 24h/24',45,0),(75,'SERVICES','Service d\'étage 24h/24',55,0),(76,'PACKAGE','Package lune de miel romantique',299.99,0),(77,'PACKAGE','Package famille avec enfants',189.99,0),(78,'PACKAGE','Package aventure extrême',345,0),(79,'PACKAGE','Package détente et bien-être',259.99,0),(80,'PACKAGE','Package culturel et patrimoine',199.99,0),(81,'PACKAGE','Package gastronomique',245,0),(82,'PACKAGE','Package sports nautiques',189.99,0),(83,'PACKAGE','Package montagne et randonnée',165,0),(84,'PACKAGE','Package découverte urbaine',149.99,0),(85,'PACKAGE','Package photographie professionnelle',399.99,0);
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_options`
--

DROP TABLE IF EXISTS `order_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_options` (
  `order_id` int NOT NULL,
  `option_id` int NOT NULL,
  KEY `FK6dh02f3ewf22logdjykii7s1u` (`option_id`),
  KEY `FKmcvp9xr7iu4wy4xsmdw1f26sc` (`order_id`),
  CONSTRAINT `FK6dh02f3ewf22logdjykii7s1u` FOREIGN KEY (`option_id`) REFERENCES `options` (`optionid`),
  CONSTRAINT `FKmcvp9xr7iu4wy4xsmdw1f26sc` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_options`
--

LOCK TABLES `order_options` WRITE;
/*!40000 ALTER TABLE `order_options` DISABLE KEYS */;
INSERT INTO `order_options` VALUES (2,44),(2,51),(3,35),(3,37),(3,77),(10,58),(11,1),(11,14),(12,1);
/*!40000 ALTER TABLE `order_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `creation_date` date NOT NULL,
  `number_of_passenger` int NOT NULL,
  `total` double NOT NULL,
  `travel_time` date NOT NULL,
  `trip_start_date` date NOT NULL,
  `version` int NOT NULL,
  `trip_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FKsl66l3hmn2ucp1tw7l49wkuwb` (`trip_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKsl66l3hmn2ucp1tw7l49wkuwb` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (2,'2025-06-17',6,5094,'2025-07-20','2025-07-20',0,24,11),(3,'2025-06-17',4,4515.96,'2025-06-23','2025-06-23',0,24,2),(4,'2025-06-17',4,5996,'2025-07-04','2025-07-04',0,26,8),(5,'2025-06-17',1,799,'2025-06-30','2025-06-30',0,24,8),(6,'2025-06-17',6,4794,'2025-06-27','2025-06-27',0,24,11),(10,'2025-06-18',5,5705,'2025-06-22','2025-06-22',0,21,11),(11,'2025-06-18',3,3236.94,'2025-06-23','2025-06-23',0,24,11),(12,'2025-06-18',7,11822.93,'2025-06-29','2025-06-29',0,16,11),(13,'2025-06-18',3,3597,'2025-06-27','2025-06-27',0,25,13);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reset_token`
--

DROP TABLE IF EXISTS `reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reset_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `expiration` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_used` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reset_token`
--

LOCK TABLES `reset_token` WRITE;
/*!40000 ALTER TABLE `reset_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trip_options`
--

DROP TABLE IF EXISTS `trip_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trip_options` (
  `trip_id` int NOT NULL,
  `option_id` int NOT NULL,
  KEY `FKq39v0k1ilg8prtsboh7m3ci85` (`option_id`),
  KEY `FKsal2humxicdf38o1nxtunjc6q` (`trip_id`),
  CONSTRAINT `FKq39v0k1ilg8prtsboh7m3ci85` FOREIGN KEY (`option_id`) REFERENCES `options` (`optionid`),
  CONSTRAINT `FKsal2humxicdf38o1nxtunjc6q` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trip_options`
--

LOCK TABLES `trip_options` WRITE;
/*!40000 ALTER TABLE `trip_options` DISABLE KEYS */;
INSERT INTO `trip_options` VALUES (1,69),(1,27),(1,13),(2,69),(2,27),(2,13),(4,69),(4,27),(4,13),(5,69),(5,27),(5,13),(6,69),(6,27),(6,13),(7,69),(7,27),(7,13),(8,69),(8,27),(8,13),(9,69),(9,27),(9,13),(10,69),(10,27),(10,13),(11,69),(11,27),(11,13),(12,69),(12,27),(12,13),(13,69),(13,27),(13,13),(14,69),(14,27),(14,13),(15,69),(15,27),(15,13),(16,69),(16,27),(16,13),(17,69),(17,27),(17,13),(18,69),(18,27),(18,13),(19,69),(19,27),(19,13),(20,69),(20,27),(20,13),(21,69),(21,27),(21,13),(22,69),(22,27),(22,13),(23,69),(23,27),(23,13),(24,69),(24,27),(24,13),(25,69),(25,27),(25,13),(26,69),(26,27),(26,13),(27,69),(27,27),(27,13),(28,69),(28,27),(28,13),(29,69),(29,27),(29,13),(30,69),(30,27),(30,13),(31,69),(31,27),(31,13),(1,76),(1,38),(1,29),(1,2),(2,76),(2,38),(2,29),(2,2),(4,76),(4,38),(4,29),(4,2),(15,25),(14,25),(13,25),(12,25),(11,25),(10,25),(9,25),(15,33),(14,33),(13,33),(12,33),(11,33),(10,33),(9,33),(15,40),(14,40),(13,40),(12,40),(11,40),(10,40),(9,40),(15,80),(14,80),(13,80),(12,80),(11,80),(10,80),(9,80),(6,61),(6,17),(6,15),(6,8),(9,61),(9,17),(9,15),(9,8),(16,61),(16,17),(16,15),(16,8),(4,82),(4,79),(4,60),(4,19),(4,6),(20,82),(20,79),(20,60),(20,19),(20,6),(29,82),(29,79),(29,60),(29,19),(29,6),(31,82),(31,79),(31,60),(31,19),(31,6),(22,78),(22,62),(22,59),(22,45),(22,3),(26,78),(26,62),(26,59),(26,45),(26,3),(27,78),(27,62),(27,59),(27,45),(27,3);
/*!40000 ALTER TABLE `trip_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trips`
--

DROP TABLE IF EXISTS `trips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trips` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `destination_city` enum('AMSTERDAM','AUCKLAND','BANGKOK','BARCELONA','BEIJING','BERLIN','BUENOS_AIRES','CAIRO','CAPE_TOWN','CUSCO','DELHI','HANOI','LISBON','LONDON','LOS_ANGELES','MARRAKECH','MONTREAL','NAIROBI','NEW_YORK','PARIS','RIO_DE_JANEIRO','ROME','SANTIAGO','SANTORINI','SEOUL','SINGAPORE','SUVA','SYDNEY','TOKYO','TUNIS','VANCOUVER') DEFAULT NULL,
  `destination_continent` enum('AFRIQUE','AMERIQUE_DU_NORD','AMERIQUE_DU_SUD','ANTARTIQUE','ASIE','EUROPE','OCEANIE') DEFAULT NULL,
  `destination_country` enum('ARGENTINA','AUSTRALIA','BRAZIL','CANADA','CHILE','CHINA','EGYPT','FIJI','FRANCE','GERMANY','GREECE','INDIA','ITALY','JAPAN','KENYA','MOROCCO','NETHERLANDS','NEW_ZEALAND','PERU','PORTUGAL','SINGAPORE','SOUTH_AFRICA','SOUTH_KOREA','SPAIN','THAILAND','TUNISIA','UNITED_KINGDOM','USA','VIETNAM') DEFAULT NULL,
  `minimum_duration` int DEFAULT NULL,
  `unit_price` int NOT NULL,
  `version` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trips`
--

LOCK TABLES `trips` WRITE;
/*!40000 ALTER TABLE `trips` DISABLE KEYS */;
INSERT INTO `trips` VALUES (1,'Découvrez la Ville Lumière : Tour Eiffel, Louvre, Champs-Élysées. Un voyage romantique au cœur de l\'art et de la culture française.','PARIS','EUROPE','FRANCE',3,899,0),(2,'Plongez dans l\'histoire antique : Colisée, Vatican, Fontaine de Trevi. Rome vous attend avec ses trésors millénaires.','ROME','EUROPE','ITALY',4,749,0),(3,NULL,NULL,NULL,NULL,0,0,1),(4,'Couchers de soleil époustouflants, villages blancs et bleus, cuisine méditerranéenne. Santorin, la perle des Cyclades.','SANTORINI','EUROPE','GREECE',5,1299,0),(5,'Tramways jaunes, azulejos colorés, fado authentique. Lisbonne vous charme par son authenticité.','LISBON','EUROPE','PORTUGAL',4,599,0),(6,'Palais royaux, Big Ben, musées exceptionnels. Londres, capitale cosmopolite entre tradition et modernité.','LONDON','EUROPE','UNITED_KINGDOM',4,799,0),(7,'Canaux romantiques, musées d\'art, vélos partout. Amsterdam, la Venise du Nord aux mille charmes.','AMSTERDAM','EUROPE','NETHERLANDS',3,679,0),(8,'Histoire contemporaine, art de rue, vie nocturne. Berlin, métropole dynamique au passé fascinant.','BERLIN','EUROPE','GERMANY',4,589,0),(9,'Métropole futuriste entre tradition et modernité : temples, sushi, technology. Tokyo, l\'expérience japonaise ultime.','TOKYO','ASIE','JAPAN',7,1899,0),(10,'Temples dorés, marchés flottants, massage thaï. Bangkok, porte d\'entrée vers l\'Asie du Sud-Est.','BANGKOK','ASIE','THAILAND',6,1199,0),(11,'Palais moghols, épices parfumées, spiritualité intense. Delhi, voyage au cœur de l\'Inde mystique.','DELHI','ASIE','INDIA',8,999,0),(12,'Grande Muraille, Cité Interdite, canard laqué. Pékin, capitale impériale aux mille facettes.','BEIJING','ASIE','CHINA',6,1399,0),(13,'Baie d\'Halong, pho authentique, histoire coloniale. Vietnam, beauté naturelle et culturelle.','HANOI','ASIE','VIETNAM',7,899,0),(14,'K-pop, palais anciens, technologie de pointe. Séoul, modernité asiatique et traditions séculaires.','SEOUL','ASIE','SOUTH_KOREA',5,1299,0),(15,'Cité-État futuriste, jardins verticaux, fusion culinaire. Singapour, melting-pot moderne.','SINGAPORE','ASIE','SINGAPORE',4,1499,0),(16,'Big Apple : Central Park, Broadway, Statue de la Liberté. New York, la ville qui ne dort jamais.','NEW_YORK','AMERIQUE_DU_NORD','USA',5,1599,0),(17,'Charme européen en Amérique : Vieux-Montréal, poutine, festivals. Montréal, métropole bilingue.','MONTREAL','AMERIQUE_DU_NORD','CANADA',4,1099,0),(18,'Hollywood, plages de Malibu, style de vie californien. Los Angeles, ville des anges et des stars.','LOS_ANGELES','AMERIQUE_DU_NORD','USA',5,1399,0),(19,'Montagnes et océan, nature urbaine, qualité de vie. Vancouver, perle du Pacifique canadien.','VANCOUVER','AMERIQUE_DU_NORD','CANADA',5,1199,0),(20,'Christ Rédempteur, plages de Copacabana, samba. Rio, la Cidade Maravilhosa.','RIO_DE_JANEIRO','AMERIQUE_DU_SUD','BRAZIL',6,1299,0),(21,'Tango, bœuf argentin, architecture européenne. Buenos Aires, Paris de l\'Amérique du Sud.','BUENOS_AIRES','AMERIQUE_DU_SUD','ARGENTINA',5,1099,0),(22,'Machu Picchu, culture inca, altitude mystique. Pérou, aventure archéologique exceptionnelle.','CUSCO','AMERIQUE_DU_SUD','PERU',8,1599,0),(23,'Cordillère des Andes, vignobles, gastronomie. Santiago, élégance chilienne au pied des montagnes.','SANTIAGO','AMERIQUE_DU_SUD','CHILE',6,1199,0),(24,'Médina colorée, tajines savoureux, désert du Sahara. Marrakech, porte du désert.','MARRAKECH','AFRIQUE','MOROCCO',5,799,0),(25,'Pyramides de Gizeh, Nil éternel, trésors de Toutânkhamon. Égypte, berceau de la civilisation.','CAIRO','AFRIQUE','EGYPT',7,1199,0),(26,'Table Mountain, vignobles, safari Big Five. Afrique du Sud, diversité naturelle extraordinaire.','CAPE_TOWN','AFRIQUE','SOUTH_AFRICA',8,1499,0),(27,'Safari Masai Mara, migration des gnous, culture masaï. Kenya, royaume de la savane.','NAIROBI','AFRIQUE','KENYA',9,1799,0),(28,'Carthage antique, médina authentique, hospitalité tunisienne. Tunisie, carrefour méditerranéen.','TUNIS','AFRIQUE','TUNISIA',5,699,0),(29,'Opéra House, Grande Barrière de Corail, Outback. Australie, continent aux mille merveilles.','SYDNEY','OCEANIE','AUSTRALIA',10,2299,0),(30,'Fjords majestueux, All Blacks, seigneur des anneaux. Nouvelle-Zélande, nature à l\'état pur.','AUCKLAND','OCEANIE','NEW_ZEALAND',12,2499,0),(31,'Paradis tropical, récifs coralliens, culture mélanésienne. Fidji, évasion dans le Pacifique Sud.','SUVA','OCEANIE','FIJI',7,1899,0),(32,NULL,NULL,NULL,NULL,0,0,0),(36,NULL,NULL,NULL,NULL,0,0,0),(38,NULL,NULL,NULL,NULL,0,0,0);
/*!40000 ALTER TABLE `trips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','USER') DEFAULT NULL,
  `surname` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `version` int NOT NULL,
  `card_info_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKoh2smxkjm0e0xg5vj4gxw2rmy` (`card_info_id`),
  CONSTRAINT `FKeuvasat7pv7e8987a84625t71` FOREIGN KEY (`card_info_id`) REFERENCES `card_info` (`card_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'789 Boulevard Test, 31000 Toulouse','lionel.batt@live.fr','test','$2a$10$gnlC/1EYuSdsPfLuGgRSSeNUd0hA068R7RQv09RyBTDfj.xsB9FrW','+33612345680','USER','Dupont','test',0,NULL),(2,'789 Boulevard Test, 31000 Toulouse','lionel.batt+1@gmail.com','test','$2a$10$EMrgGFVvpxPqpEg/v0yJrueTWSvYeTbjVqrK97mdgeQiTL2X98eIe','+33612345680','ADMIN','Dupont','pinktree',0,2),(8,'789 Boulevard Test, 31000 Toulouse','lionel.batt+2@gmail.com','test','$2a$10$hEWIq6pxkDRtmrj6eD5/oO2dGmNYs/bbL5xLcWKCjn.e6UjKffwfy','+33612345680','USER','Dupont','toto',0,NULL),(10,'1 rue de paris , Paris 10','lionel.batt+3@gmail.com','test','$2a$10$iu8ggyu2xf9RmAARg95tuuPF5XZxm/0fMJAhXKq7ac5g4Zcj411.6','+33612345680','USER','Dupont','toto1',1,NULL),(11,'456 test','lionel.batt+5@gmail.com','test','$2a$10$An47c1ROYd94XWDqkkbutePPqlZhNZMcDXtI0n/6bRqs1gObaBlV6','+33646545687','USER','Paul','testUpdate',6,1),(13,'213 Avenue Paul Turin','dupond.toto@gmail.com','Dupond','$2a$10$ZW/KbHRTVgJiI7tkaOzfBuWcef1YK5dUs9ZBlsbQxgqp6Lyet2xV2','0645957541','ADMIN','Toto','TotoDupond',1,4);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-19 13:01:50
