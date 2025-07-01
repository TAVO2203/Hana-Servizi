package com.example.hanaservizi_e.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroVendedorDTO extends RegistroUsuarioDTO {
    private String city;
}
