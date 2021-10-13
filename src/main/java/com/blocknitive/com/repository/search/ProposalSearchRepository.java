package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.Proposal;
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
 * Spring Data Elasticsearch repository for the {@link Proposal} entity.
 */
public interface ProposalSearchRepository extends ElasticsearchRepository<Proposal, Long>, ProposalSearchRepositoryInternal {}

interface ProposalSearchRepositoryInternal {
    Page<Proposal> search(String query, Pageable pageable);
}

class ProposalSearchRepositoryInternalImpl implements ProposalSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ProposalSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Proposal> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Proposal> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Proposal.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
