package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.javawebinar.topjava.AllActiveProfileResolver;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.ErrorType;

import javax.annotation.PostConstruct;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles(resolver = AllActiveProfileResolver.class)
abstract public class AbstractControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    protected MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private JpaUtil jpaUtil;

    @Autowired
    protected UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected MessageUtil messageUtil;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        cacheManager.getCache("users").clear();
        if (jpaUtil != null) {
            jpaUtil.clear2ndLevelHibernateCache();
        }
    }

    protected String getMessage(String code) {
        return messageUtil.getMessage(code, Locale.ENGLISH);
    }

    public ResultMatcher errorType(ErrorType type) {
        return jsonPath("$.type").value(type.name());
    }

    public ResultMatcher jsonMessage(String path, String code) {
        return jsonPath(path).value(getMessage(code));
    }
}
