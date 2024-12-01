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
     @Autowired
    private EmailService emailService;
    @PostMapping("/send")
    public String sendEmail(@RequestParam String to) {
        emailService.sendSimpleEmail(to);
        System.out.println("mail send");
        return "Email sent successfully!";
    }
    
}
