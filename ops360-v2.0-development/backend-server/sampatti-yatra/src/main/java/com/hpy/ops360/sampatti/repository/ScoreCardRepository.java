package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.sampatti.entity.ScoreCardResponseEntity;

@Repository
public interface ScoreCardRepository extends JpaRepository<ScoreCardResponseEntity, Long> {

    @Query(value = "EXEC dbo.uspQry_ViewMyScoreCardDetails @p_user_id = :userId", nativeQuery = true)
    List<ScoreCardResponseEntity> getScoreCardData(@Param("userId") String userId);
    

}
