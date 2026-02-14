package com.hpy.ops360.sampatti.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atm_uptime_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtmUptimeData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long srno;

    private LocalDate date;

    private String atmid;

    private String monthtotilldateuptime;

    private String lastmonthuptime;
}
