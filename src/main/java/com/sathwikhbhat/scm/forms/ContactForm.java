package com.sathwikhbhat.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactForm {

    @Size(min = 3, message = "Name must be minimum 3 characters")
    private String name;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email address")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    private String phoneNumber;

    @Size(min = 10, message = "Address must be minimum 10 characters")
    private String address;

    private String websiteLink;

    private String linkedinLink;

    private MultipartFile contactImage;

    private boolean favourite;

    @Size(max = 500, message = "Description must be maximum 500 characters")
    private String description;

}