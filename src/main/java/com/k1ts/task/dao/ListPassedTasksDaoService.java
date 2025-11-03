package com.k1ts.task.dao;

import com.k1ts.task.request.ListPassedTasksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ListPassedTasksDaoService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Map<Integer, List<Integer>> listPassedTasks(String studentUsername, int courseId, int subjectId) {

        return namedParameterJdbcTemplate.query("""
                        SELECT practice_id, task_id FROM task_result WHERE user_id = :studentUsername AND course_id = :courseId AND subject_id = :subjectId
                        """,
                Map.of("studentUsername", studentUsername, "courseId", courseId, "subjectId", subjectId),
                (rs) -> {
                    Map<Integer, List<Integer>> result = new HashMap<>();

                    while (rs.next()) {
                        int practiceId = rs.getInt("practice_id");
                        result.putIfAbsent(practiceId, new ArrayList<>());

                        int taskId = rs.getInt("task_id");
                        result.get(practiceId).add(taskId);
                    }

                    return result;
                });
    }
}
