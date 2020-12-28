package tech.balaji.urlshortener.services;


import org.springframework.data.domain.Page;
import tech.balaji.urlshortener.dtos.GenerateUrlRequest;
import tech.balaji.urlshortener.models.UrlModel;

public interface UrlService {

    UrlModel generateShortUrl(GenerateUrlRequest generateUrlRequest);

    Page<UrlModel> getPaginatedUrls(int pageNo, int pageSize);

    String getOriginalUrlByKeyAndUpdate(String key);
}
