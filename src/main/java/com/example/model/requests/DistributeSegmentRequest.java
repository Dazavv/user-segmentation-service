package com.example.model.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DistributeSegmentRequest {
    @NotBlank
    private String code;

    @Min(1)
    @Max(100)
    private int percent;
}
