package com.k1ts.practice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticePermissionRepository extends CrudRepository<PracticePermission, PracticePermissionCompositeId> {

    @Query(nativeQuery = true, value = "SELECT allowed FROM practice_permission WHERE" +
            " user_id = :username" +
            " AND \"year\" = :year" +
            " AND course_id = :courseId" +
            " AND subject_id = :subjectId")
    boolean isPracticesAllowed(
            @Param("username") String username,
            @Param("year") int year,
            @Param("courseId") int courseId,
            @Param("subjectId") int subjectId);
}
