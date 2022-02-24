package ru.job4j.shortcut;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.shortcut.filter.JWTAuthenticationFilter;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.LinkDTO;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.repository.LinkRepository;
import ru.job4j.shortcut.repository.SiteRepository;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class LinkControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteRepository siteRepository;

    @MockBean
    private LinkRepository linkRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void simpleCheckExistingMockMvcAndRestTemplate() {
        Assert.assertNotNull(mockMvc);
    }

    @Test
    public void whenCreateNewLinkAndFindByIdThenStatusOk() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site site = Site.of("test");
        Link link = Link.of("test/1111", site);
        linkRepository.save(link);
        Mockito.when(linkRepository.findById(link.getId())).thenReturn(Optional.of(link));
        mockMvc.perform(
                        get("/link/0")
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url", is("test/1111")));
    }

    @Test
    public void whenCreateNewLinkThenStatusCreatedThenStatusOk() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site site = Site.of("test");
        Link link = Link.of("test/1111", site);
        LinkDTO linkDTO = new LinkDTO(0, "test/1111");
        Mockito.when(linkRepository.save(any(Link.class))).thenReturn(link);
        Mockito.when(siteRepository.findById(link.getId())).thenReturn(Optional.of(site));
        mockMvc.perform(
                post("/link/shortcut")
                        .header("Authorization", "Bearer " + token)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(linkDTO))
        ).andExpect(status().isCreated());
    }

    @Test
    public void whenCreateNewLinkAndFindAll() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site site = Site.of("test");
        Link link = Link.of("test/1111", site);
        Mockito.when(linkRepository.findAll()).thenReturn(Collections.singletonList(link));
        mockMvc.perform(
                        get("/link/all")
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Collections.singletonList(link))));
    }

    @Test
    public void whenCreateNewLinkAndUpdate() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site site = Site.of("test");
        Link link = Link.of("test/1111", site);
        Mockito.when(linkRepository.save(any(Link.class))).thenReturn(link);
        Mockito.when(linkRepository.findById(link.getId())).thenReturn(Optional.of(link));
        link.setShortcut("edit");
        mockMvc.perform(
                patch("/link/")
                        .header("Authorization", "Bearer " + token)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(link))
        ).andExpect(status().isOk());
    }

    @Test
    public void whenRedirectThenStatusFound() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site forTestSite = Site.of("test");
        Link forTestLink = Link.of("test/111", forTestSite);
        forTestLink.setShortcut("edit");
        String shortcut = forTestLink.getShortcut();
        Mockito.when(linkRepository.findByShortcut(shortcut)).thenReturn(forTestLink);
        mockMvc.perform(
                        get("/link/redirect/{shortcut}", shortcut)
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isMovedPermanently());
    }
}