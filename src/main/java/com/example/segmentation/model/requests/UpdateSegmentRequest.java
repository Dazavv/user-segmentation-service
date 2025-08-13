package com.example.segmentation.model.requests;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateSegmentRequest {
    private String newCode;
    private String info;
    private Set<Long> usersId;
}
