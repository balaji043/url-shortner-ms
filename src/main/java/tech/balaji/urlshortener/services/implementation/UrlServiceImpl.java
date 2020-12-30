package tech.balaji.urlshortener.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.balaji.urlshortener.dtos.GenerateUrlRequest;
import tech.balaji.urlshortener.models.UrlModel;
import tech.balaji.urlshortener.models.User;
import tech.balaji.urlshortener.properties.ApplicationProperties;
import tech.balaji.urlshortener.repository.UrlRepository;
import tech.balaji.urlshortener.services.UrlService;
import tech.balaji.urlshortener.services.UserService;

import java.util.Optional;

import static tech.balaji.urlshortener.utils.BaseConversion.encode;
import static tech.balaji.urlshortener.utils.MessageConstants.throwAccessDeniedException;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final UserService userService;
    private final ApplicationProperties applicationProperties;

    @Override
    public UrlModel generateShortUrl(GenerateUrlRequest generateUrlRequest) {
        // Get Current Request's User
        User user = userService.getUser().orElseThrow(throwAccessDeniedException);

        // Create URL Entity
        UrlModel urlModel = new UrlModel();
        urlModel.setLongUrl(generateUrlRequest.getUrl());
        urlModel.setCreatedBy(user.getId());
        urlModel.setTitle(generateUrlRequest.getTitle());

        // Save the entity to get Id
        urlRepository.save(urlModel);

        // Create Short URL
        String shortUrl = encode(urlModel.getId());
        urlModel.setShortUrl(shortUrl);
        // Set the shortUrl key to db
        urlRepository.save(urlModel);

        // Create the URL response
        return urlModel;
    }

    @Override
    public Page<UrlModel> getPaginatedUrls(int pageNo, int pageSize) {
        User user = userService.getUser().orElseThrow(throwAccessDeniedException);
        Pageable pageSortedWithUpdateOne = PageRequest.of(pageNo, pageSize, Sort.by("updatedOn"));
        return urlRepository.findAllByCreatedBy(user.getId(), pageSortedWithUpdateOne);
    }

    @Override
    public String getOriginalUrlByKeyAndUpdate(String key) {
        Optional<UrlModel> urlModelOptional = urlRepository.findByShortUrl(key);
        if (!urlModelOptional.isPresent())
            return applicationProperties.getNotFoundUrl();
        UrlModel urlModel = urlModelOptional.get();
        int noOfClicks = urlModel.getNoOfClicks() + 1;
        urlModel.setNoOfClicks(noOfClicks);
        urlRepository.save(urlModel);
        return urlModel.getLongUrl();
    }

}
