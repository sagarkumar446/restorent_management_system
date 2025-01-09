package edu.qsp.restorent_management_system.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.Configuration.ResponseStructure;
import edu.qsp.restorent_management_system.enums.StatusType;
import edu.qsp.restorent_management_system.service.EmailService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpSession;

@RestController
 @RequestMapping("/api")
 @CrossOrigin(origins = "http://localhost:3000")
 public class EmailController {
    @Autowired
    ResponseStructure<String> responseStructureString;
    int otp;
    String email;
    @Autowired
    private EmailService emailService;
    @PostMapping("/send")
    public ResponseEntity<ResponseStructure<String>> sendEmail(@RequestParam String to) {
            email=to;
            otp=emailService.sendSimpleEmail(to);
            responseStructureString.setData(to);
            responseStructureString.setMessage("OTP sent to your email "+to+"");
            responseStructureString.setType(StatusType.SUCCESS);
            responseStructureString.setStatusCode(200);
            return new ResponseEntity<>(responseStructureString,HttpStatus.ACCEPTED);
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<ResponseStructure<String>> stMethodName(@RequestParam String otpData,HttpSession session) {
        session.setAttribute("email", email);
        if(otpData.equals(String.valueOf(otp))){
            responseStructureString.setData(email);
            responseStructureString.setMessage("Login Success"); 
            responseStructureString.setType(StatusType.SUCCESS);   
            responseStructureString.setStatusCode(200); 
            return new ResponseEntity<>(responseStructureString,HttpStatus.ACCEPTED);
        }
        else
        {
            responseStructureString.setData(otpData);
            responseStructureString.setMessage("Invalid OTP");  
            responseStructureString.setType(StatusType.ERROR);
            responseStructureString.setStatusCode(406);
            return new ResponseEntity<>(responseStructureString,HttpStatus.ACCEPTED);
        }
    }
    @PostMapping("/newUser")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
    
}
