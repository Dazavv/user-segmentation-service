package com.example.segmentation.controller;

import com.example.segmentation.model.dto.SegmentDto;
import com.example.segmentation.model.requests.CreateSegmentRequest;
import com.example.segmentation.model.requests.DistributeSegmentRequest;
import com.example.segmentation.model.requests.UpdateSegmentRequest;
import com.example.segmentation.service.SegmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public SegmentDto addSegment(@RequestBody @Valid CreateSegmentRequest request) {
        return segmentService.addNewSegment(request.getCode(), request.getInfo());
    }

    @PostMapping("{code}/users")
    public void addUsersToSegment(@PathVariable String code,
                                  @RequestParam Set<Long> usersId) {
        segmentService.addUsersToSegment(code, usersId);
    }

    @PostMapping("{code}/distribute")
    public void distributeSegmentToUsers(@RequestBody @Valid DistributeSegmentRequest request) {
        segmentService.assignSegmentToRandomUsers(request.getCode(), request.getPercent());
    }

    @PutMapping(path = "{code}")
    public void changeSegment(@PathVariable String code,
                              @RequestBody @Valid UpdateSegmentRequest request) {
        segmentService.changeSegment(code, request.getNewCode(), request.getInfo(), request.getUsersId());
    }

    @DeleteMapping(path = "{code}")
    public void deleteSegment(@PathVariable String code) {
        segmentService.deleteSegment(code);
    }

    @DeleteMapping(path = "{code}/users/all")
    public void deleteAllUsersInSegment(@PathVariable String code) {
        segmentService.deleteAllUsersInSegment(code);
    }

    @DeleteMapping(path = "{code}/users")
    public void deleteUsersFromSegment(@PathVariable String code,
                                       @RequestParam Set<Long> usersId) {
        segmentService.deleteUsersFromSegment(code, usersId);
    }
}
