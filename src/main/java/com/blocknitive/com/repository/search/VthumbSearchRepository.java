package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.Vthumb;
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
 * Spring Data Elasticsearch repository for the {@link Vthumb} entity.
 */
public interface VthumbSearchRepository extends ElasticsearchRepository<Vthumb, Long>, VthumbSearchRepositoryInternal {}

interface VthumbSearchRepositoryInternal {
    Page<Vthumb> search(String query, Pageable pageable);
}

class VthumbSearchRepositoryInternalImpl implements VthumbSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    VthumbSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Vthumb> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Vthumb> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Vthumb.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
