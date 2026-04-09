package com.example.auth.util;

import java.util.Random;

import jakarta.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class GenerateController {

    private static final Logger log = LoggerFactory.getLogger(GenerateController.class);

    protected boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    public String isLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    protected String getLoginUserName(HttpSession session) {
        return session.getAttribute("userName") != null
            ? String.valueOf(session.getAttribute("userName"))
            : "Unknown";
    }

    protected String generateRandomString(
        int length
        , String nama
        , long digitData
    ) {

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

    protected void showLogger(
        String controller
        , String message
        , String type
    ) {
        String logType = type == null ? "info" : type.trim().toLowerCase();
        String logMessage = "[" + controller.toUpperCase() + "] " + message;
        
        switch (logType) {
            case "warn":
                log.warn(logMessage);
                break;
            case "error":
                log.error(logMessage);
                break;
            case "trace":
                log.trace(logMessage);
                break;
            case "debug":
                log.debug(logMessage);
                break;
            default:
                log.info(logMessage);
                break;
        }
    }
    // End of GenerateController
}