package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.ProposalVote;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProposalVote} entity.
 */
public interface ProposalVoteSearchRepository extends ElasticsearchRepository<ProposalVote, Long>, ProposalVoteSearchRepositoryInternal {}

interface ProposalVoteSearchRepositoryInternal {
    Page<ProposalVote> search(String query, Pageable pageable);
}

class ProposalVoteSearchRepositoryInternalImpl implements ProposalVoteSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ProposalVoteSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<ProposalVote> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<ProposalVote> hits = elasticsearchTemplate
            .search(nativeSearchQuery, ProposalVote.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
