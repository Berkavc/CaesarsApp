package com.example.sezar.encryption_algorithms;

public class CaesarsCiphering {
    public static String caesarsEncryption(String message, int key) {
        String encryptedMessage = "";
        char ch;
        for (int i = 0; i < message.length(); ++i) {
            ch = message.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                ch = (char) (ch + key);

                if (ch > 'z') {
                    ch = (char) (ch - 'z' + 'a' - 1);
                }
                encryptedMessage += ch;
            } else if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch + key);

                if (ch > 'Z') {
                    ch = (char) (ch - 'Z' + 'A' - 1);
                }

                encryptedMessage += ch;
            } else {
                encryptedMessage += ch;
            }
        }
        return encryptedMessage;
    }

    public static String caesarsDecryption(String message, int key) {
        String decryptedMessage = "";
        char ch;

        for (int i = 0; i < message.length(); ++i) {
            ch = message.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                ch = (char) (ch - key);

                if (ch < 'a') {
                    ch = (char) (ch + 'z' - 'a' + 1);
                }

                decryptedMessage += ch;
            } else if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch - key);

                if (ch < 'A') {
                    ch = (char) (ch + 'Z' - 'A' + 1);
                }

                decryptedMessage += ch;
            } else {
                decryptedMessage += ch;
            }
        }
        return decryptedMessage;

    }
}
