package covidapp.covid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@covidapp.com");
            message.setTo(toEmail);
            message.setSubject("Your OTP for COVID Dashboard Login");
            message.setText("Hello,\n\n" +
                    "Your One-Time Password (OTP) for login is: " + otp + "\n\n" +
                    "This OTP is valid for 10 minutes. Please do not share this code with anyone.\n\n" +
                    "If you did not request this OTP, please ignore this email.\n\n" +
                    "Best regards,\nCOVID Dashboard Team");

            mailSender.send(message);
            System.out.println("OTP email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}

