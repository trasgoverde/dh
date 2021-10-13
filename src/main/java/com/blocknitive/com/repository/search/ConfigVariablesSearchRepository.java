package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.ConfigVariables;
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
 * Spring Data Elasticsearch repository for the {@link ConfigVariables} entity.
 */
public interface ConfigVariablesSearchRepository
    extends ElasticsearchRepository<ConfigVariables, Long>, ConfigVariablesSearchRepositoryInternal {}

interface ConfigVariablesSearchRepositoryInternal {
    Page<ConfigVariables> search(String query, Pageable pageable);
}

class ConfigVariablesSearchRepositoryInternalImpl implements ConfigVariablesSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ConfigVariablesSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<ConfigVariables> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<ConfigVariables> hits = elasticsearchTemplate
            .search(nativeSearchQuery, ConfigVariables.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
