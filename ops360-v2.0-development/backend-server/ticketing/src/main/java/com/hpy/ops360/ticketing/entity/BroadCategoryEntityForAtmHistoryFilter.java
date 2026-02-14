package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BroadCategoryEntityForAtmHistoryFilter {

	@Id
    @Column(name = "broad_category_id")
    private Long srno;

    @Column(name = "broad_category")
    private String broadCategory;

}
