package com.oceancode.coursereview.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private UUID id;
    private String token;
    private String email;
    private String name;
    private String program;
    private Integer year;
}
