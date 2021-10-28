package com.bog.demo.service.shortlink;

import com.bog.demo.domain.shortlinks.ShortLink;

public interface ShortLinkService {
    ShortLink getShortLink(String key);
}