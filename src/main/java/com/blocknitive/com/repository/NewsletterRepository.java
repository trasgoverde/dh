package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Newsletter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Newsletter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long>, JpaSpecificationExecutor<Newsletter> {}
