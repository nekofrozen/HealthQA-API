package com.seproject.healthqa.web.payload;

import javax.validation.constraints.*;


public class SignUpRequest {
    
    @NotNull
    @Size(min = 1, max = 255)
    private String firstname;

    
    @NotNull
    @Size(min = 1, max = 255)
    private String lastname;

    
    @NotNull
    @Size(min = 1, max = 255)
    private String username;
    
    @NotNull
    @Size(min = 8, max = 255)
    private String password;
   
    @NotNull
    @Size(min = 1, max = 255)
    @Email
    private String email;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

}
