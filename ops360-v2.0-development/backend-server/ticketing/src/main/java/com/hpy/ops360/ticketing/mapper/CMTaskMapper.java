package com.hpy.ops360.ticketing.mapper;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.hpy.ops360.ticketing.entity.CMTask;
import com.hpy.ops360.ticketing.ticket.dto.CMTaskDto;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CMTaskMapper {

	public CMTaskDto toDto(CMTask entity) {
		if (entity == null) {
			return null;
		}
		try {
			CMTaskDto dto = CMTaskDto.class.getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.error("Error Creating Dto", e);
			return null;
		}
	}

}
