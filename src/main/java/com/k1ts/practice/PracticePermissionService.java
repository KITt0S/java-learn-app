package com.k1ts.practice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PracticePermissionService {
    private final PracticePermissionRepository practicePermissionRepository;

    public PracticePermission save(PracticePermission practicePermission) {
        return practicePermissionRepository.save(practicePermission);
    }

    public boolean isPracticesAllowed(String username, int year, int courseId, int subjectId) {
        return practicePermissionRepository.isPracticesAllowed(username, year, courseId, subjectId);
    }
}
