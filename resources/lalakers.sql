-- phpMyAdmin SQL Dump
-- version 4.9.7
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 05, 2025 at 03:32 AM
-- Server version: 8.4.3
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lalakers`
--

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `DeleteActivity`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteActivity` (IN `ActivityID` INT)  BEGIN
    DELETE FROM Activity
    WHERE Activity_ID = ActivityID;
END$$

DROP PROCEDURE IF EXISTS `DeleteAppointment`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteAppointment` (IN `AppointmentId` INT)  BEGIN
    DELETE FROM Appointment
    WHERE appointment_id = AppointmentId;
END$$

DROP PROCEDURE IF EXISTS `DeleteCaregiver`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteCaregiver` (IN `Caregiverid` INT)  BEGIN
	DELETE FROM Caregiver
    Where caregiver_id = caregiverid;
END$$

DROP PROCEDURE IF EXISTS `DeleteCaregiverService`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteCaregiverService` (IN `CaregiverId` INT, IN `ServiceId` INT)  BEGIN
    DELETE FROM `caregiver_service`
    WHERE `caregiver_id` = CaregiverId AND `service_id` = ServiceId;
END$$

DROP PROCEDURE IF EXISTS `DeleteElder`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteElder` (IN `ElderID` INT)  BEGIN
    DELETE FROM `elder`
    WHERE `elder_id` = ElderID;
END$$

DROP PROCEDURE IF EXISTS `DeleteGuardian`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteGuardian` (IN `GuardianID` INT)  BEGIN
    DELETE FROM Guardian
    WHERE Guardian_ID = GuardianID;
END$$

DROP PROCEDURE IF EXISTS `DeleteMedicalRecord`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteMedicalRecord` (IN `recordId` INT)  BEGIN
    DELETE FROM Medical_Record
    WHERE medical_record_id = recordId;
END$$

DROP PROCEDURE IF EXISTS `DeletePayment`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeletePayment` (IN `PaymentID` INT)  BEGIN
    DELETE FROM Payment WHERE payment_id = PaymentID;
END$$

DROP PROCEDURE IF EXISTS `DeleteService`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `DeleteService` (IN `ServiceID` INT)  BEGIN
    DELETE FROM Service
    WHERE service_id = ServiceID;
END$$

DROP PROCEDURE IF EXISTS `FindAdminByUsernameAndPassword`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `FindAdminByUsernameAndPassword` (IN `fUsername` VARCHAR(50), IN `fPassword` VARCHAR(50))  BEGIN
	   SELECT admin_id, username, password FROM admin
       Where username = fusername  
       Limit 1;

END$$

DROP PROCEDURE IF EXISTS `FindCaregiverByUsernameAndPassword`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `FindCaregiverByUsernameAndPassword` (IN `p_Username` VARCHAR(255), IN `p_Password` VARCHAR(255))  BEGIN
 SELECT *
    FROM Caregiver
    WHERE Username = p_Username
    LIMIT 1;
END$$

DROP PROCEDURE IF EXISTS `FindGuardianByUsernameAndPassword`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `FindGuardianByUsernameAndPassword` (IN `fUsername` VARCHAR(255), IN `fPassword` VARCHAR(255))  BEGIN
    SELECT
        Guardian_ID AS Guardianid,
        Username,
        Password,
        first_name AS Firstname,
        last_name AS Lastname,
        contact_number AS contact_number,
        Email,
        Address
    FROM Guardian
    WHERE Username = fUsername
    LIMIT 1;
END$$

DROP PROCEDURE IF EXISTS `GetActivityById`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetActivityById` (IN `fActivityID` INT)  BEGIN
    SELECT
        Activity_ID AS activity_id,
        Title,
        Description,
        Timestamp
    FROM Activity
    WHERE Activity_ID = fActivityID
    LIMIT 1;
END$$

DROP PROCEDURE IF EXISTS `GetAllActivities`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllActivities` ()  BEGIN
    SELECT
        ActivityID AS activity_id,
        Title,
        Description,
        Timestamp
    FROM Activity
    ORDER BY Timestamp DESC;
END$$

DROP PROCEDURE IF EXISTS `GetAllAppointments`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllAppointments` ()  BEGIN
    -- Select all appointment details from the Appointment table
    SELECT 
        appointment_id,
        appointment_date,
        status,
        duration,
        creation_date
    FROM 
        Appointment;
END$$

DROP PROCEDURE IF EXISTS `GetAllCaregivers`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllCaregivers` ()  BEGIN
	SELECT * FROM caregiver;
END$$

DROP PROCEDURE IF EXISTS `GetAllCaregiverServices`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllCaregiverServices` ()  BEGIN
    SELECT `caregiver_id`, `service_id`, `experience_years`, `hourly_rate`
    FROM `caregiver_service`;
END$$

DROP PROCEDURE IF EXISTS `GetAllElders`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllElders` ()  BEGIN
    SELECT `elder_id`, `first_name`, `last_name`, `date_of_birth`, `contact_number`, `email`, `address`
    FROM `elder`;
END$$

DROP PROCEDURE IF EXISTS `GetAllMedicalRecords`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllMedicalRecords` ()  BEGIN
    SELECT 
        medical_record_id,
        diagnosis,
        medications,
        treatment_plan,
        medication_status,
        treatment_status,
        time_stamp
    FROM Medical_Record;
END$$

DROP PROCEDURE IF EXISTS `GetAllPayments`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllPayments` ()  BEGIN
    SELECT 
        payment_id,
        total_amount,
        payment_method,
        additional_charges,
        currency,
        transaction_date
    FROM 
        Payment;
END$$

DROP PROCEDURE IF EXISTS `GetAllServices`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAllServices` ()  BEGIN
    SELECT service_id, category, service_name, description
    FROM Service;
END$$

DROP PROCEDURE IF EXISTS `GetAppointmentById`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetAppointmentById` (IN `AppointmentId` INT)  BEGIN
    -- Select appointment details based on the provided AppointmentId
    SELECT 
        appointment_id,
        appointment_date,
        status,
        duration,
        creation_date
    FROM 
        Appointment
    WHERE 
        appointment_id = AppointmentId;
END$$

DROP PROCEDURE IF EXISTS `GetCaregiverById`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetCaregiverById` (IN `caregiverId` INT)  BEGIN
 SELECT 
        caregiver_id,
        username,
        password,
        first_name,
        last_name,
        date_of_birth,
        gender,
        contact_number,
        email,
        address,
        certifications,
        background_check_status,
        medical_clearance_status,
        availability_schedule,
        employment_type
    FROM Caregiver
    WHERE caregiver_id = caregiverId;
END$$

DROP PROCEDURE IF EXISTS `GetCaregiverService`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetCaregiverService` (IN `CaregiverId` INT, IN `ServiceId` INT)  BEGIN
    SELECT `experience_years`, `hourly_rate`
    FROM `caregiver_service`
    WHERE `caregiver_id` = CaregiverId AND `service_id` = ServiceId;
END$$

DROP PROCEDURE IF EXISTS `GetElderById`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetElderById` (IN `ElderId` INT)  BEGIN
    SELECT `elder_id`, `first_name`, `last_name`, `date_of_birth`, `contact_number`, `email`, `address`
    FROM `elder`
    WHERE `elder_id` = ElderId;
END$$

DROP PROCEDURE IF EXISTS `GetEldersByGuardianId`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetEldersByGuardianId` (IN `GuardianID` INT)  BEGIN
    SELECT gel.guardian_id ,e.elder_id, e.first_name, e.last_name, e.date_of_birth, e.contact_number, e.email, e.address
    FROM elder e
    JOIN guardian_elder gel ON e.elder_id = gel.elder_id
    WHERE gel.guardian_id = GuardianID;
END$$

DROP PROCEDURE IF EXISTS `GetGuardianById`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetGuardianById` (IN `GuardianID` INT)  BEGIN
    SELECT
        guardian_id AS guardian_id,
        first_name AS first_name,
        last_name AS last_name,
        contact_number AS contact_number,
        Email AS email,
        Address AS address
    FROM Guardian
    WHERE Guardian_ID = GuardianID
    LIMIT 1;
END$$

DROP PROCEDURE IF EXISTS `GetMedicalRecordById`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetMedicalRecordById` (IN `RecordId` INT)  BEGIN
    SELECT 
        medical_record_id,
        diagnosis,
        medications,
        treatment_plan,
        medication_status,
        treatment_status,
        time_stamp
    FROM Medical_Record
    WHERE medical_record_id= RecordId;
END$$

DROP PROCEDURE IF EXISTS `GetPaymentById`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetPaymentById` (IN `PaymentID` INT)  BEGIN
    SELECT 
        payment_id,
        total_amount,
        payment_method,
        additional_charges,
        currency,
        transaction_date
    FROM 
        Payment
    WHERE 
        payment_id = PaymentID;
END$$

DROP PROCEDURE IF EXISTS `GetServiceByID`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetServiceByID` (IN `ServiceID` INT)  BEGIN
    SELECT service_id, category, service_name, description
    FROM Service
    WHERE service_id = ServiceID;
END$$

DROP PROCEDURE IF EXISTS `InsertActivity`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertActivity` (IN `fTitle` VARCHAR(255), IN `fDescription` TEXT)  BEGIN
    INSERT INTO Activity (
        Title,
        Description
    ) VALUES (
        fTitle,
        fDescription
    );
END$$

DROP PROCEDURE IF EXISTS `InsertAppointment`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertAppointment` (IN `AppointmentDate` DATETIME, IN `Duration` INT)  BEGIN
    INSERT INTO Appointment (
        Appointment_Date,
        Duration
    ) VALUES (
        AppointmentDate,
        Duration
    );
END$$

DROP PROCEDURE IF EXISTS `InsertCaregiver`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertCaregiver` (IN `username` VARCHAR(50), IN `Password` VARCHAR(255), IN `First_Name` VARCHAR(50), IN `Last_Name` VARCHAR(50), IN `Date_Of_Birth` DATETIME, IN `Gender` ENUM('Male','Female','Other'), IN `Contact_Number` VARCHAR(20), IN `Email` VARCHAR(100), IN `Address` VARCHAR(255), IN `Certifications` TEXT, IN `Employment_Type` ENUM('Full-time','Part-time'), IN `Availability_Schedule` TEXT)  BEGIN
    INSERT INTO caregiver (
        Username,
        Password,
        First_Name,
        Last_Name,
        Date_Of_Birth,
        Gender,
        Contact_Number,
        Email,
        Address,
        Certifications,
        Employment_Type,
        Availability_Schedule
    )
    VALUES (
        Username,
        Password,
        First_Name,
        Last_Name,
        Date_Of_Birth,
        Gender,
        Contact_Number,
        Email,
        Address,
        Certifications,
        Employment_Type,
        Availability_Schedule
    );
    END$$

DROP PROCEDURE IF EXISTS `InsertCaregiverService`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertCaregiverService` (IN `CaregiverId` INT, IN `ServiceId` INT, IN `ExperienceYears` INT, IN `HourlyRate` DECIMAL(10,2))  BEGIN
    INSERT INTO Caregiver_Service (
        caregiver_id,
        service_id,
        experience_years,
        hourly_rate
    ) VALUES (
        CaregiverId,
        ServiceId,
        ExperienceYears,
        HourlyRate
    );
END$$

DROP PROCEDURE IF EXISTS `InsertElder`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertElder` (IN `FirstName` VARCHAR(255), IN `LastName` VARCHAR(255), IN `DateOfBirth` DATETIME, IN `ContactNumber` VARCHAR(20), IN `Email` VARCHAR(100), IN `Address` VARCHAR(255))  BEGIN
    INSERT INTO `elder` (
        `first_name`,
        `last_name`,
        `date_of_birth`,
        `contact_number`,
        `email`,
        `address`
    ) VALUES (
        FirstName,
        LastName,
        DateOfBirth,
        ContactNumber,
        Email,
        Address
    );
END$$

DROP PROCEDURE IF EXISTS `InsertGuardian`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertGuardian` (IN `Username` VARCHAR(255), IN `Password` VARCHAR(255), IN `FirstName` VARCHAR(255), IN `LastName` VARCHAR(255), IN `ContactNumber` VARCHAR(50), IN `Email` VARCHAR(255), IN `Address` VARCHAR(500))  BEGIN
    INSERT INTO Guardian (
        Username,
        Password,
        First_Name,
        Last_Name,
        Contact_Number,
        Email,
        Address
    ) VALUES (
        Username,
        Password,
        FirstName,
        LastName,
        ContactNumber,
        Email,
	Address
    );
END$$

DROP PROCEDURE IF EXISTS `InsertGuardianElderLink`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertGuardianElderLink` (IN `GuardianID` INT, IN `ElderID` INT, IN `RelationshipType` VARCHAR(255))  BEGIN
    INSERT INTO `guardian_elder` (
        `guardian_id`,
        `elder_id`,
        `relationship_type`
    ) VALUES (
        GuardianID,
        ElderID,
        RelationshipType
    );
END$$

DROP PROCEDURE IF EXISTS `InsertMedicalRecord`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertMedicalRecord` (IN `Diagnosis` VARCHAR(255), IN `Medications` VARCHAR(255), IN `TreatmentPlan` VARCHAR(255), IN `MedicationStatus` ENUM('Ongoing','Completed','Discontinued'), IN `TreatmentStatus` ENUM('Ongoing','Completed','Discontinued'))  BEGIN
    INSERT INTO Medical_Record (
        diagnosis,
        medications,
        treatment_plan,
        medication_status,
        treatment_status
    ) 
    VALUES (
        Diagnosis,
        Medications,
        TreatmentPlan,
        MedicationStatus,
        TreatmentStatus
    );
END$$

DROP PROCEDURE IF EXISTS `InsertPayment`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertPayment` (IN `TotalAmount` DECIMAL(10,2), IN `PaymentMethod` ENUM('Credit Card','Debit Card','Cash','Bank Transfer','Insurance'), IN `AdditionalCharges` DECIMAL(10,2), IN `Currency` ENUM('USD','EUR','GBP','CAD','AUD','PHP'))  BEGIN
    INSERT INTO Payment (
        total_amount,
        payment_method,
        additional_charges,
        currency
    )
    VALUES (
        TotalAmount,
        PaymentMethod,
        AdditionalCharges,
        Currency
    );
END$$

DROP PROCEDURE IF EXISTS `InsertService`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `InsertService` (IN `Category` VARCHAR(255), IN `ServiceName` VARCHAR(255), IN `Description` TEXT)  BEGIN
    INSERT INTO Service (category, service_name, description)
    VALUES (Category, ServiceName, Description);
END$$

DROP PROCEDURE IF EXISTS `UpdateActivity`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateActivity` (IN `ActivityID` INT, IN `fTitle` VARCHAR(255), IN `fDescription` TEXT)  BEGIN
    UPDATE Activity
    SET
        Title = fTitle,
        Description = fDescription
    WHERE Activity_ID = ActivityID;
END$$

DROP PROCEDURE IF EXISTS `UpdateAppointment`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateAppointment` (IN `appointmentid` INT, IN `fstatus` ENUM('PAID','UNPAID','FINISHED','CANCELLED'))  BEGIN
    UPDATE appointment
    SET 
		status = fstatus 
    WHERE appointment_id = appointmentId;
END$$

DROP PROCEDURE IF EXISTS `UpdateCaregiver`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateCaregiver` (IN `scaregiver_id` INT, IN `Username` VARCHAR(255), IN `Password` VARCHAR(255), IN `First_Name` VARCHAR(255), IN `Last_Name` VARCHAR(255), IN `Date_Of_Birth` DATETIME, IN `Gender` ENUM('Male','Female','Other'), IN `Contact_Number` VARCHAR(20), IN `Email` VARCHAR(100), IN `Address` VARCHAR(255), IN `Certifications` TEXT, IN `AvailabilitySchedule` TEXT, IN `EmploymentType` ENUM('Full-time','Part-time'))  BEGIN
    UPDATE Caregiver
    SET
        Username = Username,
        Password = Password,
        First_Name = First_Name,
        Last_Name = Last_Name,
        Date_Of_Birth = Date_Of_Birth,
        Gender = Gender,
        Contact_Number = Contact_Number,
        Email = Email,
        Address = Address,
        Certifications = Certifications,
        Availability_Schedule = Availability_Schedule,
        Employment_Type = Employment_Type
    WHERE caregiver_id = scaregiver_id;
END$$

DROP PROCEDURE IF EXISTS `UpdateCaregiverService`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateCaregiverService` (IN `CaregiverId` INT, IN `ServiceId` INT, IN `ExperienceYears` INT, IN `HourlyRate` DECIMAL(10,2))  BEGIN
    UPDATE `caregiver_service`
    SET 
        `experience_years` = ExperienceYears,
        `hourly_rate` = HourlyRate
    WHERE 
        `caregiver_id` = CaregiverId AND 
        `service_id` = ServiceId;
END$$

DROP PROCEDURE IF EXISTS `UpdateCaregiverStatus`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateCaregiverStatus` (IN `CaregiverId` INT, IN `Backgroundcheck` ENUM('Passed','Pending','Failed'), IN `MedicalClearance` ENUM('Cleared','Not Cleared','Pending'))  BEGIN
    UPDATE Caregiver
    SET 
        background_check_status = Backgroundcheck,
        medical_clearance_status = MedicalClearance
    WHERE caregiver_id = CaregiverId;
END$$

DROP PROCEDURE IF EXISTS `UpdateElder`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateElder` (IN `ElderID` INT, IN `FirstName` VARCHAR(255), IN `LastName` VARCHAR(255), IN `DateOfBirth` DATETIME, IN `ContactNumber` VARCHAR(20), IN `Email` VARCHAR(100), IN `Address` VARCHAR(255))  BEGIN
    UPDATE `elder`
    SET 
        `first_name` = FirstName,
        `last_name` = LastName,
        `date_of_birth` = DateOfBirth,
        `contact_number` = ContactNumber,
        `email` = Email,
        `address` = Address
    WHERE `elder_id` = ElderID;
END$$

DROP PROCEDURE IF EXISTS `UpdateGuardian`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateGuardian` (IN `GuardianID` INT, IN `FirstName` VARCHAR(255), IN `LastName` VARCHAR(255), IN `ContactNumber` VARCHAR(50), IN `Email` VARCHAR(255), IN `Address` VARCHAR(500))  BEGIN
    UPDATE Guardian
    SET
        First_Name = FirstName,
        Last_Name = LastName,
        Contact_Number = ContactNumber,
        Email = Email,
        Address = Address
    WHERE Guardian_ID = guardianID;
END$$

DROP PROCEDURE IF EXISTS `UpdateMedicalRecord`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateMedicalRecord` (IN `recordId` INT, IN `diagnosis` VARCHAR(255), IN `medications` VARCHAR(255), IN `treatmentPlan` VARCHAR(255), IN `medicationStatus` ENUM('Ongoing','Completed','Discontinued'), IN `treatmentStatus` ENUM('Ongoing','Completed','Discontinued'))  BEGIN
    UPDATE Medical_Record
    SET 
        diagnosis = diagnosis,
        medications = medications,
        treatment_plan = treatmentPlan,
        medication_status = medicationStatus,
        treatment_status = treatmentStatus
    WHERE medical_record_id = recordId;
END$$

DROP PROCEDURE IF EXISTS `UpdatePayment`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdatePayment` (IN `paymentid` INT, IN `TotalAmount` DECIMAL(10,2), IN `PaymentMethod` ENUM('Credit Card','Debit Card','Cash','Bank Transfer','Insurance'), IN `AdditionalCharges` DECIMAL(10,2), IN `Currency` ENUM('USD','EUR','GBP','CAD','AUD','PHP'))  BEGIN
    UPDATE Payment
    SET 
        total_amount = totalamount,
        payment_method = paymentmethod,
        additional_charges = additionalcharges,
        currency = currency
    WHERE 
        payment_id = paymentid;
END$$

DROP PROCEDURE IF EXISTS `UpdateService`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateService` (IN `ServiceID` INT, IN `Category` VARCHAR(255), IN `ServiceName` VARCHAR(255), IN `Description` VARCHAR(255))  BEGIN
    UPDATE Service
    SET category = Category,
        service_name = ServiceName,
        description = Description
    WHERE service_id = ServiceID;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity` (
  `activity_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`activity_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`admin_id`, `username`, `password`) VALUES
(1, 'admin1', 'adminpass');

-- --------------------------------------------------------

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
CREATE TABLE IF NOT EXISTS `appointment` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `appointment_date` datetime NOT NULL,
  `status` enum('PAID','UNPAID','FINISHED','CANCELLED') COLLATE utf8mb4_general_ci DEFAULT 'UNPAID',
  `duration` int NOT NULL,
  `creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`appointment_id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointment`
--

INSERT INTO `appointment` (`appointment_id`, `appointment_date`, `status`, `duration`, `creation_date`) VALUES
(18, '2025-03-25 00:00:00', 'FINISHED', 50, '2025-05-05 02:10:43');

-- --------------------------------------------------------

--
-- Table structure for table `caregiver`
--

DROP TABLE IF EXISTS `caregiver`;
CREATE TABLE IF NOT EXISTS `caregiver` (
  `caregiver_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(25) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` enum('Male','Female','Other') COLLATE utf8mb4_general_ci NOT NULL,
  `contact_number` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `address` text COLLATE utf8mb4_general_ci NOT NULL,
  `certifications` text COLLATE utf8mb4_general_ci NOT NULL,
  `employment_type` enum('Full-time','Part-time') COLLATE utf8mb4_general_ci NOT NULL,
  `background_check_status` enum('Passed','Pending','Failed') COLLATE utf8mb4_general_ci DEFAULT 'Pending',
  `medical_clearance_status` enum('Cleared','Not Cleared','Pending') COLLATE utf8mb4_general_ci DEFAULT 'Pending',
  `availability_schedule` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`caregiver_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `caregiver_service`
--

DROP TABLE IF EXISTS `caregiver_service`;
CREATE TABLE IF NOT EXISTS `caregiver_service` (
  `caregiver_id` int NOT NULL,
  `service_id` int NOT NULL,
  `experience_years` int NOT NULL,
  `hourly_rate` decimal(10,2) NOT NULL,
  PRIMARY KEY (`caregiver_id`,`service_id`),
  KEY `service_id` (`service_id`),
  KEY `caregiver_id` (`caregiver_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `elder`
--

DROP TABLE IF EXISTS `elder`;
CREATE TABLE IF NOT EXISTS `elder` (
  `elder_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `date_of_birth` date NOT NULL,
  `contact_number` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `address` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`elder_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `contact_number` (`contact_number`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `guardian`
--

DROP TABLE IF EXISTS `guardian`;
CREATE TABLE IF NOT EXISTS `guardian` (
  `guardian_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(25) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `contact_number` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `address` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`guardian_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `contact_number` (`contact_number`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `guardian_elder`
--

DROP TABLE IF EXISTS `guardian_elder`;
CREATE TABLE IF NOT EXISTS `guardian_elder` (
  `guardian_id` int NOT NULL,
  `elder_id` int NOT NULL,
  `relationship_type` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`guardian_id`,`elder_id`),
  KEY `elder_id` (`elder_id`),
  KEY `guardian_id` (`guardian_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `medical_record`
--

DROP TABLE IF EXISTS `medical_record`;
CREATE TABLE IF NOT EXISTS `medical_record` (
  `medical_record_id` int NOT NULL AUTO_INCREMENT,
  `diagnosis` text COLLATE utf8mb4_general_ci NOT NULL,
  `medications` text COLLATE utf8mb4_general_ci NOT NULL,
  `treatment_plan` text COLLATE utf8mb4_general_ci NOT NULL,
  `medication_status` enum('Ongoing','Completed','Discontinued') COLLATE utf8mb4_general_ci NOT NULL,
  `treatment_status` enum('Ongoing','Completed','Discontinued') COLLATE utf8mb4_general_ci NOT NULL,
  `time_stamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`medical_record_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `medical_record_update`
--

DROP TABLE IF EXISTS `medical_record_update`;
CREATE TABLE IF NOT EXISTS `medical_record_update` (
  `medical_record_id` int NOT NULL,
  `update_number` int NOT NULL AUTO_INCREMENT,
  `new_diagnosis` text COLLATE utf8mb4_general_ci,
  `new_medication` text COLLATE utf8mb4_general_ci,
  `new_treatment_plan` text COLLATE utf8mb4_general_ci,
  `new_medication_status` enum('Ongoing','Completed','Discontinued') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `new_treatment_status` enum('Ongoing','Completed','Discontinued') COLLATE utf8mb4_general_ci DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`update_number`),
  KEY `medical_record_id` (`medical_record_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
CREATE TABLE IF NOT EXISTS `payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `total_amount` decimal(10,2) NOT NULL,
  `payment_method` enum('Credit Card','Debit Card','Cash','Bank Transfer','Insurance') COLLATE utf8mb4_general_ci NOT NULL,
  `additional_charges` decimal(10,2) DEFAULT '0.00',
  `currency` enum('USD','EUR','GBP','CAD','AUD') COLLATE utf8mb4_general_ci NOT NULL,
  `transaction_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`payment_id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE IF NOT EXISTS `service` (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `service_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`service_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
