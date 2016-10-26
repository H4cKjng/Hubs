package com.miner.droidminer.modules;


import android.util.Log;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Kopites on 10/18/2016.
 */

public class Mailer {
    public void sendMail(
            String server,
            String from, String to,
            String subject, String text,
            final String login,
            final String password,
            String fileName
    ) {
        Properties properties = new Properties();
        properties.put ("mail.smtp.host", server);
        properties.put ("mail.smtp.socketFactory.port", "465");
        properties.put ("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put ("mail.smtp.auth", "true");
        properties.put ("mail.smtp.port", "465");
        try {
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login, password);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress (to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart ();
            BodyPart bodyPart = new MimeBodyPart ();

            bodyPart.setContent (text, "text/html; charset=utf-8");
            multipart.addBodyPart (bodyPart);

            if (fileName != null) {
                bodyPart.setDataHandler (new DataHandler (new FileDataSource (fileName)));
                bodyPart.setFileName (fileName);
                multipart.addBodyPart (bodyPart);
            }
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            Log.e("Send Mail Error", e.getMessage());
        }
    }
}
