package com.k1ts.practice.dao;

import com.k1ts.practice.request.PracticeListStudentPermissionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PracticeListStudentPermissionsDaoService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<PracticeListStudentPermissionsResponse.StudentPracticePermission> getStudentPermissions(
            int year, int courseId, int subjectId, int speciality) {

        String sql = "SELECT u.username," +
                " u.last_name," +
                " u.first_name," +
                " CASE" +
                "   WHEN pp.subject_id IS NULL OR" +
                "        NOT EXISTS (" +
                "            SELECT 1 FROM practice_permission pp2" +
                "            WHERE pp2.user_id = u.username" +
                "            AND pp2.\"year\" = :year" +
                "            AND pp2.course_id = :courseId" +
                "            AND pp2.subject_id = :subjectId" +
                "        ) THEN false" +
                "   ELSE pp.allowed" +
                " END AS allowed" +
                " FROM users u" +
                " INNER JOIN student_data sd ON u.username = sd.student_username" +
                " LEFT JOIN practice_permission pp ON u.username = pp.user_id" +
                " WHERE (pp.\"year\" IS NULL OR pp.\"year\" = :year)" +
                " AND (EXTRACT(YEAR FROM sd.first_course_date) = (:year - :courseId + 1) OR pp.course_id = :courseId)" +
                " AND (pp.subject_id IS NULL OR pp.subject_id = :subjectId OR NOT EXISTS(" +
                "   SELECT 1 FROM practice_permission pp2" +
                "   WHERE pp2.user_id = u.username" +
                "   AND pp2.\"year\" = :year" +
                "   AND pp2.course_id = :courseId" +
                "   AND pp2.subject_id = :subjectId))" +
                " AND sd.speciality = :speciality";


        return namedParameterJdbcTemplate.query(sql,
                Map.of("year", year,
                        "courseId", courseId,
                        "subjectId", subjectId,
                        "speciality", speciality),
                rs -> {
                    List<PracticeListStudentPermissionsResponse.StudentPracticePermission> result = new ArrayList<>();

                    while (rs.next()) {

                        String username = rs.getString("username");

                        PracticeListStudentPermissionsResponse.StudentPracticePermission permission = result
                                .stream()
                                .filter(entry -> entry.getUsername().equals(username))
                                .findFirst()
                                .orElse(null);

                        if (permission == null) {

                            permission = PracticeListStudentPermissionsResponse.StudentPracticePermission
                                    .builder()
                                    .username(username)
                                    .build();

                            result.add(permission);
                        }

                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");

                        permission.setFirstName(firstName);
                        permission.setLastName(lastName);

                        boolean allowed = rs.getBoolean("allowed");
                        permission.setAllowed(allowed);
                    }

                    result.sort(Comparator.comparing(PracticeListStudentPermissionsResponse.StudentPracticePermission::getLastName));

                    return result;
                });
    }
}
