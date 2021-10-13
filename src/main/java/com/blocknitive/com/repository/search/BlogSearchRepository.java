package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.Blog;
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
 * Spring Data Elasticsearch repository for the {@link Blog} entity.
 */
public interface BlogSearchRepository extends ElasticsearchRepository<Blog, Long>, BlogSearchRepositoryInternal {}

interface BlogSearchRepositoryInternal {
    Page<Blog> search(String query, Pageable pageable);
}

class BlogSearchRepositoryInternalImpl implements BlogSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    BlogSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Blog> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Blog> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Blog.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
