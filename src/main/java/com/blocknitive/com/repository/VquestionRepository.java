package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Vquestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vquestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VquestionRepository extends JpaRepository<Vquestion, Long>, JpaSpecificationExecutor<Vquestion> {}
