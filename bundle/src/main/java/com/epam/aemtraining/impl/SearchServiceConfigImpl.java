package com.epam.aemtraining.impl;

import com.epam.aemtraining.SearchServiceConfig;
import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;

import java.util.Dictionary;

/**
 * One implementation of the {@link SearchServiceConfig}. Note that
 * the repository is injected, not retrieved.
 */
@Service(SearchServiceConfig.class)
@Component(immediate = true, metatype = true)
public class SearchServiceConfigImpl implements SearchServiceConfig {
    private final static String SEARCH_KEYWORDS="com.epam.aemtraining.search.keyword";
    private final static String SEARCH_METHOD="com.epam.aemtraining.search.method";
    private final static String SEARCH_PATH="com.epam.aemtraining.search.path";

    private final static String SEARCH_KEYWORDS_DEFAULT="QUERY BUILDER";
    private final static String SEARCH_PATH_DEFAULT="/content/geometrixx";
    private final static String SEARCH_METHOD_DEFAULT="QueryManager";

    @Property(name=SEARCH_KEYWORDS, label="Search keywords", value = SEARCH_KEYWORDS_DEFAULT)
    private static String keywords;

    @Property(name=SEARCH_METHOD, label="Search method", value = SEARCH_METHOD_DEFAULT)
    private static String method;

    @Property(name=SEARCH_PATH, label="Search path", value = SEARCH_PATH_DEFAULT)
    private static String path;

    public String getKeywords() {
        return keywords;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
    @Activate
    @Modified
    protected void activate(ComponentContext ctx){
        Dictionary<String, Object> properties = ctx.getProperties();
        keywords = (String)properties.get(SEARCH_KEYWORDS);
        method = (String)properties.get(SEARCH_METHOD);
        path = (String)properties.get(SEARCH_PATH);
    }
}
