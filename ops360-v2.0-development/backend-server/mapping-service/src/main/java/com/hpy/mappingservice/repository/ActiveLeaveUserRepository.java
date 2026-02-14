package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.ActiveLeaveUserEntity;

@Repository
public interface ActiveLeaveUserRepository extends CrudRepository<ActiveLeaveUserEntity, Long> {

    @Query(value = "EXEC GetActiveLeaveUsersByManager :managerUsername", nativeQuery = true)
    List<ActiveLeaveUserEntity> getActiveLeaveUsersByManager(@Param("managerUsername") String managerUsername);
}
