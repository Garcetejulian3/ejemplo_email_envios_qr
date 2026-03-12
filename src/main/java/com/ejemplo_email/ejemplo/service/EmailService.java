package com.ejemplo_email.ejemplo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Service
public class EmailService {

    @Autowired
    private QRService qrService;

    @Value("${RESEND_API_KEY}")
    private String apiKey;

    public void sendEmailConQR(String to, String content, String codigo) {

        try {

            byte[] qrBytes = qrService.generarQR(codigo);

            String qrBase64 = Base64.getEncoder().encodeToString(qrBytes);

            URL url = new URL("https://api.resend.com/emails");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);

            String html = """
            <h2>Tu código QR</h2>
            <p>%s</p>
            <img src="data:image/png;base64,%s"/>
            """.formatted(content, qrBase64);

            String json = """
            {
              "from": "onboarding@resend.dev",
              "to": ["%s"],
              "subject": "Tu código QR",
              "html": "%s"
            }
            """.formatted(to, html.replace("\"","\\\""));

            try(OutputStream os = conn.getOutputStream()){
                os.write(json.getBytes());
            }

            int responseCode = conn.getResponseCode();

            if(responseCode != 200){
                throw new RuntimeException("Error enviando email");
            }

        } catch(Exception e){
            throw new RuntimeException("Error enviando email", e);
        }
    }
}