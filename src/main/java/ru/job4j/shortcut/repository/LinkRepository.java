package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortcut.model.Link;

import java.util.Collection;

@Transactional
public interface LinkRepository extends CrudRepository<Link, Integer> {

    @Query("SELECT l FROM Link l WHERE l.shortcut = :shortLink")
    Link findByShortcut(@Param("shortLink") String shortUrl);

    @Modifying
    @Query("UPDATE Link SET total = total + 1")
    void incrementTotal();

    @Query("SELECT l FROM Link l JOIN FETCH l.site s WHERE l.site.id = :siteId")
    Collection<Link> findBySiteId(@Param("siteId") int siteId);
}
