package com.example.service;

import com.example.exceptions.IllegalRequestException;
import com.example.exceptions.NotFoundException;
import com.example.model.dto.SegmentDto;
import com.example.model.entity.Segment;
import com.example.model.entity.User;
import com.example.repository.SegmentRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SegmentService {
    private final SegmentRepository segmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<SegmentDto> getAllSegments() {
        return segmentRepository.findAll()
                .stream()
                .map(segment -> modelMapper.map(segment, SegmentDto.class))
                .toList();
    }

    public SegmentDto getSegmentDtoByCode(String code) {
        Segment segment = getExistedSegmentByCode(code);
        return modelMapper.map(segment, SegmentDto.class);
    }

    public SegmentDto addNewSegment(String code, String info) {
        Optional<Segment> existedSegment = segmentRepository.findByCode(code);
        if (existedSegment.isEmpty()) throw new IllegalRequestException("segment with code = " + code + " already exists");

        Segment segment = new Segment();
        segment.setCode(code);
        segment.setInfo(info);
        Segment savedSegment = segmentRepository.save(segment);

        return modelMapper.map(savedSegment, SegmentDto.class);
    }

    public void changeSegment(String code, String newCode, String info, Set<User> users) {
        Segment segment = getExistedSegmentByCode(code);

        if (newCode != null && !segment.getCode().equals(newCode)) {
            Optional<Segment> checkedSegment = segmentRepository.findByCode(newCode);
            if (checkedSegment.isEmpty()) throw new IllegalRequestException("segment with code = " + code + " already exists");
            segment.setCode(code);
        }

        if (info != null && !segment.getInfo().equals(info)) {
            segment.setInfo(info);
        }
        if (!users.isEmpty() && !segment.getUsers().equals(users)) {
            segment.setUsers(users);
        }

        segmentRepository.save(segment);
    }

    public void deleteSegment(String code) {
        Segment segment = getExistedSegmentByCode(code);
        segmentRepository.delete(segment);
    }

    public void assignSegmentToRandomUsers(String code, int percent) {
        Segment savedSegment = getExistedSegmentByCode(code);

        List<User> allUsers = userRepository.findAll();
        Set<User> usersAlreadyAssign = savedSegment.getUsers();

        int usersCount = allUsers.size();
        int usersAlreadyAssignCount = usersAlreadyAssign.size();
        int usersNeedToAssign = (int) Math.ceil(usersCount * (percent / 100.0));

        List<User> availableUsers  = allUsers.stream()
                .filter(user -> !usersAlreadyAssign.contains(user))
                .sorted(Comparator.comparingInt(user -> user.getSegments().size()))
                .limit(usersNeedToAssign - usersAlreadyAssignCount)
                .toList();
        usersAlreadyAssign.addAll(availableUsers);
//        Segment updatedSegment = savedSegment.get();
//        updatedSegment.setUsers(usersAlreadyAssign);
        segmentRepository.save(savedSegment);


        //если usersAlreadyAssignCount > UsersNeedToAssign -> часть надо удалить

    }

    public void addUsersToSegment(String code, Set<Long> usersId) {
        Segment segment = getExistedSegmentByCode(code);
        List<User> usersToAdd = userRepository.findAllById(usersId);
        if (usersToAdd.size() != usersId.size()) throw new NotFoundException("not all users with id = " + usersId + " was found");

        segment.getUsers().addAll(usersToAdd);
        segmentRepository.save(segment);
    }

    public void deleteUsersFromSegment(String code, Set<Long> usersId) {
        Segment segment = getExistedSegmentByCode(code);
        List<User> usersToDelete = userRepository.findAllById(usersId);
        if (usersToDelete.size() != usersId.size()) throw new NotFoundException("not all users with id = " + usersId + " was found");


        segment.getUsers().removeAll(new HashSet<>(usersToDelete));
        segmentRepository.save(segment);
    }

    public Segment getExistedSegmentByCode(String code) {
        Optional<Segment> savedSegment = segmentRepository.findByCode(code);
        if (savedSegment.isEmpty()) throw new NotFoundException("segment with code = " + code + " not found");

        return savedSegment.get();
    }
}
