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
public class DepartmentDto {
    private UUID id;

    private String departmentCode;

    private String name;
}
