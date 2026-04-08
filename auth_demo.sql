-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 08 Apr 2026 pada 11.22
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
