package com.bog.demo.repository.shortlinks;

import com.bog.demo.domain.shortlinks.ShortLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortLinkRepository extends CrudRepository<ShortLink, Integer> {
    ShortLink findByKey(String key);
}