package tech.balaji.urlshortener.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.balaji.urlshortener.dtos.GenerateUrlRequest;
import tech.balaji.urlshortener.models.UrlModel;
import tech.balaji.urlshortener.response.ApiSuccessResponse;
import tech.balaji.urlshortener.services.UrlService;
import tech.balaji.urlshortener.utils.MessageConstants;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Tag(name = "URL")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UrlController {

    private final UrlService urlService;

    @PostMapping(value = "/a/url")
    public ResponseEntity<ApiSuccessResponse<UrlModel>> generateShortUrl(@RequestBody GenerateUrlRequest generateUrlRequest) {
        return status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(urlService.generateShortUrl(generateUrlRequest), MessageConstants.URL_GENERATED));
    }

    @GetMapping("/a/url")
    public ResponseEntity<ApiSuccessResponse<Page<UrlModel>>> getUrls(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return ok(new ApiSuccessResponse<>(urlService.getPaginatedUrls(page, size), MessageConstants.SUCCESS));
    }

    @GetMapping("/u/{key}")
    public void handleResourceRedirect(@PathVariable("key") String key, HttpServletResponse httpServletResponse) {
        String originalUrl = urlService.getOriginalUrlByKeyAndUpdate(key);
        httpServletResponse.setHeader(HttpHeaders.LOCATION, originalUrl);
        httpServletResponse.setStatus(302);
    }
}
