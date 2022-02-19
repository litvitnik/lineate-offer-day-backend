package com.example.offerdaysongs.repository;

import com.example.offerdaysongs.model.Copyright;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface CopyrightRepository extends JpaRepository<Copyright, Long>, JpaSpecificationExecutor<Copyright> {
    List<Copyright> findAllByCompanyId(long id);
    Optional<Copyright> findCopyrightByBeginsAndExpires(ZonedDateTime begins, ZonedDateTime expires);
}
