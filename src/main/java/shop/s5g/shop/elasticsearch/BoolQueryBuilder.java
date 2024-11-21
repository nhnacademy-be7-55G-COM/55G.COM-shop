package shop.s5g.shop.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.springframework.stereotype.Component;

@Component
public class BoolQueryBuilder {

    public static Query createMatchQuery(String field, String query, float boost) {
        return new MatchQuery.Builder()
            .field(field)
            .query(query)
            .boost(boost)
            .build()
            ._toQuery();
    }

    public static Query createTermQuery(String field, String value, float boost) {
        return new TermQuery.Builder()
            .field(field)
            .value(value)
            .boost(boost)
            .build()
            ._toQuery();
    }
}
