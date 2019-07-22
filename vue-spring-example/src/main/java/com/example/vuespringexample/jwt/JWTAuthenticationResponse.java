package com.example.vuespringexample.jwt;

public class JWTAuthenticationResponse {
    private String authorization;

    public JWTAuthenticationResponse() {
    }

    public JWTAuthenticationResponse(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

}
