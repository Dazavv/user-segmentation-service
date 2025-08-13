package com.example.model.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSegmentRequest {
    @NotBlank
    private String code;
    private String info;
}
