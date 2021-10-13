package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Vanswer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vanswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VanswerRepository extends JpaRepository<Vanswer, Long>, JpaSpecificationExecutor<Vanswer> {}
