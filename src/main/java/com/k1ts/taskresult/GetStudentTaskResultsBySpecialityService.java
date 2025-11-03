package com.k1ts.taskresult;

import com.k1ts.subject.SubjectIdDefiner;
import com.k1ts.taskresult.dao.GetStudentTaskResultsBySpecialityDaoService;
import com.k1ts.taskresult.dao.StudentTaskResults;
import com.k1ts.taskresult.request.GetStudentTaskResultsBySpecialityResponse;
import com.k1ts.user.Role;
import com.k1ts.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.k1ts.taskresult.request.GetStudentTaskResultsBySpecialityResponse.*;
import static com.k1ts.taskresult.request.GetStudentTaskResultsBySpecialityResponse.StudentData.*;

@RequiredArgsConstructor
@Service
public class GetStudentTaskResultsBySpecialityService {
    private final UserService userService;
    private final GetStudentTaskResultsBySpecialityDaoService getStudentTaskResultsBySpecialityDaoService;
    private final SubjectIdDefiner subjectIdDefiner;

    public GetStudentTaskResultsBySpecialityResponse getStudentTaskResultsBySpeciality(
            String username, int year, int courseId, int subjectId, int speciality) {

        if (!userService.getById(username).getRole().equals(Role.Admin)) {
            return failed(GetStudentTaskResultsBySpecialityResponse.Error.insufficientPrivileges);
        }

        List<StudentTaskResults> studentTaskResults =
                getStudentTaskResultsBySpecialityDaoService.getStudentTaskResultsBySpeciality(
                        year, courseId, subjectId, speciality);

        Map<Integer, Integer> practiceIdTaskCountMap = getPracticeIdTaskCountMap(year, courseId, subjectId);

        List<StudentData> result = getStudentData(practiceIdTaskCountMap, studentTaskResults);

        for (StudentData studentData : result) {
            calculateAverageMark(studentData);
        }

        result.sort(Comparator.comparing(StudentData::getStudentDetails));

        return success(result);
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

    private List<StudentData> getStudentData(Map<Integer, Integer> practiceIdTaskCountMap, List<StudentTaskResults> studentTaskResults) {
        List<StudentData> result = new ArrayList<>();

        for (StudentTaskResults studentTaskResult : studentTaskResults) {
            String studentDetails = studentTaskResult.getStudentDetails();

            StudentData studentData = result
                    .stream()
                    .filter(filterEntry -> filterEntry.getStudentDetails().equals(studentDetails))
                    .findFirst()
                    .orElse(null);

            if (studentData == null) {
                studentData = new StudentData(studentDetails, new ArrayList<>(), 0);
                result.add(studentData);
            }

            for (Map.Entry<Integer, Integer> entry : practiceIdTaskCountMap.entrySet()) {
                Integer practiceId = entry.getKey();
                Integer taskCount = entry.getValue();

                PracticeIdTaskIdMarks practiceIdTaskIdMarks = studentData
                        .getPracticeIdTaskResults()
                        .stream()
                        .filter(filterEntry -> filterEntry.getPracticeId() == practiceId)
                        .findFirst()
                        .orElse(null);

                if (practiceIdTaskIdMarks == null) {
                    practiceIdTaskIdMarks = new PracticeIdTaskIdMarks(practiceId, new ArrayList<>());
                    studentData.getPracticeIdTaskResults().add(practiceIdTaskIdMarks);

                    for (int taskNumber = 1; taskNumber <= taskCount; taskNumber++) {
                        final int taskId = taskNumber;

                        TaskIdMark taskIdMark = practiceIdTaskIdMarks
                                .getTaskIdMarks()
                                .stream()
                                .filter(filterEntry -> filterEntry.getTaskId() == taskId)
                                .findFirst()
                                .orElse(null);

                        if (taskIdMark == null) {
                            taskIdMark = new TaskIdMark(taskNumber, 0);
                            practiceIdTaskIdMarks.getTaskIdMarks().add(taskIdMark);
                        }
                    }
                }
            }
        }

        for (StudentTaskResults studentTaskResult : studentTaskResults) {
            for (StudentTaskResults.PracticeIdTaskIdMarks practiceIdTaskIdMarks : studentTaskResult.getPracticeIdTaskIdMarks()) {
                for (StudentTaskResults.TaskIdMark taskIdMark : practiceIdTaskIdMarks.getTaskIdMarks()) {
                    TaskIdMark taskMark = result
                            .stream()
                            .filter(entry -> entry.getStudentDetails().equals(studentTaskResult.getStudentDetails()))
                            .flatMap(entry -> entry.getPracticeIdTaskResults().stream().filter(filterEntry -> filterEntry.getPracticeId() == practiceIdTaskIdMarks.getPracticeId()))
                            .flatMap(entry -> entry.getTaskIdMarks().stream().filter(filterEntry -> filterEntry.getTaskId() == taskIdMark.getTaskId()))
                            .findFirst()
                            .orElse(null);

                    if (taskMark == null) {
                        continue;
                    }

                    taskMark.setMark(taskIdMark.getMark());
                }
            }
        }

        return result;
    }

    private void calculateAverageMark(StudentData studentData) {
        int sum = 0;
        int count = 0;

        for (PracticeIdTaskIdMarks practiceIdTaskIdMarks : studentData.getPracticeIdTaskResults()) {
            for (TaskIdMark taskIdMark : practiceIdTaskIdMarks.getTaskIdMarks()) {
                sum += taskIdMark.getMark();
                count++;
            }
        }

        studentData.setAverageMark(BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(count), RoundingMode.CEILING).intValue());
    }
}
