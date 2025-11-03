package com.k1ts.taskresult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TaskResultRepository extends JpaRepository<TaskResult, TaskResultCompositeId> {

    @Query(nativeQuery = true, value = "SELECT passed FROM task_result WHERE" +
            " user_id = :username" +
            " AND \"year\" = :year" +
            " AND course_id = :courseId" +
            " AND subject_id = :subjectId" +
            " AND practice_id = :practiceId" +
            " AND task_id = :taskId")
    Boolean isTaskPassed(
            @Param("username") String username,
            @Param("year") int year,
            @Param("courseId") int courseId,
            @Param("subjectId") int subjectId,
            @Param("practiceId") int practiceId,
            @Param("taskId") int taskId);

    @Query(nativeQuery = true, value = "SELECT code FROM task_result WHERE" +
            " user_id = :username" +
            " AND \"year\" = :year" +
            " AND course_id = :courseId" +
            " AND subject_id = :subjectId" +
            " AND practice_id = :practiceId" +
            " AND task_id = :taskId")
    String getCode(@Param("username") String username,
                   @Param("year") int year,
                   @Param("courseId") int courseId,
                   @Param("subjectId") int subjectId,
                   @Param("practiceId") int practiceId,
                   @Param("taskId") int taskId);
}
