package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Vtopic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vtopic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VtopicRepository extends JpaRepository<Vtopic, Long>, JpaSpecificationExecutor<Vtopic> {}
