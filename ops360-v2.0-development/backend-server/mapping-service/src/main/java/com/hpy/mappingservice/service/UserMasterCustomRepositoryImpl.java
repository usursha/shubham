package com.hpy.mappingservice.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.UserMaster;
import com.hpy.mappingservice.repository.UserMasterCustomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserMasterCustomRepositoryImpl implements UserMasterCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int[] bulkInsertOrUpdate(List<UserMaster> users) {
    	log.info("Started method User bulkInsertOrUpdate");
        String sql = """
            MERGE INTO user_master AS target
            USING (SELECT ? AS username, ? AS full_name, ? AS designation, ? AS mobileno,
                          ? AS employee_code, ? AS city, ? AS state, ? AS zone, ? AS email_id, ? AS home_address) AS source
            ON target.username = source.username
            WHEN MATCHED THEN
                UPDATE SET full_name = source.full_name,
                           designation = source.designation,
                           mobileno = source.mobileno,
                           employee_code = source.employee_code,
                           city = source.city,
                           state = source.state,
                           zone = source.zone,
                           email_id = source.email_id,
                           home_address = source.home_address
            WHEN NOT MATCHED THEN
                INSERT (username, full_name, designation, mobileno, employee_code, city, state, zone, email_id, home_address)
                VALUES (source.username, source.full_name, source.designation, source.mobileno,
                        source.employee_code, source.city, source.state, source.zone, source.email_id, source.home_address);
        """;

        List<Object[]> batchArgs = users.stream()
            .map(u -> new Object[]{
                u.getUsername(), u.getFullName(), u.getDesignation(), u.getMobileno(),
                u.getEmployeeCode(), u.getCity(), u.getState(), u.getZone(),
                u.getEmailId(), u.getHomeAddress()
            })
            .toList();

        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}

