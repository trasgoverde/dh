package com.blocknitive.com.repository;

import com.blocknitive.com.domain.ProposalVote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProposalVote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProposalVoteRepository extends JpaRepository<ProposalVote, Long>, JpaSpecificationExecutor<ProposalVote> {}
