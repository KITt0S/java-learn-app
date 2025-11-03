package com.k1ts.practice;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class PracticePermission {

    @EmbeddedId
    private PracticePermissionCompositeId id;

    private boolean allowed;
}
