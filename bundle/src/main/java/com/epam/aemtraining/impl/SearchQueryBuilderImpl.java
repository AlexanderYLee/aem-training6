package com.epam.aemtraining.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.epam.aemtraining.ISearch;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Aliaksandr_Li on 5/5/2016.
 */
@Service(ISearch.class)
@Component(immediate = true)
public class SearchQueryBuilderImpl implements ISearch {

    private Logger logger = LoggerFactory.getLogger(SearchQueryBuilderImpl.class);

    public String[] doSearch(String type, String path, String keywords, ResourceResolver resourceResolver) {
        Session session = resourceResolver.adaptTo(Session.class);

        // create query description as hash map (simplest way, same as form post)
        Map<String, String> map = new HashMap<String, String>();

        //set QueryBuilder search criteria
        map.put("type", type);
        map.put("path", path);
        map.put("fulltext", keywords);
        map.put("property", "jcr:content/metadata/dc:format");
        map.put("property.value", "%pdf%");
        map.put("property.operation", "like");
        map.put("p.limit", "-1");

        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);

        //INvoke the Search query
        Query query = builder.createQuery(PredicateGroup.create(map), session);

        SearchResult sr= query.getResult();


        // iterating over the results
        List<String> result = new ArrayList<String>();
        for (Hit hit : sr.getHits()) {

            //Convert the HIT to an asset - each asset will be placed into a ZIP for downloading
            try {
                result.add(hit.getPath());
            } catch (RepositoryException e) {
                logger.debug(e.getMessage());
            }
        }
        return result.toArray(new String[0]);
    }
}