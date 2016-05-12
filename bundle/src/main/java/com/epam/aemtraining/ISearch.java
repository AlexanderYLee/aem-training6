package com.epam.aemtraining;

import org.apache.sling.api.resource.ResourceResolver;

/**
 *
 * Created by Aliaksandr_Li on 5/5/2016.
 */
public interface ISearch {
    String[] doSearch(String type, String path, String keywords, ResourceResolver resourceResolver);
}
