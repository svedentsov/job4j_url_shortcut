package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.shortcut.model.Site;

public interface SiteRepository extends CrudRepository<Site, Integer> {

    @Query("SELECT s FROM Site s WHERE s.username = :username")
    Site findByUsername(@Param("username") String username);

    @Query("SELECT s FROM Site s WHERE s.nameOfSite = :name")
    Site findByNameOfSite(@Param("name") String name);
}
