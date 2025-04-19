-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 19, 2025 at 08:51 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `main_project`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_database`
--

CREATE TABLE `admin_database` (
  `username` varchar(10) NOT NULL CHECK (`username` regexp cast('^[A-Za-z]{3,10}$' as char charset binary)),
  `password` varchar(8) NOT NULL CHECK (`password` regexp cast('^[A-Za-z]{3,10}$' as char charset binary))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin_database`
--

INSERT INTO `admin_database` (`username`, `password`) VALUES
('Jeeva', 'Password');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_database`
--
ALTER TABLE `admin_database`
  ADD PRIMARY KEY (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
