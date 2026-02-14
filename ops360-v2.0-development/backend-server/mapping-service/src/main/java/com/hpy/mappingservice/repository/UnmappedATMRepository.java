package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.UnMappedATMEntity;

@Repository
public interface UnmappedATMRepository extends JpaRepository<UnMappedATMEntity, Integer> {

    @Query(value = "EXEC GetATMsByCEUsername :ceUsername", nativeQuery = true)
    List<UnMappedATMEntity> getATMsByCEUsername(@Param("ceUsername") String ceUsername);
    
    @Query(value = "EXEC GetATMsBySecondaryCEUsername :ceUsername", nativeQuery = true)
    List<UnMappedATMEntity> getATMsBySecondaryCEUsername(@Param("ceUsername") String ceUsername);
    
    @Query(value = "EXEC GetATMsdistByCEUsername :ceUsername, :newMappedCE, :atmCodeList", nativeQuery = true)
    List<UnMappedATMEntity> getATMsWithDistance(
        @Param("ceUsername") String ceUsername,
        @Param("newMappedCE") String newMappedCE,
        @Param("atmCodeList") String atmCodeList
    );
}