package edu.qsp.restorent_management_system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.service.EmailService;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {

    // Store OTPs temporarily. A better approach is Redis or DB.
    private Map<String, Integer> otpMap = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @PostMapping("/otp/send")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestParam String to) {
        int otp = emailService.sendSimpleEmail(to);
        otpMap.put(to, otp);
        System.out.println("mail sent to " + to + " with OTP " + otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Email sent successfully!");
        response.put("type", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String otpValue) {
        Map<String, String> response = new HashMap<>();
        try {
            int otpToVerify = Integer.parseInt(otpValue);
            if (otpMap.containsValue(otpToVerify)) {
                response.put("message", "OTP Verified Successfully");
                response.put("type", "success");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Invalid OTP");
                response.put("type", "error");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (NumberFormatException e) {
            response.put("message", "Invalid OTP format");
            response.put("type", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
