package com.k1ts.taskresult.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GetStudentTaskResultsDaoService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Map<Integer, Map<Integer, Integer>> getPracticeIdTaskIdMarkMap(int year, int courseId, int subjectId, String studentUsername) {
        return namedParameterJdbcTemplate.query(
                "SELECT practice_id, task_id, mark FROM task_result WHERE" +
                        " \"year\" = :year" +
                        " AND course_id = :courseId" +
                        " AND subject_id = :subjectId" +
                        " AND user_id = :studentUsername",
                Map.of("year", year,
                        "courseId", courseId,
                        "subjectId", subjectId,
                        "studentUsername", studentUsername),
                rs -> {
                    Map<Integer, Map<Integer, Integer>> result = new HashMap<>();

                    while (rs.next()) {
                        int practiceId = rs.getInt("practice_id");

                        result.putIfAbsent(practiceId, new HashMap<>());

                        int taskId = rs.getInt("task_id");

                        Integer mark = result.get(practiceId).get(taskId);

                        if (mark == null) {
                            mark = rs.getInt("mark");
                            result.get(practiceId).put(taskId, mark);
                        }
                    }

                    return result;
                });
    }
}
