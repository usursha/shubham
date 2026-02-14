package com.hpy.ops360.framework.service;

import com.hpy.generic.IGenericDto;
import com.hpy.generic.IGenericEntity;
import com.hpy.generic.impl.GenericService;
import com.hpy.ops360.framework.dto.PointDto;
import com.hpy.ops360.framework.dto.UserLocationDto;

public abstract class Ops360GenericService<T extends IGenericDto, S extends IGenericEntity>
		extends GenericService<T, S> {

	protected void savePoint(T dto) {
		if (dto instanceof UserLocationDto) {
			UserLocationDto userLocationDto = ((UserLocationDto) dto);
			PointDto point = userLocationDto.getPoint();
			String userName = userLocationDto.getUsername();

		}
	}

}
