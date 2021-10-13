package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Calbum;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Calbum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalbumRepository extends JpaRepository<Calbum, Long>, JpaSpecificationExecutor<Calbum> {}
