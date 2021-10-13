package com.blocknitive.com.repository;

import com.blocknitive.com.domain.Photo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {}
