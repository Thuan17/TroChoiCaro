///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package POJO;
//import POJO.ModelMessage;
//import java.net.PasswordAuthentication;
//import java.util.Properties;
///**
// *
// * @author thuan
// */
//public class ServiceMail {
//    public ModelMessage sendMain(String toEmail, String code) {
//        ModelMessage ms = new ModelMessage(false, "");
//        String from = "******@gmail.com";
//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true");
//        String username = "******@gmail.com";
//        String password = "******";    //  Your email password here
//        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(from));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
//            message.setSubject("Verify Code");
//            message.setText(code);
//            Transport.send(message);
//            ms.setSuccess(true);
//        } catch (MessagingException e) {
//            if (e.getMessage().equals("Invalid Addresses")) {
//                ms.setMessage("Invalid email");
//            } else {
//                ms.setMessage("Error");
//            }
//        }
//        return ms;
//    }
//
//    
//}
