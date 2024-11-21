package shop.s5g.shop.repository.booktag.qdsl;

import shop.s5g.shop.entity.booktag.BookTag;

import java.util.List;

public interface BookTagQuerydslRepository {


    List<BookTag> BookTagCount(Long tagId);
}
