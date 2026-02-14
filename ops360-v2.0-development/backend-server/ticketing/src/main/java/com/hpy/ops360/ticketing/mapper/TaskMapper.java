package com.hpy.ops360.ticketing.mapper;

import org.springframework.stereotype.Component;

import com.hpy.generic.impl.GenericMapper;
import com.hpy.ops360.ticketing.dto.TaskDto;
import com.hpy.ops360.ticketing.entity.Task;

@Component
public class TaskMapper extends GenericMapper<TaskDto, Task>{

	public TaskMapper( ) {
		super(TaskDto.class, Task.class);
		
	}
	
	

}
