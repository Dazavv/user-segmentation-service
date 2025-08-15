package com.example.segmentation.controller;

import com.example.segmentation.model.dto.SegmentDto;
import com.example.segmentation.model.requests.*;
import com.example.segmentation.service.SegmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/segments")
@RequiredArgsConstructor
public class SegmentController {
    private final SegmentService segmentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST', 'VIEWER')")
    public List<SegmentDto> getAllSegments() {
        return segmentService.getAllSegments();
    }

    @GetMapping(path = "{code}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST', 'VIEWER')")
    public SegmentDto getSegmentByCode(@PathVariable String code) {
        return segmentService.getSegmentDtoByCode(code);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST')")
    public SegmentDto addSegment(@Valid @RequestBody CreateSegmentRequest request) {
        return segmentService.addNewSegment(request.getCode(), request.getInfo());
    }

    @PostMapping("{code}/users")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST')")
    public void addUsersToSegment (@PathVariable String code,
                                   @Valid @RequestBody GetUsersRequest request) {
        segmentService.addUsersToSegment(code, request.getUsersId());
    }

    @PostMapping("{code}/distribute")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST')")
    public void distributeSegmentToUsers(@Valid @RequestBody DistributeSegmentRequest request) {
        segmentService.assignSegmentToRandomUsers(request.getCode(), request.getPercent());
    }

    @PutMapping(path = "{code}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void changeSegment(@PathVariable String code,
                              @Valid @RequestBody UpdateSegmentRequest request) {
        segmentService.changeSegment(code, request.getNewCode(), request.getInfo(), request.getUsersId());
    }

    @DeleteMapping(path = "{code}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteSegment(@PathVariable String code) {
        segmentService.deleteSegment(code);
    }

    @DeleteMapping(path = "{code}/users/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST')")
    public void deleteAllUsersInSegment(@PathVariable String code) {
        segmentService.deleteAllUsersInSegment(code);
    }

    @DeleteMapping(path = "{code}/users")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANALYST')")
    public void deleteUsersFromSegment(@PathVariable String code,
                                       @Valid @RequestBody GetUsersRequest request) {
        segmentService.deleteUsersFromSegment(code, request.getUsersId());
    }
}
