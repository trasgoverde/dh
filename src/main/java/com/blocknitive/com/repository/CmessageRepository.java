package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Cmessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cmessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CmessageRepository extends JpaRepository<Cmessage, Long>, JpaSpecificationExecutor<Cmessage> {}
