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
-- Table structure for table `arrangement`
--

CREATE TABLE `arrangement` (
  `id` int(11) NOT NULL,
  `department` varchar(255) NOT NULL,
  `reg_no` varchar(50) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `subject` varchar(50) DEFAULT NULL,
  `course_code` varchar(20) DEFAULT NULL,
  `hall_name` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `session` varchar(2) DEFAULT NULL CHECK (`session` in ('an','fn')),
  `row` int(11) NOT NULL,
  `col` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `arrangement`
--

INSERT INTO `arrangement` (`id`, `department`, `reg_no`, `name`, `dob`, `subject`, `course_code`, `hall_name`, `date`, `session`, `row`, `col`) VALUES
(1, '2Msc(CS)', '2023PGD001', 'Aarthi', '2002-12-10', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 1, 1),
(1, '2Msc(CS)', '2023PGD003', 'Ajith', '2002-02-21', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 2, 1),
(1, '2Msc(CS)', '2023PGD004', 'Ganesh Kumar', '2002-02-20', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 3, 1),
(1, '2Msc(CS)', '2023PGD006', 'Harish', '2002-02-26', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 4, 1),
(1, '2Msc(CS)', '2023PGD007', 'jeeva', '2002-11-10', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 5, 1),
(1, '2Msc(CS)', '2023PGD008', 'Jegathishwari', '2002-02-12', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 6, 1),
(1, '2Msc(CS)', '2023PGD009', 'Karthi', '2002-02-26', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 7, 1),
(1, '2Msc(CS)', '2023PGD010', 'Meenaambika', '2002-02-15', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 8, 1),
(1, '2Msc(CS)', '2023PGD011', 'Monika', '2002-02-19', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 9, 1),
(1, '2Msc(CS)', '2023PGD012', 'Muthu Kumar', '2002-02-18', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 10, 1),
(1, '2Msc(CS)', '2023PGD013', 'Palpandi', '2002-02-14', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 11, 1),
(1, '2Msc(CS)', '2023PGD014', 'Prasana Venkatesh', '2002-02-19', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 12, 1),
(1, '2Msc(CS)', '2023PGD015', 'Prasanna', '2002-02-05', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 13, 1),
(1, '2Msc(CS)', '2023PGD016', 'Prithiksha', '2002-02-13', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 14, 1),
(1, '2Msc(CS)', '2023PGD017', 'Priyanka', '2002-02-07', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 15, 1),
(1, '2Msc(CS)', '2023PGD018', 'Rajalakshimi', '2002-12-10', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 16, 1),
(1, '2Msc(CS)', '2023PGD019', 'Santhosh kumar', '2002-05-04', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 17, 1),
(1, '2Msc(CS)', '2023PGD020', 'Soundariya', '2002-12-09', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 18, 1),
(1, '2Msc(CS)', '2023PGD021', 'Subasri', '2025-02-21', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 19, 1),
(1, '2Msc(CS)', '2023PGD022', 'Thamaraikani', '2002-02-07', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 20, 1),
(1, '2Msc(CS)', '2023PGD024', 'Tharun Kumar', '2025-02-13', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 21, 1),
(1, '2Msc(CS)', '2023PGD026', 'VairaSundar', '2002-02-12', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 22, 1),
(1, '2Msc(CS)', '2023PGD027', 'Vicky', '2002-04-09', 'CN', 'qwe', 'qwe3', '2025-04-28', 'AN', 23, 1),
(4, '2Msc(CS)', '2023PGD001', 'Aarthi', '2002-12-10', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 1, 1),
(4, '2Msc(CS)', '2023PGD003', 'Ajith', '2002-02-21', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 1, 2),
(4, '2Msc(CS)', '2023PGD004', 'Ganesh Kumar', '2002-02-20', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 2, 1),
(4, '2Msc(CS)', '2023PGD006', 'Harish', '2002-02-26', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 2, 2),
(4, '2Msc(CS)', '2023PGD007', 'jeeva', '2002-11-10', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 3, 1),
(4, '2Msc(CS)', '2023PGD008', 'Jegathishwari', '2002-02-12', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 3, 2),
(4, '2Msc(CS)', '2023PGD009', 'Karthi', '2002-02-26', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 4, 1),
(4, '2Msc(CS)', '2023PGD010', 'Meenaambika', '2002-02-15', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 4, 2),
(4, '2Msc(CS)', '2023PGD011', 'Monika', '2002-02-19', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 5, 1),
(4, '2Msc(CS)', '2023PGD012', 'Muthu Kumar', '2002-02-18', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 5, 2),
(4, '2Msc(CS)', '2023PGD013', 'Palpandi', '2002-02-14', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 6, 1),
(4, '2Msc(CS)', '2023PGD014', 'Prasana Venkatesh', '2002-02-19', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 6, 2),
(4, '2Msc(CS)', '2023PGD015', 'Prasanna', '2002-02-05', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 7, 1),
(4, '2Msc(CS)', '2023PGD016', 'Prithiksha', '2002-02-13', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 7, 2),
(4, '2Msc(CS)', '2023PGD017', 'Priyanka', '2002-02-07', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 8, 1),
(4, '2Msc(CS)', '2023PGD018', 'Rajalakshimi', '2002-12-10', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 8, 2),
(4, '2Msc(CS)', '2023PGD019', 'Santhosh kumar', '2002-05-04', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 9, 1),
(4, '2Msc(CS)', '2023PGD020', 'Soundariya', '2002-12-09', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 9, 2),
(4, '2Msc(CS)', '2023PGD021', 'Subasri', '2025-02-21', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 10, 1),
(4, '2Msc(CS)', '2023PGD022', 'Thamaraikani', '2002-02-07', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 10, 2),
(4, '2Msc(CS)', '2023PGD024', 'Tharun Kumar', '2025-02-13', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 11, 1),
(4, '2Msc(CS)', '2023PGD026', 'VairaSundar', '2002-02-12', 'CN', 'qwe', 'qwe2', '2025-04-15', 'AN', 11, 2),
(4, '2Msc(CS)', '2023PGD027', 'Vicky', '2002-04-09', 'CN', 'qwe', 'qwe3', '2025-04-15', 'AN', 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `arrangement`
--
ALTER TABLE `arrangement`
  ADD PRIMARY KEY (`id`,`department`,`reg_no`),
  ADD KEY `id` (`id`,`hall_name`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `arrangement`
--
ALTER TABLE `arrangement`
  ADD CONSTRAINT `arrangement_ibfk_1` FOREIGN KEY (`id`,`hall_name`) REFERENCES `hall` (`id`, `room`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
