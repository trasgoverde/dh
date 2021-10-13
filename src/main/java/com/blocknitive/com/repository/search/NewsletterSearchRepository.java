package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.Newsletter;
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
 * Spring Data Elasticsearch repository for the {@link Newsletter} entity.
 */
public interface NewsletterSearchRepository extends ElasticsearchRepository<Newsletter, Long>, NewsletterSearchRepositoryInternal {}

interface NewsletterSearchRepositoryInternal {
    Page<Newsletter> search(String query, Pageable pageable);
}

class NewsletterSearchRepositoryInternalImpl implements NewsletterSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    NewsletterSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Newsletter> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Newsletter> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Newsletter.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
