package com.hpy.uam.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.uam.entity.UserDetailsEntity;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetailsEntity, Long>{

	
	@Query(value="select count(id) from user_entity where realm_id=(SELECT id \r\n"
			+ "        FROM realm \r\n"
			+ "        WHERE name = :realmName)", nativeQuery=true)
	int getTotalUserCount(@Param("realmName") String realmName);
	
	
	@Query(value = "SELECT count(u.id)\r\n"
			+ "FROM user_entity u\r\n"
			+ "JOIN user_group_membership m ON u.id = m.user_id\r\n"
			+ "WHERE m.group_id = (\r\n"
			+ "    SELECT id \r\n"
			+ "    FROM keycloak_group \r\n"
			+ "    WHERE name = 'CM_USER' \r\n"
			+ "    AND realm_id = (\r\n"
			+ "        SELECT id \r\n"
			+ "        FROM realm \r\n"
			+ "        WHERE name = :realmName \r\n"
			+ "    ))", nativeQuery = true)
    int getCMUsersCountsInGroup(@Param("realmName") String realmName);
	
	@Query(value = "SELECT count(u.id)\r\n"
			+ "FROM user_entity u\r\n"
			+ "JOIN user_group_membership m ON u.id = m.user_id\r\n"
			+ "WHERE m.group_id = (\r\n"
			+ "    SELECT id \r\n"
			+ "    FROM keycloak_group \r\n"
			+ "    WHERE name = 'CE_USER' \r\n"
			+ "    AND realm_id = (\r\n"
			+ "        SELECT id \r\n"
			+ "        FROM realm \r\n"
			+ "        WHERE name = :realmName \r\n"
			+ "    ))", nativeQuery = true)
    int getCEUsersCountsInGroup(@Param("realmName") String realmName);
	
	@Query(value = "SELECT u.username\r\n"
			+ "FROM user_entity u\r\n"
			+ "JOIN user_group_membership m ON u.id = m.user_id\r\n"
			+ "WHERE m.group_id = (\r\n"
			+ "    SELECT id \r\n"
			+ "    FROM keycloak_group \r\n"
			+ "    WHERE name = 'CE_USER' \r\n"
			+ "    AND realm_id = (\r\n"
			+ "        SELECT id \r\n"
			+ "        FROM realm \r\n"
			+ "        WHERE name = :realmName \r\n"
			+ "    ))", nativeQuery = true)
    List<String> getCEUsersInGroup(@Param("realmName") String realmName);
	
	@Query(value = "SELECT u.username\r\n"
			+ "FROM user_entity u\r\n"
			+ "JOIN user_group_membership m ON u.id = m.user_id\r\n"
			+ "WHERE m.group_id = (\r\n"
			+ "    SELECT id \r\n"
			+ "    FROM keycloak_group \r\n"
			+ "    WHERE name = 'CM_USER' \r\n"
			+ "    AND realm_id = (\r\n"
			+ "        SELECT id \r\n"
			+ "        FROM realm \r\n"
			+ "        WHERE name = :realmName \r\n"
			+ "    ))", nativeQuery = true)
    List<String> getCMUsersInGroup(@Param("realmName") String realmName);
}
