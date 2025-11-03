package com.k1ts.taskresult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class TaskResultService {
    private final TaskResultRepository taskResultRepository;

    public boolean exists(TaskResultCompositeId id) {
        return taskResultRepository.existsById(id);
    }

    public TaskResult getById(TaskResultCompositeId id) {
        return taskResultRepository.findById(id).orElse(null);
    }

    public TaskResult save(TaskResult taskResult) {
        return taskResultRepository.save(taskResult);
    }

    public boolean isTaskPassed(String username, int year, int courseId, int subjectId, int practiceId, int taskId) {
        Boolean taskPassed = taskResultRepository.isTaskPassed(username, year, courseId, subjectId, practiceId, taskId);

        if (taskPassed == null) {
            return false;
        }

        return taskPassed;
    }

    public String getCode(String username, int year, int courseId, int subjectId, int practiceId, int taskId) {
        return taskResultRepository.getCode(username, year, courseId, subjectId, practiceId, taskId);
    }

    public boolean isEmpty() {
        return taskResultRepository.count() == 0;
    }

    public void deleteAll() {
        taskResultRepository.deleteAll();
    }
}
