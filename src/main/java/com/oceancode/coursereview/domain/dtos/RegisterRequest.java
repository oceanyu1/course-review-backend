package com.oceancode.coursereview.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    @Pattern(regexp = ".*@cmail\\.carleton\\.ca$",
            message = "Must use a Carleton email address (@cmail.carleton.ca)")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private String program;  // Optional - "Computer Science"
    private Integer year;
}
