package com.hpy.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericRepo<T extends IGenericEntity> extends JpaRepository<T, Long> {




}
