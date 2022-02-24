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
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.SiteOnlyNameDTO;
import ru.job4j.shortcut.repository.SiteRepository;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class SiteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteRepository siteRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void simpleCheckExistingMockMvcAndRestTemplate() {
        Assert.assertNotNull(mockMvc);
    }

    @Test
    public void whenRegNewSiteWhenReturnOk() throws Exception {
        Site site = Site.of("test");
        SiteOnlyNameDTO siteOnlyNameDTO = SiteOnlyNameDTO.of("test");
        String loginGen = site.getUsername();
        when(siteRepository.save(any(Site.class))).thenReturn(site);
        mockMvc.perform(post("/site/registration")
                        .content(mapper.writeValueAsString(siteOnlyNameDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registered", is(false)))
                .andExpect(jsonPath("$.login", is(loginGen)));
    }

    @Test
    public void whenNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(
                        get("/site/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenGenerateAuthTokenAndReturnAllSite() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        assertNotNull(token);
        mockMvc.perform(
                get("/site/all").header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk());
    }

    @Test
    public void whenCreateNewSiteAndAuthorizationAndFindById() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site site = Site.of("test");
        siteRepository.save(site);
        Mockito.when(siteRepository.findById(site.getId())).thenReturn(Optional.of(site));
        mockMvc.perform(
                        get("/site/0").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameOfSite", is("test")));
    }

    @Test
    public void whenCreateNewSiteAndAuthorizationAndUpdateUsername() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site site = Site.of("test");
        siteRepository.save(site);
        Mockito.when(siteRepository.findById(site.getId())).thenReturn(Optional.of(site));
        site.setUsername("edit");
        mockMvc.perform(
                patch("/site/")
                        .header("Authorization", "Bearer " + token)
                        .content(mapper.writeValueAsString(site))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(
                        get("/site/0").header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("edit")));
    }

    @Test
    public void whenCreateNewSiteAndAuthorizationAndDeleteThenOk() throws Exception {
        String token = JWTAuthenticationFilter.createToken("test");
        Site site = Site.of("test");
        siteRepository.save(site);
        Mockito.when(siteRepository.findById(site.getId())).thenReturn(Optional.of(site));
        mockMvc.perform(
                        delete("/site/0").header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }
}
