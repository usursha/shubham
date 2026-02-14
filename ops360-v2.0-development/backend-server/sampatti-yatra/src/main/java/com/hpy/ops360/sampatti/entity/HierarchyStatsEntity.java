package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
public class HierarchyStatsEntity {

    @Id
    private Long srno; 

    private String level;
    private String superiorUserId;
    private Integer teamCount;
    private Integer totalCount;
}
