-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 10 Apr 2026 pada 05.52
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `auth_demo`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `bussiness_units`
--

CREATE TABLE `bussiness_units` (
  `id` bigint(20) NOT NULL,
  `id_bussiness_unit` varchar(255) NOT NULL,
  `id_company` varchar(255) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `id_country` varchar(255) DEFAULT NULL,
  `id_province` varchar(255) DEFAULT NULL,
  `id_city` varchar(255) DEFAULT NULL,
  `tax_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `created_date` date DEFAULT NULL,
  `created_by` varchar(150) DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  `updated_by` varchar(150) DEFAULT NULL,
  `deleted_date` date DEFAULT NULL,
  `deleted_by` varchar(150) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `bussiness_units`
--

INSERT INTO `bussiness_units` (`id`, `id_bussiness_unit`, `id_company`, `code`, `name`, `address`, `id_country`, `id_province`, `id_city`, `tax_number`, `email`, `phone_number`, `status`, `created_date`, `created_by`, `updated_date`, `updated_by`, `deleted_date`, `deleted_by`, `created_at`, `updated_at`) VALUES
(1, '', NULL, 'BUSKPL.0001.0000.000021', 'PT TRIAL WEB', 'JLN SAMBI KEREP LONTAR NO.113', NULL, NULL, NULL, '38298721', 'dokumen.lia@gmail.co.id', '+6325-1336-3613', 1, '2026-04-07', '1', '2026-04-08', '2', NULL, NULL, '2026-04-07 09:44:51', '2026-04-08 01:52:02'),
(2, '6UGPT0000002', NULL, 'BUSKPL.0001.0000.000001', 'PT INDOPRIMA', 'JLNKEDAMEAN 2/NO23', NULL, NULL, NULL, '382682', NULL, '0312-5133-6361', 1, '2026-04-08', '2', '2026-04-08', '2', NULL, NULL, '2026-04-08 07:48:15', '2026-04-08 07:48:15'),
(3, 'IPCUD0000003', NULL, 'BUSKPL.0001.0000.000006', 'UD VISINIAGA MITRA KREASINDO', 'JALAN KEDUNG TARI BLOK F NO 17', NULL, NULL, NULL, NULL, NULL, '0832-3527-2128', 1, '2026-04-08', '2', '2026-04-08', '2', NULL, NULL, '2026-04-08 07:49:24', '2026-04-08 07:49:24');

-- --------------------------------------------------------

--
-- Struktur dari tabel `cities`
--

CREATE TABLE `cities` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `id_city` varchar(150) NOT NULL,
  `id_province` varchar(150) NOT NULL,
  `id_country` varchar(150) NOT NULL,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `created_date` date DEFAULT NULL,
  `created_by` varchar(150) DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  `updated_by` varchar(150) DEFAULT NULL,
  `deleted_date` date DEFAULT NULL,
  `deleted_by` varchar(150) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `cities`
--

INSERT INTO `cities` (`id`, `id_city`, `id_province`, `id_country`, `code`, `name`, `status`, `created_date`, `created_by`, `updated_date`, `updated_by`, `deleted_date`, `deleted_by`, `created_at`, `updated_at`) VALUES
(1, '1Q4SA0000001', '8SZJA0000001', '1DZIN0000001', 'CTY.0001.0001.0001', 'SIDOARJO', 1, '2024-08-14', 'O69KH0000001', '2024-08-14', 'O69KH0000001', '2024-08-14', 'O69KH0000001', '2024-08-10 12:36:56', '2024-08-10 12:36:56'),
(2, '1T3SU0000002', '8SZJA0000001', '1DZIN0000001', 'CTY.0001.0001.0002', 'SURABAYA', 1, '2024-08-14', 'O69KH0000001', '2024-08-26', 'HRULI0000003', '2024-08-14', 'O69KH0000001', '2024-08-10 12:38:11', '2024-08-22 12:17:16'),
(3, 'IFEPR0000003', '8SZJA0000001', '1DZIN0000001', 'CTY.0001.0001.0003', 'PROBOLINGGO', 1, '2024-08-14', 'O69KH0000001', '2025-03-21', 'HRULI0000003', '2024-08-14', 'O69KH0000001', '2024-08-10 12:39:49', '2025-03-20 17:46:44'),
(4, 'L7DMA0000004', '8SZJA0000001', '1DZIN0000001', 'CTY.0001.0001.0004', 'MALANG', 1, '2024-08-14', 'O69KH0000001', '2024-09-21', 'HRULI0000003', '2024-08-14', 'O69KH0000001', '2024-08-10 12:40:41', '2024-09-17 10:50:57'),
(5, '5EDMO0000005', '8SZJA0000001', '1DZIN0000001', 'CTY.0004.0300.0001', 'MOJOKERTO', 3, '2024-08-14', 'O69KH0000001', '2024-08-14', 'O69KH0000001', '2024-08-14', NULL, '2024-08-10 12:46:26', '2024-08-10 13:02:49'),
(6, 'M86DE0000006', '1T9BA0000003', '1DZIN0000001', 'CTY.0004.0300.0002', 'DENPASAR', 1, '2024-08-14', 'O69KH0000001', '2025-03-21', 'HRULI0000003', '2024-08-14', 'O69KH0000001', '2024-08-10 12:47:32', '2025-03-20 17:37:04'),
(7, 'M6DCA0000007', 'NPSBA0000004', 'FF9TH0000005', 'CTY.0001.020.0005', 'CAGAYAN', 1, '2024-08-14', 'O69KH0000001', '2024-08-14', 'O69KH0000001', '2024-08-14', 'O69KH0000001', '2024-08-10 12:48:47', '2024-08-10 12:48:47'),
(8, 'EB3BA0000008', '8SZJA0000001', '1DZIN0000001', 'CTY.0001.00.0004', 'BATU', 1, '2024-09-21', 'HRULI0000003', '2024-09-21', 'HRULI0000003', '2024-09-21', 'HRULI0000003', '2024-09-17 10:54:34', '2024-09-17 10:54:34'),
(9, 'W97AL0000009', 'P59MA000018', '1U9AR000012', 'CTY.0001.020.0010', 'AL-MADINAH', 1, '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-20 17:54:11', '2025-03-20 17:54:11');

-- --------------------------------------------------------

--
-- Struktur dari tabel `countries`
--

CREATE TABLE `countries` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `id_country` varchar(150) NOT NULL,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `created_date` date DEFAULT NULL,
  `created_by` varchar(150) DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  `updated_by` varchar(150) DEFAULT NULL,
  `deleted_date` date DEFAULT NULL,
  `deleted_by` varchar(150) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `countries`
--

INSERT INTO `countries` (`id`, `id_country`, `code`, `name`, `status`, `created_date`, `created_by`, `updated_date`, `updated_by`, `deleted_date`, `deleted_by`, `created_at`, `updated_at`) VALUES
(1, '1DZIN0000001', 'CRY.0001.2100.0001', 'INDONESIA', 1, '2024-08-10', 'HRULI0000003', '2024-08-10', 'HRULI0000003', '2024-08-10', 'HRULI0000003', '2024-08-06 05:30:49', '2024-08-06 05:30:49'),
(2, 'KV6MA0000002', 'CRY.0000.0000.0001', 'MALAYSIA', 1, '2024-08-10', 'HRULI0000003', '2024-08-10', 'HRULI0000003', '2024-08-10', 'HRULI0000003', '2024-08-06 05:36:05', '2024-08-06 05:36:05'),
(3, 'PUMFI0000003', 'CRY.0000.0000.0002', 'FILIPINA', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:49:50', '2024-08-06 05:49:50'),
(4, 'KEUAM0000004', 'CRY.0001.0040.4003', 'AMERIKA', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2025-03-06', 'HRULI0000003', '2024-08-06 05:50:08', '2025-03-05 12:20:20'),
(5, 'FF9TH0000005', 'CRY.0000.0001.0012', 'THAILAND', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:50:43', '2024-08-06 05:50:43'),
(6, '5OWBR0000006', 'CRY.0001.0001.0012', 'BRAZIL', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:50:57', '2024-08-06 05:50:57'),
(7, 'WJHPR0000007', 'CRY.0200.0000.0010', 'PRANCIS', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:51:23', '2024-08-06 05:51:23'),
(8, 'EYSPA0000008', 'CRY.0200.0000.0011', 'PAPUA NUGINI', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:51:36', '2024-08-06 05:51:36'),
(9, '3N1CA0000009', 'CRY.0200.0000.0012', 'CANADA', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:51:51', '2024-08-06 05:51:51'),
(10, '3E1EN00000010', 'CRY.0000.0001.0003', 'ENGLAND', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:52:10', '2024-08-06 05:52:10'),
(11, '4E6NE000011', 'CRY.0000.0001.0004', 'NETHERLAND', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:52:21', '2024-08-06 05:52:21'),
(12, '1U9AR000012', 'CRY.0000.0001.0006', 'ARAB', 1, '2024-08-10', 'K4J0000005', '2025-03-06', 'HRULI0000003', '2024-08-10', 'K4J0000005', '2024-08-06 05:53:00', '2025-03-05 12:20:45'),
(13, '6BDAF000013', 'CRY.0000.0402.0001', 'AFGANISTAN', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2025-03-06', 'HRULI0000003', '2024-08-06 05:53:18', '2025-03-05 12:20:15'),
(14, 'C6IAR000014', 'CRY.0000.0402.0002', 'ARGENTINA', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:53:36', '2024-08-06 05:53:36'),
(15, 'ZSNIN000015', 'CRY.0000.0402.0003', 'INDIA', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:54:03', '2024-08-06 05:54:03'),
(18, 'UDDIR000016', 'CRY.0000.0402.0006', 'IRAN', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:54:36', '2024-08-06 05:54:36'),
(19, 'L28IR000017', 'CRY.0000.0402.0004', 'IRAK', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:54:59', '2024-08-06 05:54:59'),
(20, '9HUJA000018', 'CRY.0005.0302.0001', 'JAMAIKA', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:55:24', '2024-08-06 05:55:24'),
(22, '607SO000019', 'CRY.0005.0302.0002', 'SOUTH KOREA', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:55:57', '2024-08-06 05:55:57'),
(23, '47SLI000020', 'CRY.0000.0000.0017', 'LIBANON', 1, '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-10', 'K4J0000005', '2024-08-06 05:57:20', '2024-08-06 05:57:20');

-- --------------------------------------------------------

--
-- Struktur dari tabel `items`
--

CREATE TABLE `items` (
  `id` bigint(20) NOT NULL,
  `code` varchar(80) NOT NULL,
  `description` varchar(255) NOT NULL,
  `item_type` varchar(30) NOT NULL,
  `stock` decimal(18,4) NOT NULL DEFAULT 0.0000,
  `note` text DEFAULT NULL,
  `business_unit` varchar(40) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `created_by` varchar(100) NOT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `items`
--

INSERT INTO `items` (`id`, `code`, `description`, `item_type`, `stock`, `note`, `business_unit`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'FA.AHK.0000.0000.0000', 'ASET HAK GUNA KENDARAAN PABRIK', 'Sawn Timber', 24.4000, '', 'PT Mindo', '2026-01-12 10:29:23', 'Alvin Gohat', '2026-01-12 11:24:07', 'Stefanus Nigel'),
(2, 'FS.ITY.0002.0003.00002', 'GALILEA BENCH SOFA MODULAR 105X105X53 3.0 CREAM', 'Finish Good', 54366.4200, 'ini saya dummy', 'PT Visiniaga', '2026-01-12 10:33:00', 'Stefanus Nigel', '2026-04-07 06:42:26', 'Alvin Gohat'),
(3, 'FD.CML.0003.0006.00001', 'Brown 475 Desk Vanity With Mirror 12637 Box 1/1', 'Fixed Asset', 78.0000, 'ini cobaan data', 'PT Bimoli', '2026-04-07 06:25:51', '1', '2026-04-07 06:42:12', 'Alvin Gohat');

-- --------------------------------------------------------

--
-- Struktur dari tabel `provinces`
--

CREATE TABLE `provinces` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `id_province` varchar(150) NOT NULL,
  `id_country` varchar(150) NOT NULL,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `created_date` date DEFAULT NULL,
  `created_by` varchar(150) DEFAULT NULL,
  `updated_date` date DEFAULT NULL,
  `updated_by` varchar(150) DEFAULT NULL,
  `deleted_date` date DEFAULT NULL,
  `deleted_by` varchar(150) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `provinces`
--

INSERT INTO `provinces` (`id`, `id_province`, `id_country`, `code`, `name`, `status`, `created_date`, `created_by`, `updated_date`, `updated_by`, `deleted_date`, `deleted_by`, `created_at`, `updated_at`) VALUES
(1, '8SZJA0000001', '1DZIN0000001', 'PRO.0001.0001.0001', 'JAWA TIMUR', 1, '2024-08-12', 'O69KH0000001', '2024-08-12', 'O69KH0000001', '2024-08-12', 'O69KH0000001', '2024-08-08 14:10:09', '2024-08-08 14:10:09'),
(2, 'OTKJA0000002', '1DZIN0000001', 'PRO.0001.0001.0002', 'JAWA TENGAH', 1, '2024-08-12', 'O69KH0000001', '2024-08-14', 'O69KH0000001', '2024-08-12', 'O69KH0000001', '2024-08-08 14:14:19', '2024-08-10 13:10:22'),
(3, '1T9BA0000003', '1DZIN0000001', 'PRO.0001.0001.0003', 'BALI', 1, '2024-08-12', 'O69KH0000001', '2024-08-12', 'O69KH0000001', '2024-08-12', 'O69KH0000001', '2024-08-08 14:17:13', '2024-08-08 14:17:13'),
(4, 'NPSBA0000004', 'FF9TH0000005', 'PRO.0000.0001.0004', 'BANGKOK', 1, '2024-08-12', 'O69KH0000001', '2024-08-12', 'O69KH0000001', '2024-08-12', 'O69KH0000001', '2024-08-08 14:28:33', '2024-08-08 14:28:33'),
(5, 'EL0JO0000005', 'KV6MA0000002', 'PRO.0000.0001.0005', 'JOHOR', 1, '2024-09-21', 'HRULI0000003', '2024-09-21', 'HRULI0000003', '2024-09-21', 'HRULI0000003', '2024-09-17 11:01:15', '2024-09-17 11:01:15'),
(6, 'UITPE0000006', 'KV6MA0000002', 'PRO.0000.0001.0006', 'PENANG', 1, '2024-09-21', 'HRULI0000003', '2024-09-21', 'HRULI0000003', '2024-09-21', 'HRULI0000003', '2024-09-17 11:01:56', '2024-09-17 11:01:56'),
(7, 'JJGCA0000005', 'FF9TH0000005', 'PRO.0000.0001.0007', 'CAGAYAN', 1, '2024-08-13', 'S25NI0000002', '2025-03-21', 'HRULI0000003', '2024-08-13', 'S25NI0000002', '2024-08-08 09:14:24', '2025-03-20 13:16:35'),
(8, 'N3WIL0000006', 'PUMFI0000003', 'PRO.0000.0001.0008', 'ILOCOS NORTE', 2, '2024-08-13', 'S25NI0000002', '2024-08-13', NULL, '2024-08-13', 'S25NI0000002', '2024-08-08 09:16:06', '2024-08-08 13:59:45'),
(9, 'CXWGY0000007', '607SO000019', 'PRO.0000.0520.0040', 'GYEONGGI', 1, '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-08 13:56:10', '2024-08-08 13:56:10'),
(10, 'WZZGY0000008', '607SO000019', 'PRO.0000.0520.0041', 'GYEONGSANG UTARA', 1, '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-08 13:56:27', '2024-08-08 13:56:27'),
(11, 'GEIJE0000009', '607SO000019', 'PRO.0000.0520.0042', 'JEJU', 1, '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-08 13:56:45', '2024-08-08 13:56:45'),
(12, 'BMAON00000010', '3N1CA0000009', 'PRO.0000.0520.0043', 'ONTARIO', 1, '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-13', 'O69KH0000001', '2024-08-08 13:57:56', '2024-08-08 13:57:56'),
(13, 'BH4QU000011', '3N1CA0000009', 'PRO.0000.0520.0044', 'QUEBEC', 3, '2024-08-13', 'O69KH0000001', '2024-08-13', NULL, '2024-08-13', NULL, '2024-08-08 13:58:13', '2024-08-08 17:27:04'),
(15, 'T4VDA000014', '1DZIN0000001', 'PRO.0000.0520.0045', 'DAERAH ISTIMEWA JOGJAKARTA', 1, '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-20 13:33:32', '2025-03-20 13:33:56'),
(16, '1L5SY000015', '1U9AR000012', 'PRO.0000.0520.0046', 'SYARQIYAH', 1, '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-20 17:44:47', '2025-03-20 17:44:47'),
(17, '861MA000016', '1U9AR000012', 'PRO.0000.0520.0047', 'MAKKAH', 1, '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-20 17:45:09', '2025-03-20 17:45:09'),
(18, '005RI000017', '1U9AR000012', 'PRO.0000.0520.0048', 'RIYADH', 1, '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-20 17:45:30', '2025-03-20 17:45:30'),
(19, 'P59MA000018', '1U9AR000012', 'PRO.0000.0520.0049', 'MADINAH', 1, '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-21', 'HRULI0000003', '2025-03-20 17:46:00', '2025-03-20 17:46:00');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password_hash`, `created_at`) VALUES
(1, 'Stefanus Nigel', 'dokumen.nigel2@gmail.com', '$2a$12$dq7w4otwC9evDh3A46iRN.EPztrUcnKqAxQMO.0ZIRhVFMWuVjRZa', '2026-01-12 02:36:08'),
(2, 'Alvin Gohat', 'alvin.vas@gmail.com', '$2a$12$dq7w4otwC9evDh3A46iRN.EPztrUcnKqAxQMO.0ZIRhVFMWuVjRZa', '2026-01-12 07:58:39');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `bussiness_units`
--
ALTER TABLE `bussiness_units`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_bussiness_units_company_code` (`id_company`,`code`);

--
-- Indeks untuk tabel `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_items_code_bu` (`code`,`business_unit`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `bussiness_units`
--
ALTER TABLE `bussiness_units`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `items`
--
ALTER TABLE `items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
