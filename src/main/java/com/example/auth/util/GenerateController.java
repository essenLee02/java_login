package com.example.auth.util;

import java.util.Random;

public class GenerateController {

    protected String generateRandomString(int length, String nama, long digitData) {

        String characters = "0123456789abcdefghijklmnopqrstuvwxyz";
        String digits = "000000";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        String digitDataStr = String.valueOf(digitData);

        // logic digit seperti Laravel
        if (digitDataStr.length() > 1 && digitDataStr.length() <= digits.length()) {
            digits = digits.substring(0, digits.length() - digitDataStr.length()) + (digitData + 1);
        } else if (digitDataStr.length() > digits.length()) {
            digits = String.valueOf(digitData + 1);
        } else {
            digits = digits + (digitData + 1);
        }

        // random string
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        // ambil 2 huruf depan nama
        String namaClean = (nama == null ? "" : nama.trim().toUpperCase());
        String namaPrefix;

        if (namaClean.length() >= 2) {
            namaPrefix = namaClean.substring(0, 2);
        } else {
            namaPrefix = String.format("%-2s", namaClean).replace(' ', 'X');
        }

        return (randomString.toString() + namaPrefix + digits).toUpperCase();
    }
}