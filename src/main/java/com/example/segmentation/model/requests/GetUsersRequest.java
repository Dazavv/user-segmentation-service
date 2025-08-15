package com.example.segmentation.model.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class GetUsersRequest {
    @NotBlank
    private Set<Long> usersId;
}
