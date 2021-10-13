package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.Vtopic;
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
 * Spring Data Elasticsearch repository for the {@link Vtopic} entity.
 */
public interface VtopicSearchRepository extends ElasticsearchRepository<Vtopic, Long>, VtopicSearchRepositoryInternal {}

interface VtopicSearchRepositoryInternal {
    Page<Vtopic> search(String query, Pageable pageable);
}

class VtopicSearchRepositoryInternalImpl implements VtopicSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    VtopicSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Vtopic> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Vtopic> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Vtopic.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
