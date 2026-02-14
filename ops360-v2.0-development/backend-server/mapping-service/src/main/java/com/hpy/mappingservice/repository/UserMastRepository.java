package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.UserMast;

@Repository
public interface UserMastRepository extends JpaRepository<UserMast, Integer> {

}
