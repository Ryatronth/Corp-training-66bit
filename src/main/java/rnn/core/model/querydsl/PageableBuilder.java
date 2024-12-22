package rnn.core.model.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageableBuilder {
    public static <T> Page<T> build(JPAQuery<T> query, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        long total = query.fetchCount();
        List<T> entities = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(entities, pageable, total);
    }
}
