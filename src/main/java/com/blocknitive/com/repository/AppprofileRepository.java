package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Appprofile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Appprofile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppprofileRepository extends JpaRepository<Appprofile, Long>, JpaSpecificationExecutor<Appprofile> {}
