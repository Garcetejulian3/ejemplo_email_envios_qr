package com.ejemplo_email.ejemplo.controller;

import com.ejemplo_email.ejemplo.model.EmailRequest;
import com.ejemplo_email.ejemplo.service.EmailService;
import com.ejemplo_email.ejemplo.service.QRService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private QRService qrService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String content){
        emailService.sendEmail(to,subject,content);
        return "Se envio correctamente el email con asunto" + subject + " a" + to;
    }

    @PostMapping("/send/qr")
    public ResponseEntity<?> sendEmailQR(@RequestBody EmailRequest request){

        try {

            emailService.sendEmailConQR(
                    request.getEmail(),
                    request.getMensaje(),
                    request.getCodigo()
            );

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Se envió correctamente el email a: " + request.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());

            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/qr")
    public ResponseEntity<byte[]> generarQR(@RequestParam String codigo) throws IOException, WriterException {
        byte[] qr = qrService.generarQR(codigo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(qr);
    }


    @PostMapping("/validar")
    public ResponseEntity<String> validarQR(@RequestBody Map<String,String> body){
        String codigo = body.get("codigo");

        return ResponseEntity.ok("Codigo recibido: " + codigo);
    }
}
