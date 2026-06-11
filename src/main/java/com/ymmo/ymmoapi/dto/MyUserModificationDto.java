package com.ymmo.ymmoapi.dto;

public class MyUserModificationDto extends UserCreationDto {
    private String oldPassword;

    public MyUserModificationDto(String email, String name, String surname, String unhashedPassword) {
        super(email, name, surname, unhashedPassword);
    }

    public String getOldPassword() {
        return oldPassword;
    }
}
