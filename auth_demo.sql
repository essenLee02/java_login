-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 12 Jan 2026 pada 13.18
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
(2, 'FS.ITY.0002.0003.00002', 'GALILEA BENCH SOFA MODULAR 105X105X53 3.0 CREAM', 'Finish Good', 543.0000, 'ini saya coba dl', 'PT Visiniaga', '2026-01-12 10:33:00', 'Stefanus Nigel', '2026-01-12 11:07:52', 'Alvin Gohat');

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
(1, 'Stefanus Nigel', 'dokuemen.nigel2@gmail.com', '$2a$12$dq7w4otwC9evDh3A46iRN.EPztrUcnKqAxQMO.0ZIRhVFMWuVjRZa', '2026-01-12 02:36:08'),
(2, 'Alvin Gohat', 'alvin.vas@gmail.com', '$2a$12$jC0VmbUaJDzxm9zEmF/uyOKPxMQ4S3vFi4VYx0RikLenZ0sJP8ZIG', '2026-01-12 07:58:39');

--
-- Indexes for dumped tables
--

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
-- AUTO_INCREMENT untuk tabel `items`
--
ALTER TABLE `items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
