package com.hpy.ops360.ticketing.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filter_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterCategory extends GenericEntity {

	@Column(name = "screen")
	private String screen;

	@Column(name = "filter_code")
	private String filterCode;

	@Column(name = "filter_label")
	private String filterLabel;

	@Column(name = "seq_order")
	private String seqOrder;

//	@OneToMany(mappedBy = "filterCategory", cascade = CascadeType.ALL)
//	private List<FilterCategoryValue> filterCategoryValues;
}
