package com.ymmo.ymmoapi.dto;

public class UserCreationDto {
    private String email;
    private String name;
    private String surname;
    private String unhashedPassword;

    public UserCreationDto(String email, String name, String surname, String unhashedPassword) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.unhashedPassword = unhashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUnhashedPassword() {
        return unhashedPassword;
    }
}
