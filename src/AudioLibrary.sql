-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 11, 2024 at 10:18 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `AudioLibrary`
--

-- --------------------------------------------------------

--
-- Table structure for table `Account`
--

CREATE TABLE `Account` (
  `id_account` int(11) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` varchar(255) NOT NULL,
  `administrator` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- Dumping data for table `Account`
--

INSERT INTO `Account` (`id_account`, `username`, `password`, `administrator`) VALUES
(3, 'paul', 'A1dRPeuQOgVudKfkdSR/wf/jHYvkwdSjH1jdR65IQQA=', 1),
(4, 'ceva', 'chaXYZg5O1FnqcrAWRn0ZRL3CdVM7StxeUxiboH+0jA=', 1),
(5, 'ioan', '9lX/xyBDmkkMNNLYfnk2Om4ZXMn6sTuZVLPxFMh16lo=', 0);

-- --------------------------------------------------------

--
-- Table structure for table `Audit`
--

CREATE TABLE `Audit` (
  `id_audit` int(11) NOT NULL,
  `id_account` int(11) NOT NULL,
  `command` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `success` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- Dumping data for table `Audit`
--

INSERT INTO `Audit` (`id_audit`, `id_account`, `command`, `date`, `success`) VALUES
(4, 3, 'login paul paul', '2024-05-11', 1),
(5, 3, 'promote cineva', '2024-05-11', 0),
(6, 3, 'promote ceva', '2024-05-11', 1),
(7, 3, 'login paul paul', '2024-05-11', 1),
(8, 3, 'audit paul', '2024-05-11', 1),
(9, 3, 'audit paul 2', '2024-05-11', 1),
(10, 3, 'audit paul 4', '2024-05-11', 1);

-- --------------------------------------------------------

--
-- Table structure for table `Melody`
--

CREATE TABLE `Melody` (
  `id_melody` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `year` int(11) NOT NULL
) ;

--
-- Dumping data for table `Melody`
--

INSERT INTO `Melody` (`id_melody`, `name`, `author`, `year`) VALUES
(1, 'Alejandro', 'Lady Gaga', 2009),
(2, 'Big boss', 'Cineva', 2025),
(3, 'melody2', 'author2', 2000),
(4, 'melody3', 'author3', 2020),
(5, 'melody10', 'author1', 1999);

-- --------------------------------------------------------

--
-- Table structure for table `Playlist`
--

CREATE TABLE `Playlist` (
  `id_playlist` int(11) NOT NULL,
  `id_account` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- Dumping data for table `Playlist`
--

INSERT INTO `Playlist` (`id_playlist`, `id_account`, `name`) VALUES
(1, 3, 'MyPlaylist'),
(2, 3, 'My sweet small playlist'),
(3, 5, 'abcc'),
(4, 5, 'defg'),
(5, 5, 'minecraft'),
(6, 5, 'duck duck go'),
(7, 5, 'MyPlaylist'),
(8, 4, 'ratatouille');

-- --------------------------------------------------------

--
-- Table structure for table `Playlist_Melody`
--

CREATE TABLE `Playlist_Melody` (
  `id_playlist` int(11) NOT NULL,
  `id_melody` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- Dumping data for table `Playlist_Melody`
--

INSERT INTO `Playlist_Melody` (`id_playlist`, `id_melody`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Account`
--
ALTER TABLE `Account`
  ADD PRIMARY KEY (`id_account`);

--
-- Indexes for table `Audit`
--
ALTER TABLE `Audit`
  ADD PRIMARY KEY (`id_audit`);

--
-- Indexes for table `Melody`
--
ALTER TABLE `Melody`
  ADD PRIMARY KEY (`id_melody`);

--
-- Indexes for table `Playlist`
--
ALTER TABLE `Playlist`
  ADD PRIMARY KEY (`id_playlist`),
  ADD KEY `playl_id_acc_fk` (`id_account`);

--
-- Indexes for table `Playlist_Melody`
--
ALTER TABLE `Playlist_Melody`
  ADD PRIMARY KEY (`id_playlist`,`id_melody`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Account`
--
ALTER TABLE `Account`
  MODIFY `id_account` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `Audit`
--
ALTER TABLE `Audit`
  MODIFY `id_audit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `Melody`
--
ALTER TABLE `Melody`
  MODIFY `id_melody` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Playlist`
--
ALTER TABLE `Playlist`
  MODIFY `id_playlist` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Playlist`
--
ALTER TABLE `Playlist`
  ADD CONSTRAINT `playl_id_acc_fk` FOREIGN KEY (`id_account`) REFERENCES `Account` (`id_account`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
