package com.hpy.mappingservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "target_range_CE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetRangeCE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer srno;

    @Column(name = "target_start", nullable = false)
    private Integer targetStart;

    @Column(name = "target_end", nullable = false)
    private Integer targetEnd;
}
