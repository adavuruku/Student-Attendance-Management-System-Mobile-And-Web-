-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 07, 2018 at 10:46 AM
-- Server version: 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `atbu_attendance`
--

-- --------------------------------------------------------

--
-- Table structure for table `atbu_attendance`
--

CREATE TABLE `atbu_attendance` (
  `id` int(11) NOT NULL,
  `code` varchar(200) NOT NULL,
  `regno` varchar(200) NOT NULL,
  `stlevel` varchar(150) NOT NULL,
  `cid` varchar(200) NOT NULL,
  `recDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `stName` varchar(250) NOT NULL,
  `dept` varchar(250) NOT NULL,
  `facult` varchar(250) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `atbu_attendance`
--

INSERT INTO `atbu_attendance` (`id`, `code`, `regno`, `stlevel`, `cid`, `recDate`, `stName`, `dept`, `facult`) VALUES
(1, 'MTH221', '419419', '200', '2018-10-07 08:10:26', '2018-10-07 08:10:26', 'Abdulhaqq Abdulraheem A.', 'Mathematics', 'Science'),
(2, 'MTH221', '12/23222D/1', '100', '2018-10-07 08:10:26', '2018-10-07 08:10:26', 'Abdullahi Sunday H.', 'Computer Science', 'Science'),
(3, 'CS142', '419419', '200', '2018-10-07 08:10:37', '2018-10-07 08:10:37', 'Abdulhaqq Abdulraheem A.', 'Mathematics', 'Science'),
(4, 'CS142', '14/37139D/1', '200', '2018-10-07 08:10:37', '2018-10-07 08:10:37', 'Marry Jude K.', 'Architect', 'Environmental'),
(5, 'MTH221', '419419', '200', '2018-10-07 08:10:54', '2018-10-07 08:10:54', 'Abdulhaqq Abdulraheem A.', 'Mathematics', 'Science'),
(6, 'MTH221', '14/37139D/1', '200', '2018-10-07 08:10:54', '2018-10-07 08:10:54', 'Marry Jude K.', 'Architect', 'Environmental'),
(7, 'MTH221', '12/23222D/1', '100', '2018-10-07 08:10:54', '2018-10-07 08:10:54', 'Abdullahi Sunday H.', 'Computer Science', 'Science'),
(8, 'CS142', '419419', '200', '2018-10-07 08:11:02', '2018-10-07 08:11:02', 'Abdulhaqq Abdulraheem A.', 'Mathematics', 'Science');

-- --------------------------------------------------------

--
-- Table structure for table `atbu_course`
--

CREATE TABLE `atbu_course` (
  `id` int(11) NOT NULL,
  `Course_Code` varchar(50) NOT NULL,
  `Course_Title` varchar(250) NOT NULL,
  `School` varchar(250) NOT NULL,
  `Department` varchar(250) NOT NULL,
  `Level` varchar(50) NOT NULL,
  `Semester` varchar(50) NOT NULL,
  `course_unit` varchar(10) NOT NULL,
  `staff_id` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `atbu_course`
--

INSERT INTO `atbu_course` (`id`, `Course_Code`, `Course_Title`, `School`, `Department`, `Level`, `Semester`, `course_unit`, `staff_id`) VALUES
(1, 'CS142', 'Introduction To Computer Science', 'Science', 'Mathematics', '100', '1', '4', '111222'),
(2, 'MTH213', 'Analysis I', 'Science', 'Mathematics', '300', '1', '3', '111222'),
(3, 'MTH111', 'Linear Algebra', 'Science', 'Mathematics', '100', '1', '4', '10102'),
(4, 'MTH112', 'Calculus I', 'Science', 'Mathematics', '100', '1', '3', '111222'),
(5, 'MTH223', 'Analysis II', 'Science', 'Mathematics', '200', '2', '3', '180202'),
(7, 'MTH222', 'Mathematical Method', 'Science', 'Mathematics', '200', '2', '3', '10102'),
(9, 'ST271', 'Introduction to Statistics and Probability', 'Science', 'Mathematics', '200', '2', '3', '10102'),
(10, 'MTH224', 'Mathematical Modelling', 'Science', 'Mathematics', '200', '2', '3', '11113'),
(11, 'MTH221', 'Linear Algebra II', 'Science', 'Mathematics', '200', '2', '3', '111222'),
(12, 'MTH511', 'Topology II', 'Science', 'Mathematics', '100', '1', '4', '1456');

-- --------------------------------------------------------

--
-- Table structure for table `atbu_dept`
--

CREATE TABLE `atbu_dept` (
  `id` int(11) NOT NULL,
  `school` varchar(250) NOT NULL,
  `department` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `atbu_staff`
--

CREATE TABLE `atbu_staff` (
  `id` int(11) NOT NULL,
  `staff_id` varchar(250) NOT NULL,
  `staff_name` varchar(250) NOT NULL,
  `staff_dept` varchar(250) NOT NULL,
  `staff_faculty` varchar(250) NOT NULL,
  `staff_password` varchar(250) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  `course_upload` tinyint(1) NOT NULL,
  `staff_registration` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `atbu_staff`
--

INSERT INTO `atbu_staff` (`id`, `staff_id`, `staff_name`, `staff_dept`, `staff_faculty`, `staff_password`, `admin`, `course_upload`, `staff_registration`) VALUES
(1, '111222', 'Abdulraheem Sherif A', 'Mathematics', 'Science', '111222', 1, 1, 1),
(2, '11113', 'Jimoh Hadi O.', 'Mathematics', 'Science', '1010', 0, 1, 1),
(3, '10102', 'Prof. Usman Dahiru', 'Mathematics', 'Science', '111222', 0, 1, 1),
(4, '180202', 'Usman Dalhatu J.', 'Mathematics', 'Science', '1111', 1, 1, 0),
(5, '2345', 'Lawal M. Barde', 'Mathematics', 'Science', '2345', 0, 0, 0),
(6, '1456', 'Yakubu Hamza', 'Mathematics', 'Science', '1456', 0, 0, 0),
(7, '12345', 'Usman Dahiru', 'Mathematics', 'Science', '12345', 0, 0, 0),
(8, '2010', 'Adamu Kuti', 'Computer Science', 'Science', '2010', 0, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `atbu_student`
--

CREATE TABLE `atbu_student` (
  `id` int(11) NOT NULL,
  `student_name` varchar(250) NOT NULL,
  `student_password` varchar(250) NOT NULL,
  `student_facult` varchar(250) NOT NULL,
  `student_dept` varchar(250) NOT NULL,
  `student_level` varchar(20) NOT NULL,
  `student_regno` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `atbu_student`
--

INSERT INTO `atbu_student` (`id`, `student_name`, `student_password`, `student_facult`, `student_dept`, `student_level`, `student_regno`) VALUES
(1, 'Abdulhaqq Abdulraheem A.', '419419', 'Science', 'Mathematics', '200', '419419'),
(2, 'Marry Jude K.', '1111', 'Environmental', 'Architect', '200', '14/37139D/1'),
(3, 'Abdullahi Sunday H.', 'password', 'Science', 'Computer Science', '100', '12/23222D/1');

-- --------------------------------------------------------

--
-- Table structure for table `coursereg`
--

CREATE TABLE `coursereg` (
  `id` int(11) NOT NULL,
  `course_gp` varchar(100) NOT NULL,
  `reg_no` varchar(250) NOT NULL,
  `department` varchar(250) NOT NULL,
  `subject_code` varchar(50) NOT NULL,
  `course_title` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `coursereg`
--

INSERT INTO `coursereg` (`id`, `course_gp`, `reg_no`, `department`, `subject_code`, `course_title`) VALUES
(1, '3', '419419', 'Mathematics', 'MTH221', 'Linear Algebra II'),
(3, '4', '419419', 'Mathematics', 'CS142', 'Introduction To Computer Science'),
(4, '3', '12/23222D/1', 'Computer Science', 'MTH221', 'Linear Algebra II'),
(5, '3', '14/37139D/1', 'Architect', 'CS142', 'Introduction To Computer Science'),
(8, '3', '14/37139D/1', 'Architect', 'MTH221', 'Linear Algebra II'),
(9, '3', '419419', 'Mathematics', 'ST271', 'Introduction to Statistics and Probability');

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE `test` (
  `id` int(11) NOT NULL,
  `data` text NOT NULL,
  `dater` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `test`
--

INSERT INTO `test` (`id`, `data`, `dater`) VALUES
(1, '{"allrecord":[{"stLevel":"200","stName":"Abdulhaqq Abdulraheem A.","stCode":"MTH221","stID":"2018-10-07 00:29:18","stDate":"2018-10-07 00:29:18","stReg":"419419"},{"stLevel":"200","stName":"Marry Jude K.","stCode":"MTH221","stID":"2018-10-07 00:29:18","stDate":"2018-10-07 00:29:18","stReg":"14\\/37139D\\/1"},{"stLevel":"200","stName":"Abdulhaqq Abdulraheem A.","stCode":"CS142","stID":"2018-10-07 00:30:48","stDate":"2018-10-07 00:30:48","stReg":"419419"},{"stLevel":"200","stName":"Marry Jude K.","stCode":"CS142","stID":"2018-10-07 00:30:48","stDate":"2018-10-07 00:30:48","stReg":"14\\/37139D\\/1"},{"stLevel":"200","stName":"Abdulhaqq Abdulraheem A.","stCode":"MTH221","stID":"2018-10-07 00:31:19","stDate":"2018-10-07 00:31:19","stReg":"419419"},{"stLevel":"200","stName":"Marry Jude K.","stCode":"MTH221","stID":"2018-10-07 00:31:19","stDate":"2018-10-07 00:31:19","stReg":"14\\/37139D\\/1"},{"stLevel":"100","stName":"Abdullahi Sunday H.","stCode":"MTH221","stID":"2018-10-07 00:31:19","stDate":"2018-10-07 00:31:19","stReg":"12\\/23222D\\/1"}]}', '2018-10-07 07:27:11'),
(2, 'MTH221', '2018-10-07 00:29:18'),
(3, 'MTH221', '2018-10-07 00:29:18'),
(4, 'MTH221', '2018-10-07 00:30:48'),
(5, 'MTH221', '2018-10-07 00:30:48'),
(6, 'MTH221', '2018-10-07 00:31:19'),
(7, 'MTH221', '2018-10-07 00:31:19'),
(8, 'MTH221', '2018-10-07 00:31:19');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `atbu_attendance`
--
ALTER TABLE `atbu_attendance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `atbu_course`
--
ALTER TABLE `atbu_course`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `atbu_dept`
--
ALTER TABLE `atbu_dept`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `atbu_staff`
--
ALTER TABLE `atbu_staff`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `atbu_student`
--
ALTER TABLE `atbu_student`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `coursereg`
--
ALTER TABLE `coursereg`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `atbu_attendance`
--
ALTER TABLE `atbu_attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `atbu_course`
--
ALTER TABLE `atbu_course`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `atbu_dept`
--
ALTER TABLE `atbu_dept`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `atbu_staff`
--
ALTER TABLE `atbu_staff`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `atbu_student`
--
ALTER TABLE `atbu_student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `coursereg`
--
ALTER TABLE `coursereg`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
