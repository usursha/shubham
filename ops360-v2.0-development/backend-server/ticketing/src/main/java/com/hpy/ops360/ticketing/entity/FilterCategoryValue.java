package com.hpy.ops360.ticketing.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "filter_category_value")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterCategoryValue extends GenericEntity {

	private String filterValueCode;
	private String filterValue;
	private String seqOrder;

	@ManyToOne
	@JoinColumn(name = "filer_category_id", referencedColumnName = "id")
	private FilterCategory filterCategory;

}
