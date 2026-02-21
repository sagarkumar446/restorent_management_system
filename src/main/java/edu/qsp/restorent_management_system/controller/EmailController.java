package edu.qsp.restorent_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.service.EmailService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {
    int otp;
    @Autowired
    private EmailService emailService;

    @PostMapping("/otp/send")
    public String sendOtp(@RequestParam String to) {
        otp = emailService.sendSimpleEmail(to);
        System.out.println("mail send");
        return "Email sent successfully!";
    }

    @PostMapping("/otp/verify")
    public String verifyOtp(@RequestParam String otpValue) {
        if (otpValue.equals(String.valueOf(otp))) {
            return "Login Success";
        } else {
            return "Login Failed";
        }

    }

}
