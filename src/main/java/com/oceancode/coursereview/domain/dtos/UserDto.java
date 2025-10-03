package com.oceancode.coursereview.domain.dtos;

import com.oceancode.coursereview.domain.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String email;

    private String name;
    private String program;
    private Integer year;
    private Role role;
    private LocalDateTime createdAt;
}
