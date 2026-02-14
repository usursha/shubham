package com.hpy.ops360.dashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.UserMast;

@Repository
public interface UserRepository extends JpaRepository<UserMast, String> {
	Optional<UserMast> findByUsername(String username);

}
