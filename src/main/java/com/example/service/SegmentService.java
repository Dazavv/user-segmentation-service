package com.example.service;

import com.example.exceptions.IllegalRequestException;
import com.example.exceptions.NotFoundException;
import com.example.model.dto.SegmentDto;
import com.example.model.entity.Segment;
import com.example.model.entity.User;
import com.example.repository.SegmentRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SegmentService {
    private final SegmentRepository segmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public List<SegmentDto> getAllSegments() {
        List<SegmentDto> segments = segmentRepository.findAll()
                .stream()
                .map(segment -> {
                    SegmentDto dto = modelMapper.map(segment, SegmentDto.class);
                    dto.setUserIds(segment.getUsers().stream()
                            .map(User::getId)
                            .collect(Collectors.toSet()));
                    return dto;
                })
                .toList();

        log.info("Fetched {} segments: {}", segments.size(), segments);

        return segments;
    }


    public SegmentDto getSegmentDtoByCode(String code) {
        Segment segment = getExistedSegmentByCode(code);
        SegmentDto dto = modelMapper.map(segment, SegmentDto.class);
        dto.setUserIds(
                segment.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet())
        );
        return dto;
    }

    public SegmentDto addNewSegment(String code, String info) {
        Optional<Segment> existedSegment = segmentRepository.findByCode(code);
        if (existedSegment.isPresent()) throw new IllegalRequestException("Segment with code = " + code + " already exists");

        Segment segment = new Segment();
        segment.setCode(code);
        segment.setInfo(info);
        Segment savedSegment = segmentRepository.save(segment);

        return modelMapper.map(savedSegment, SegmentDto.class);
    }

    public void changeSegment(String code, String newCode, String info, Set<Long> usersId) {
        Segment segment = getExistedSegmentByCode(code);

        if (newCode != null && !segment.getCode().equals(newCode)) {
            Optional<Segment> checkedSegment = segmentRepository.findByCode(newCode);
            if (checkedSegment.isPresent()) throw new IllegalRequestException("Segment with code = " + newCode + " already exists");
            segment.setCode(newCode);
        }

        if (info != null && !segment.getInfo().equals(info)) {
            segment.setInfo(info);
        }

        if (!usersId.isEmpty()) {
            List<User> usersToChange = userService.getExistedUsersById(usersId);
            segment.setUsers(new HashSet<>(usersToChange));
        }

        segmentRepository.save(segment);
    }

    public void deleteSegment(String code) {
        Segment segment = getExistedSegmentByCode(code);
        segmentRepository.delete(segment);
    }

    public void assignSegmentToRandomUsers(String code, int percent) {
        Segment savedSegment = getExistedSegmentByCode(code);
        Set<User> usersAlreadyAssign = savedSegment.getUsers();

        List<User> availableUsers;
        List<User> allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) throw new NotFoundException("No users are available. Table \"users\" is empty.");

        int usersCount = allUsers.size();
        int usersAlreadyAssignCount = usersAlreadyAssign.size();
        int usersNeedToAssign = (int) Math.ceil(usersCount * (percent / 100.0));

        if (usersNeedToAssign > usersAlreadyAssignCount) {
            availableUsers = allUsers.stream()
                    .filter(user -> !usersAlreadyAssign.contains(user))
                    .collect(Collectors.toList());

            Collections.shuffle(availableUsers);

            availableUsers = availableUsers.stream()
                    .limit(usersNeedToAssign - usersAlreadyAssignCount)
                    .toList();

            usersAlreadyAssign.addAll(availableUsers);
        } else {
            List<User> shuffledUsers = new ArrayList<>(usersAlreadyAssign);
            Collections.shuffle(shuffledUsers);

            int needToDeleteCount = usersAlreadyAssignCount - usersNeedToAssign;

            availableUsers = shuffledUsers.stream()
                    .skip(needToDeleteCount)
                    .collect(Collectors.toList());

            savedSegment.setUsers(new HashSet<>(availableUsers));
        }
        segmentRepository.save(savedSegment);
    }

    public void addUsersToSegment(String code, Set<Long> usersId) {
        Segment segment = getExistedSegmentByCode(code);
        List<User> usersToAdd = userService.getExistedUsersById(usersId);

        segment.getUsers().addAll(usersToAdd);
        segmentRepository.save(segment);
    }

    public void deleteUsersFromSegment(String code, Set<Long> usersId) {
        Segment segment = getExistedSegmentByCode(code);
        List<User> usersToDelete = userService.getExistedUsersById(usersId);

        Set<User> segmentUsers = segment.getUsers();
        boolean allPresent = segmentUsers.containsAll(usersToDelete);

        if (!allPresent) {
            log.warn("Not all users are in the segment. Users to delete: IDs = {}, users in the segment: IDs = {}",
                    usersToDelete.stream().map(User::getId).toList(),
                    segmentUsers.stream().map(User::getId).toList());
        }

        segment.getUsers().removeAll(new HashSet<>(usersToDelete));
        segmentRepository.save(segment);
    }

    public Segment getExistedSegmentByCode(String code) {
        Optional<Segment> savedSegment = segmentRepository.findByCode(code);
        if (savedSegment.isEmpty()) throw new NotFoundException("Segment with code = " + code + " not found");

        return savedSegment.get();
    }

    public void deleteAllUsersInSegment(String code) {
        Segment segment = getExistedSegmentByCode(code);
        segment.setUsers(new HashSet<>());
        segmentRepository.save(segment);
    }
}
