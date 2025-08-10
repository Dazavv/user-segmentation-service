package com.example.usersegmentationservice.controller;

import com.example.usersegmentationservice.model.Segment;
import com.example.usersegmentationservice.model.User;
import com.example.usersegmentationservice.service.SegmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "segments")
public class SegmentController {
    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @GetMapping
    public List<Segment> getAllSegments() {
        return segmentService.getAllSegments();
    }

    @GetMapping(path = "{code}")
    public Segment getSegmentByCode(@PathVariable String code) {
        return segmentService.getSegmentByCode(code);
    }

    @PostMapping
    public Segment addSegment(@RequestBody Segment segment) {
        return segmentService.addNewSegment(segment);
    }

    @PostMapping("{code}")
    public void addUsersToSegment(@PathVariable String code,
                                  @RequestParam Set<User> users) {
        segmentService.addUsersToSegment(code, users);
    }

    @PostMapping("{code}/distribute")
    public void distributeSegmentToUsers(@PathVariable String code,
                                         @RequestParam int percent) {
        segmentService.assignSegmentToRandomUsers(code, percent);
    }

    @PutMapping(path = "{code}")
    public void changeSegment(@PathVariable String code,
                                 @RequestParam(required = false) String newCode,
                                 @RequestParam(required = false) String info,
                                 @RequestParam(required = false) Set<User> users) {
        segmentService.changeSegment(code, newCode, info, users);
    }

    @DeleteMapping(path = "{code}")
    public void deleteSegment(@PathVariable String code) {
        segmentService.deleteSegment(code);
    }

    @DeleteMapping(path = "{code}")
    public void deleteUsersFromSegment(@PathVariable String code,
                                       @RequestParam Set<User> users) {
        segmentService.deleteUsersFromSegment(code, users);
    }
}
