package com.k1ts.taskresult;

import com.k1ts.subject.SubjectIdDefiner;
import com.k1ts.taskresult.dao.GetStudentTaskResultsDaoService;
import com.k1ts.taskresult.request.GetStudentTaskResultsResponse;
import com.k1ts.user.Role;
import com.k1ts.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GetStudentTaskResultsService {
    private final UserService userService;
    private final GetStudentTaskResultsDaoService getStudentTaskResultsDaoService;
    private final SubjectIdDefiner subjectIdDefiner;

    public GetStudentTaskResultsResponse getStudentTaskResults(
            String principalUsername, int year, int courseId, int subjectId, String studentUsername) {
        if (!userService.getById(principalUsername).getRole().equals(Role.Admin) && !principalUsername.equals(studentUsername)) {
            return GetStudentTaskResultsResponse.failed(GetStudentTaskResultsResponse.Error.insufficientPrivileges);
        }

        Map<Integer, Map<Integer, Integer>> initialPracticeIdTaskIdMarkMap = getStudentTaskResultsDaoService
                .getPracticeIdTaskIdMarkMap(year, courseId, subjectId, studentUsername);

        Map<Integer, Integer> practiceIdTaskCountMap = getPracticeIdTaskCountMap(year, courseId, subjectId);

        Map<Integer, Map<Integer, Integer>> practiceIdTaskIdMarkMap = getPracticeIdTaskIdMarkMap(
                initialPracticeIdTaskIdMarkMap, practiceIdTaskCountMap);

        return GetStudentTaskResultsResponse.success(
                practiceIdTaskIdMarkMap,
                calculateAverageMark(practiceIdTaskIdMarkMap));
    }

    private Map<Integer, Integer> getPracticeIdTaskCountMap(int year, int courseId, int subjectId) {
        Map<Integer, Integer> result = new HashMap<>();

        String practicesLink = "materials/" + year + "/" + courseId + "_course/" + subjectIdDefiner.getSubjectIdAsText(courseId, subjectId) + "/practices";

        File[] practices = new File(practicesLink).listFiles();

        if (practices == null) {
            return Collections.emptyMap();
        }

        for (int i = 1; i <= practices.length; i++) {
            String practiceLink = practicesLink + "/practice_" + i;

            File[] tasks = new File(practiceLink).listFiles();

            if (tasks == null) {
                result.put(i, 0);
            } else {
                result.put(i, tasks.length);
            }
        }

        return result;
    }

    private Map<Integer, Map<Integer, Integer>> getPracticeIdTaskIdMarkMap(
            Map<Integer, Map<Integer, Integer>> initialPracticeIdTaskIdMarkMap, Map<Integer, Integer> practiceIdTaskCountMap) {
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : practiceIdTaskCountMap.entrySet()) {
            Integer practiceId = entry.getKey();
            Integer taskCount = entry.getValue();

            result.putIfAbsent(practiceId, new HashMap<>());

            for (int taskNumber = 1; taskNumber <= taskCount; taskNumber++) {
                result.get(practiceId).put(taskNumber, 0);
            }
        }

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : initialPracticeIdTaskIdMarkMap.entrySet()) {
            Integer practiceId = entry.getKey();

            Map<Integer, Integer> taskIdMarkMap = entry.getValue();

            for (Map.Entry<Integer, Integer> secondEntry : taskIdMarkMap.entrySet()) {
                Integer taskId = secondEntry.getKey();
                Integer mark = secondEntry.getValue();

                result.get(practiceId).put(taskId, mark);
            }
        }

        return result;
    }

    private int calculateAverageMark(Map<Integer, Map<Integer, Integer>> practiceIdTaskIdMarkMap) {
        int sum = 0;
        int count = 0;

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : practiceIdTaskIdMarkMap.entrySet()) {
            Integer practiceId = entry.getKey();
            Map<Integer, Integer> taskNumberMarkMap = entry.getValue();
            for (Map.Entry<Integer, Integer> secondEntry : taskNumberMarkMap.entrySet()) {
                Integer taskNumber = secondEntry.getKey();
                Integer mark = secondEntry.getValue();

                sum += mark;
                count++;
            }
        }
        
        if (count == 0) {
            return 0;
        }

        return BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(count), RoundingMode.CEILING).intValue();
    }
}
