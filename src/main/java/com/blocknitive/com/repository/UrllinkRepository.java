package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Urllink;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Urllink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UrllinkRepository extends JpaRepository<Urllink, Long>, JpaSpecificationExecutor<Urllink> {}
