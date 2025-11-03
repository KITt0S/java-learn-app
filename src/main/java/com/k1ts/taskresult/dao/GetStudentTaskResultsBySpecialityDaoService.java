package com.k1ts.taskresult.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GetStudentTaskResultsBySpecialityDaoService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<StudentTaskResults> getStudentTaskResultsBySpeciality(
            int year, int courseId, int subjectId, int speciality) {

        return namedParameterJdbcTemplate.query(
                "SELECT u.first_name," +
                        " u.last_name," +
                        " tr.practice_id," +
                        " tr.task_id," +
                        " tr.mark" +
                        " FROM users u INNER JOIN student_data sd ON u.username = sd.student_username LEFT JOIN task_result tr ON u.username = tr.user_id WHERE" +
                        " tr.\"year\" = :year" +
                        " AND tr.course_id = :courseId" +
                        " AND tr.subject_id = :subjectId" +
                        " AND sd.speciality = :speciality",
                Map.of("year", year,
                        "courseId", courseId,
                        "subjectId", subjectId,
                        "speciality", speciality),
                rs -> {
                    List<StudentTaskResults> result = new ArrayList<>();

                    while (rs.next()) {
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");

                        StudentTaskResults studentTaskResults = result
                                .stream()
                                .filter(entry -> entry.getStudentDetails().equals(lastName + " " + firstName))
                                .findFirst()
                                .orElse(null);

                        if (studentTaskResults == null) {
                            studentTaskResults = new StudentTaskResults(
                                    lastName + " " + firstName,
                                    new ArrayList<>());

                            result.add(studentTaskResults);
                        }

                        int practiceId = rs.getInt("practice_id");

                        if (!rs.wasNull()) {
                            StudentTaskResults.PracticeIdTaskIdMarks practiceIdTaskIdMarks = studentTaskResults
                                    .getPracticeIdTaskIdMarks()
                                    .stream()
                                    .filter(entry -> entry.getPracticeId() == practiceId)
                                    .findFirst()
                                    .orElse(null);

                            if (practiceIdTaskIdMarks == null) {
                                practiceIdTaskIdMarks = new StudentTaskResults.PracticeIdTaskIdMarks(practiceId, new ArrayList<>());
                                studentTaskResults.getPracticeIdTaskIdMarks().add(practiceIdTaskIdMarks);
                            }

                            int taskId = rs.getInt("task_id");
                            int mark = rs.getInt("mark");

                            if (!rs.wasNull()) {
                                StudentTaskResults.TaskIdMark taskIdMark = practiceIdTaskIdMarks
                                        .getTaskIdMarks()
                                        .stream()
                                        .filter(entry -> entry.getTaskId() == taskId)
                                        .findFirst()
                                        .orElse(null);

                                if (taskIdMark == null) {
                                    taskIdMark = new StudentTaskResults.TaskIdMark(taskId, mark);
                                    practiceIdTaskIdMarks.getTaskIdMarks().add(taskIdMark);
                                }
                            }
                        }
                    }

                    return result;
                });
    }
}
