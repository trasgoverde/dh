package com.blocknitive.com.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.blocknitive.com.domain.Feedback;
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
 * Spring Data Elasticsearch repository for the {@link Feedback} entity.
 */
public interface FeedbackSearchRepository extends ElasticsearchRepository<Feedback, Long>, FeedbackSearchRepositoryInternal {}

interface FeedbackSearchRepositoryInternal {
    Page<Feedback> search(String query, Pageable pageable);
}

class FeedbackSearchRepositoryInternalImpl implements FeedbackSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    FeedbackSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Feedback> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Feedback> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Feedback.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
