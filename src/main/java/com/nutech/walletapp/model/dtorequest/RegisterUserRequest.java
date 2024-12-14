package com.nutech.walletapp.model.dtorequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {
    
    @NotBlank(message = "Email tidak boleh kosong")
    @Size(max = 100)
    @Email(message = "Paramter email tidak sesuai format")
    private String email;

    @NotBlank(message = "Nama depan tidak boleh kosong")
    @Size(max = 100)
    private String first_name;

    @Size(max = 100)
    private String last_name;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 8, message = "Password minimal 8 karakter")
    private String password;
}
