package com.example.metro_booking_app;

import android.os.Handler;
import android.os.Looper;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTask {

    private final String recipientEmail;
    private final String subject;
    private final String messageBody;
    private final MailCallback mailCallback;
    private static final String SENDER_EMAIL = "taivo331@gmail.com";
    private static final String SENDER_PASSWORD = "clqg ytci bwze rsyc"; // Gợi ý: nên đọc từ file bảo mật sau

    public interface MailCallback {
        void onMailSent(boolean success);
    }

    public SendMailTask(String recipientEmail, String subject, String messageBody, MailCallback mailCallback) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.messageBody = messageBody;
        this.mailCallback = mailCallback;
    }

    public void send() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean success;
            try {
                Session session = createSession();
                Message message = createMessage(session);
                Transport.send(message);
                success = true;
            } catch (MessagingException e) {
                e.printStackTrace();
                success = false;
            }
            final boolean finalSuccess = success; // THÊM final ở đây!

            new Handler(Looper.getMainLooper()).post(() -> {
                if (mailCallback != null) {
                    mailCallback.onMailSent(finalSuccess);
                }
            });
        });
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
    }

    private Message createMessage(Session session) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }
}
