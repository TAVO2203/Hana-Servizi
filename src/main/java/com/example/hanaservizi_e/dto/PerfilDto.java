package com.example.hanaservizi_e.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilDto {
    @NotBlank(message = "El nombre no puede ir vacio")
    @Size(min = 4, max = 50, message = "El nombre debe tener entre 4 a 50 caracteres")
    private String username;

    @Email
    @NotBlank(message = "El correo no debe estar vacio")
    private String email;

    @NotBlank(message = "El telefono celular es obligatorio")
    @Pattern(regexp = "\\d{7,15}",message = "El telefono debe tener entre 7 y 15 digitos")
    private String phone;

    @NotBlank(message = "Debe ingresar una direccion")
    private String address;

    //Contraseña actual

    private String currentPassword;

    /*@Size(min = 5, max = 100)
    @jakarta.validation.constraints.Pattern(regexp = "^$|.{5,100}",
    message = "La contraseña debe tener al menos 5 caracteres ")*/
    private String newPassword;


    private String confirmPassword;

    // Para vendedores
    private String city;
}
