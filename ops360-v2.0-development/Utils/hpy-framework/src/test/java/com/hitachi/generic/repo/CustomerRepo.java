package com.hitachi.generic.repo;

import org.springframework.stereotype.Repository;

import com.hitachi.generic.utils.CustomerEntity;
import com.hpy.generic.IGenericRepo;

@Repository
public interface CustomerRepo extends IGenericRepo<CustomerEntity>{

}
