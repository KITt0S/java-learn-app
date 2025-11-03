package com.k1ts.taskresult;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class TaskResult {
    @EmbeddedId
    private TaskResultCompositeId id;

    @Column(length = 5000)
    private String code;

    private int attemptCount;

    private boolean passed;

    private int mark;

    private LocalDate passedDate;
}
