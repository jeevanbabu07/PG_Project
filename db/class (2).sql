-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 19, 2025 at 08:52 AM
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
-- Table structure for table `class`
--

CREATE TABLE `class` (
  `class_name` varchar(255) NOT NULL,
  `subject_1` varchar(50) DEFAULT NULL,
  `subject_2` varchar(50) DEFAULT NULL,
  `subject_3` varchar(50) DEFAULT NULL,
  `subject_4` varchar(50) DEFAULT NULL,
  `subject_5` varchar(50) DEFAULT NULL,
  `subject_6` varchar(50) DEFAULT NULL,
  `subject_7` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `class`
--

INSERT INTO `class` (`class_name`, `subject_1`, `subject_2`, `subject_3`, `subject_4`, `subject_5`, `subject_6`, `subject_7`) VALUES
('1 B.SC(CS)', 'Tamil - I', 'English - I', 'Discrete Mathematics', 'Programmin in C', 'Value Education', 'Digital Principles & Its Applications', NULL),
('1 B.Sc(IT)', 'Tamil - I', 'English - I', 'Principles of Information Technology', 'Discrete Mathemetics', 'Environmental Studies', NULL, NULL),
('1 M.Cs(CS)', 'Analysis & Design of Algorithms', 'Object Oriented Analysis And Design & c++', 'Python Programming', 'Advance Software Engineering', 'R Programming', NULL, NULL),
('1msc', 'vv', 'qq', 'qq', NULL, NULL, NULL, NULL),
('2 B.Sc(CS)', 'Tamil III', 'Engilsh III', 'JAVA Programming', 'salesmanship', 'Operations Research', NULL, NULL),
('2 B.Sc(IT)', 'Tamil - III', 'English - III', 'Data Structures and C++ Programming', 'Operations Research', 'Fundamental Studies', NULL, NULL),
('2Msc(CS)', 'Date Mining', 'RDBMS', 'Software Engineering', 'CN', 'QA', NULL, NULL),
('3 B.Sc(CS)', 'Tamil IV', 'English IV', 'Numercial Methods', 'Data Structures and Algorithms', 'Quantitative Aptitude', 'Advertising', NULL),
('3 B.Sc(IT)', 'Relational Database Management System', 'Operating System', 'Computer Networks', 'Software Engineering', NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `class`
--
ALTER TABLE `class`
  ADD PRIMARY KEY (`class_name`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
