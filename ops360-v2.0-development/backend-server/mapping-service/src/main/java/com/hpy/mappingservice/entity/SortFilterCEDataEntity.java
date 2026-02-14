package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "sort_filter_data_CE")
@Data
public class SortFilterCEDataEntity {

    @Id
    @Column(name="sort_id")
    private Long sortId;

    @Column(name="filter_data")
    private String filterData;
}
