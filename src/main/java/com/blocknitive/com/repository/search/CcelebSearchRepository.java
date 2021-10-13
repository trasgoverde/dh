package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.Cceleb;
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
 * Spring Data Elasticsearch repository for the {@link Cceleb} entity.
 */
public interface CcelebSearchRepository extends ElasticsearchRepository<Cceleb, Long>, CcelebSearchRepositoryInternal {}

interface CcelebSearchRepositoryInternal {
    Page<Cceleb> search(String query, Pageable pageable);
}

class CcelebSearchRepositoryInternalImpl implements CcelebSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CcelebSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Cceleb> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Cceleb> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Cceleb.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
