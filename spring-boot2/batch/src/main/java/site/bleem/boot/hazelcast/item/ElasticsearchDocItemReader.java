package site.bleem.boot.hazelcast.item;

import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Iterator;

/**
 * Create by pengchao on 2018/2/24
 */
public class ElasticsearchDocItemReader<Doc> extends AbstractPaginatedDataItemReader<Doc> implements InitializingBean {
 
    private final ElasticsearchOperations elasticsearchOperations;
 
    private final SearchQuery query;
 
    private final Class<? extends Doc> targetType;
 
    public ElasticsearchDocItemReader(ElasticsearchOperations elasticsearchOperations, SearchQuery query, Class<? extends Doc> targetType) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.query = query;
        this.targetType = targetType;
    }
 
    @Override
    protected Iterator<Doc> doPageRead() {
        return (Iterator<Doc>)elasticsearchOperations.queryForList(query, targetType).iterator();
    }
 
    @Override
    public void afterPropertiesSet() throws Exception {
    }
}