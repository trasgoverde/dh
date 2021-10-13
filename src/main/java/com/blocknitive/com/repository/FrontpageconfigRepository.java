package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Frontpageconfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Frontpageconfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrontpageconfigRepository extends JpaRepository<Frontpageconfig, Long>, JpaSpecificationExecutor<Frontpageconfig> {}
