package com.sathwikhbhat.scm.forms;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {
    @Size(min = 3, message = "Name must be minimum 3 characters")
    private String name;

    @Email(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email address"
    )
    private String email;

    @Size(min = 6, message = "Password must be minimum 6 characters")
    private String password;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull(message = "You must accept the terms and conditions")
    @AssertTrue(message = "You must accept the terms and conditions")
    private Boolean tnc;
}