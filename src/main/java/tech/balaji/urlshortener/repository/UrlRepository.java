package tech.balaji.urlshortener.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.balaji.urlshortener.models.UrlModel;

import java.util.Optional;

@Repository
public interface UrlRepository extends PagingAndSortingRepository<UrlModel, Long> {

    Page<UrlModel> findAllByCreatedBy(Long createdBy, Pageable pageable);

    Optional<UrlModel> findByShortUrl(String key);

}
