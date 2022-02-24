package ru.job4j.shortcut.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.LinkDTO;
import ru.job4j.shortcut.model.StatLinkListDTO;
import ru.job4j.shortcut.model.UrlShortcutDTO;
import ru.job4j.shortcut.service.MainService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {

    private final MainService mainService;

    public LinkController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/all")
    public List<Link> findAll() {
        return mainService.findAllLinks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Link> findById(@PathVariable int id) {
        return mainService.findLinkById(id);
    }

    @PostMapping("/shortcut")
    public ResponseEntity<UrlShortcutDTO> create(@Valid @RequestBody LinkDTO linkDTO) {
        return mainService.saveLink(linkDTO);
    }

    @PatchMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Link link) throws InvocationTargetException, IllegalAccessException {
        return mainService.updateLink(link);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return mainService.deleteLink(id);
    }

    /**
     * Метод совершает переадресацию на URL сайта по уникальному ключу.
     *
     * @param shortUrl уникальный ключ
     * @return переадресация на url сайта
     */
    @GetMapping("/redirect/{shortcut}")
    public ResponseEntity<Void> redirect(@PathVariable("shortcut") String shortUrl) {
        return mainService.redirectLink(shortUrl);
    }

    /**
     * Метод формирует статистические данные по количеству вызовов всех адресов.
     *
     * @param siteId
     * @return статистические данные
     */
    @GetMapping("/statistic/{siteId}")
    public ResponseEntity<StatLinkListDTO> stat(@PathVariable("siteId") int siteId) {
        return mainService.statLinkForSite(siteId);
    }
}
