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
    private final static String SEARCH_TYPE="com.epam.aemtraining.search.type";

    private final static String SEARCH_TYPE_DEFAULT="dam:Asset";

    @Property(name=SEARCH_TYPE, label="Search node type", value = SEARCH_TYPE_DEFAULT)
    private static String type;

    public String getType() {
        return type;
    }
    @Activate
    @Modified
    protected void activate(ComponentContext ctx){
        Dictionary<String, Object> properties = ctx.getProperties();
        type = (String)properties.get(SEARCH_TYPE);
    }
}
