package com.k1ts.taskresult;

import com.k1ts.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class TaskResultCompositeId {
    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    @Column(name = "\"year\"")
    private int year;

    private int courseId;

    private int subjectId;

    private int practiceId;

    private int taskId;
}
