package ru.job4j.shortcut.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.shortcut.model.*;
import ru.job4j.shortcut.repository.LinkRepository;
import ru.job4j.shortcut.repository.SiteRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MainService {

    private final SiteRepository siteRepository;
    private final LinkRepository linkRepository;
    private BCryptPasswordEncoder encoder;

    public MainService(SiteRepository siteRepository, LinkRepository linkRepository, BCryptPasswordEncoder encoder) {
        this.siteRepository = siteRepository;
        this.linkRepository = linkRepository;
        this.encoder = encoder;
    }

    public List<Site> findAllSites() {
        return StreamSupport.stream(
                this.siteRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public List<Link> findAllLinks() {
        return StreamSupport.stream(
                this.linkRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public ResponseEntity<Site> findSiteById(int id) {
        var site = this.siteRepository.findById(id);
        if (site.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(site.get());
    }

    public ResponseEntity<Link> findLinkById(int id) {
        var link = this.linkRepository.findById(id);
        if (link.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(link.get());
    }

    public ResponseEntity<SiteDTO> saveSite(SiteOnlyNameDTO siteOnlyNameDTO) {
        Site check = siteRepository.findByNameOfSite(siteOnlyNameDTO.getNameOfSite());
        if (check != null) {
            return new ResponseEntity<>(
                    new SiteDTO(true, check.getUsername(), "hidden password"),
                    HttpStatus.OK
            );
        } else {
            Site buff = Site.of(siteOnlyNameDTO.getNameOfSite());
            String passBefore = buff.getPassword();
            buff.setPassword(encoder.encode(passBefore));
            Site siteAfterSave = this.siteRepository.save(buff);
            return new ResponseEntity<>(
                    new SiteDTO(false, siteAfterSave.getUsername(), passBefore),
                    HttpStatus.CREATED
            );
        }
    }

    public ResponseEntity<UrlShortcutDTO> saveLink(LinkDTO linkDTO) {
        Optional<Site> buffSite = siteRepository.findById(linkDTO.getSiteId());
        String rootUrlFromLinkDTO;
        String linkForSave;
        if (linkDTO.getUrl().startsWith("http://")) {
            linkForSave = linkDTO.getUrl().split("//")[1];
            rootUrlFromLinkDTO = linkForSave.split("/")[0];
        } else {
            linkForSave = linkDTO.getUrl();
            rootUrlFromLinkDTO = linkDTO.getUrl().split("/")[0];
        }
        if (buffSite.isPresent() && buffSite.get().getNameOfSite().equals(rootUrlFromLinkDTO)) {
            Link buff = this.linkRepository.save(Link.of(linkForSave, buffSite.get()));
            UrlShortcutDTO urlShortcutDTO = new UrlShortcutDTO("http://localhost:8080/link/redirect/" + buff.getShortcut());
            return new ResponseEntity<>(
                    urlShortcutDTO,
                    HttpStatus.CREATED);
        } else {
            throw new NullPointerException("Site not registered, please register");
        }
    }

    public ResponseEntity<Void> updateSite(Site site) throws InvocationTargetException, IllegalAccessException {
        var current = siteRepository.findById(site.getId());
        if (current.isEmpty()) {
            throw new NullPointerException("Site with this id not found");
        }
        var buffSite = current.get();
        var methods = buffSite.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new NullPointerException("Invalid properties");
                }
                var newValue = getMethod.invoke(site);
                if (newValue != null) {
                    setMethod.invoke(buffSite, newValue);
                }
            }
        }
        buffSite.setPassword(encoder.encode(buffSite.getPassword()));
        siteRepository.save(buffSite);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> updateLink(Link link) throws InvocationTargetException, IllegalAccessException {
        var current = linkRepository.findById(link.getId());
        if (current.isEmpty()) {
            throw new NullPointerException("Link not found");
        }
        var buffLink = current.get();
        var methods = buffLink.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new NullPointerException("Invalid properties");
                }
                var newValue = getMethod.invoke(link);
                if (newValue != null) {
                    setMethod.invoke(buffLink, newValue);
                }
            }
        }
        linkRepository.save(buffLink);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteSite(int id) {
        if (this.siteRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found");
        }
        Site site = new Site();
        site.setId(id);
        this.siteRepository.delete(site);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteLink(int id) {
        if (this.linkRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Link not found");
        }
        Link link = new Link();
        link.setId(id);
        this.linkRepository.delete(link);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> redirectLink(String shortUrl) {
        var link = this.linkRepository.findByShortcut(shortUrl);
        if (link == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shortcut not found");
        }
        this.linkRepository.incrementTotal();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", "http://" + link.getUrl());
        return new ResponseEntity<>(
                httpHeaders, HttpStatus.MOVED_PERMANENTLY
        );
    }

    public ResponseEntity<StatLinkListDTO> statLinkForSite(int siteId) {
        List<StatLinkDTO> buffListDTO = new ArrayList<>();
        Collection<Link> buffLinkList = StreamSupport.stream(
                this.linkRepository.findBySiteId(siteId).spliterator(), false
        ).collect(Collectors.toList());
        for (Link elem : buffLinkList) {
            buffListDTO.add(new StatLinkDTO(elem.getUrl(), elem.getTotal()));
        }
        StatLinkListDTO statLinkListDTO = new StatLinkListDTO(buffListDTO);
        return new ResponseEntity<>(
                statLinkListDTO,
                HttpStatus.OK);
    }
}
