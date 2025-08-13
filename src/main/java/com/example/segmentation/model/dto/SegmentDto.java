package com.example.segmentation.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SegmentDto {
    private Long id;

    private String code;
    private String info;
    private Set<Long> userIds;
}
