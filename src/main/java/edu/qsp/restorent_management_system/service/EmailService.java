package edu.qsp.restorent_management_system.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    public int sendSimpleEmail(String to) {
         Random random = new Random();
        int otp=random.nextInt(1000,9999);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("testing");
        message.setText("Hi "+to+",\r\n" + //
                        "\r\n" + //
                        "Thank you for signing up with TechNest! To complete your signup process, please use the verification code below:\r\n" + //
                        "\r\n" + //
                        "\b"+otp+"\b\r\n" + //
                        "\r\n" + //
                        "This code is valid for the next 10 minutes. Please do not share this code with anyone.\r\n" + //
                        "\r\n" + //
                        "If you didn't request this email, you can safely ignore it.\r\n" + //
                        "\r\n" + //
                        "Best regards,  \r\n" + //
                        "The TechNest Team  \r\n" + //
                        "support@technest.com | www.technest.com\r\n" + //
                        "");
        message.setFrom("sagat56780@gmail.com");

        mailSender.send(message);
        return  otp;
    }
    
}
