package com.example.sezar.activity;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import java.util.Properties;


public class SMTPAuthenticator extends javax.mail.Authenticator {
    private static final String SMTP_AUTH_USER = "appcaesars@gmail.com";
    private static final String SMTP_AUTH_PWD  = "odanobunaga1";
    public PasswordAuthentication getPasswordAuthentication() {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }
}
