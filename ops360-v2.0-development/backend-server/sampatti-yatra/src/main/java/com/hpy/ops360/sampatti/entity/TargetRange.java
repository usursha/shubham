package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "target_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer srno;

    @Column(name = "min_target", nullable = false)
    private Integer minAchieved;

    @Column(name = "max_target", nullable = false)
    private Integer maxAchieved;
}
