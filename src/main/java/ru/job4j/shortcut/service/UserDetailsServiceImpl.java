package ru.job4j.shortcut.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.repository.SiteRepository;

import java.util.Collections;

/**
 * Class is a user detail service.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SiteRepository siteRepository;

    public UserDetailsServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    /**
     * Метод загружает детали сохраненного пользователя в SecurityContextHolder.
     *
     * @param username логин
     * @return данные пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Site site = siteRepository.findByUsername(username);
        if (site == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(site.getUsername(), site.getPassword(), Collections.emptyList());
    }
}
