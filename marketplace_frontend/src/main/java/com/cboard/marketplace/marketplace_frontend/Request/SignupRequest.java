package com.cboard.marketplace.marketplace_frontend.Request;

public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    public SignupRequest(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}