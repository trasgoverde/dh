package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Vthumb;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vthumb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VthumbRepository extends JpaRepository<Vthumb, Long>, JpaSpecificationExecutor<Vthumb> {}
