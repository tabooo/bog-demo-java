package com.bog.demo.controller;

import com.bog.demo.domain.shortlinks.ShortLink;
import com.bog.demo.service.shortlink.ShortLinkService;
import com.bog.demo.util.Descriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController("shortLinkManagementController")
@RequestMapping("rest/api/short")
public class ShortLinkController {
    private ShortLinkService shortLinkService;

    @GetMapping("/{key}")
    public ResponseEntity<Descriptor> confirmShortLink(@PathVariable final String key) {
        ShortLink shortLink = shortLinkService.getShortLink(key);
        if (shortLink == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL_NOT_FOUND");
        }

        return new ResponseEntity<>(Descriptor.validDescriptor(shortLink.getUrl()), HttpStatus.OK);
    }

    @Autowired
    public void setShortLinkService(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }
}