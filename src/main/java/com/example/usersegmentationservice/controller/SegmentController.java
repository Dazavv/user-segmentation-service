package com.example.usersegmentationservice.controller;

import com.example.usersegmentationservice.model.Segment;
import com.example.usersegmentationservice.model.User;
import com.example.usersegmentationservice.service.SegmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "segments")
public class SegmentController {
    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @GetMapping
    public Page<Segment> getAllSegments(@PageableDefault(sort = "code") Pageable pageable) {
        return segmentService.getAllSegments(pageable);
    }

    @GetMapping(path = "{code}")
    public Segment getSegmentByCode(@PathVariable String code) {
        return segmentService.getSegmentByCode(code);
    }

    @PostMapping
    public Segment addSegment(@RequestBody Segment segment) {
        return segmentService.addNewSegment(segment);
    }

    @PostMapping("/segments/{code}/distribute")
    public void distributeSegmentToUsers(@RequestBody String code,
                                            @RequestParam int percent) {
        segmentService.assignSegmentToRandomUsers(code, percent);
    }

    @PutMapping(path = "{code}")
    public Segment changeSegment(@RequestParam String code,
                                 @RequestParam(required = false) String info,
                                 @RequestParam(required = false) Set<User> users) {
        return segmentService.changeSegment(code, info, users);
    }

    @DeleteMapping(path = "{code}")
    public void deleteSegment(@PathVariable String code) {
        segmentService.deleteSegment(code);
    }

}
