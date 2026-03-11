package com.ejemplo_email.ejemplo.model;

public class EmailRequest {
    private String email;
    private String mensaje;

    private String codigo;

    public EmailRequest(String email,String mensaje,String codigo) {
        this.email = email;
        this.mensaje = mensaje;
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
