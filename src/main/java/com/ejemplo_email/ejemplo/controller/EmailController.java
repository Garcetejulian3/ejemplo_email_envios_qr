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

    // Enviar email con QR
    @PostMapping("/send/qr")
    public ResponseEntity<?> sendEmailQR(@RequestBody EmailRequest request){

        try {

            emailService.sendEmailConQR(
                    request.getEmail(),
                    request.getMensaje(),
                    request.getCodigo()
            );

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Email enviado correctamente a " + request.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            Map<String, String> error = new HashMap<>();
            error.put("error", "Error enviando email");

            return ResponseEntity.status(500).body(error);
        }
    }

    // Generar QR directamente (para probar desde navegador)
    @GetMapping("/qr")
    public ResponseEntity<byte[]> generarQR(@RequestParam String codigo) throws IOException, WriterException {

        byte[] qr = qrService.generarQR(codigo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(qr);
    }

    // Endpoint para validar QR escaneado
    @PostMapping("/validar")
    public ResponseEntity<String> validarQR(@RequestBody Map<String,String> body){

        String codigo = body.get("codigo");

        return ResponseEntity.ok("Código recibido: " + codigo);
    }
}