package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "sort_filter_data")
@Data
public class SortFilterDataEntity {

    @Id
    private Long sortId;

    private String filterData;
}
