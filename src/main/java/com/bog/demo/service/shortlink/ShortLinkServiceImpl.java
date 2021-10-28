package com.bog.demo.service.shortlink;

import com.bog.demo.domain.shortlinks.ShortLink;
import com.bog.demo.repository.shortlinks.ShortLinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class, readOnly = true)
public class ShortLinkServiceImpl implements ShortLinkService {

    private ShortLinkRepository shortLinkRepository;

    @Override
    public ShortLink getShortLink(String key) {
        ShortLink shortLink = shortLinkRepository.findByKey(key);
        if (shortLink == null) {
            return null;
        }


        return shortLink;
    }

    @Autowired
    public void setShortLinkRepository(ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }
}
