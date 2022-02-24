package ru.job4j.shortcut.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteDTO;
import ru.job4j.shortcut.model.SiteOnlyNameDTO;
import ru.job4j.shortcut.service.MainService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/site")
public class SiteController {

    private final MainService mainService;

    public SiteController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/all")
    public List<Site> findAll() {
        return mainService.findAllSites();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> findById(@PathVariable int id) {
        return mainService.findSiteById(id);
    }

    /**
     * Метод регистрирует новый сайт и отображает результат сохранения аккаунта в систему.
     *
     * @param siteOnlyNameDTO новый сайт
     * @return аккаунт
     */
    @PostMapping("/registration")
    public ResponseEntity<SiteDTO> register(@Valid @RequestBody SiteOnlyNameDTO siteOnlyNameDTO) {
        return mainService.saveSite(siteOnlyNameDTO);
    }

    @PatchMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Site site) throws InvocationTargetException, IllegalAccessException {
        return mainService.updateSite(site);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return mainService.deleteSite(id);
    }
}
