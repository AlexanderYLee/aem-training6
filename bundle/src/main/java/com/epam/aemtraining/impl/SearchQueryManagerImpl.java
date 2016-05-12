package com.epam.aemtraining.impl;

import com.epam.aemtraining.ISearch;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Aliaksandr_Li on 5/5/2016.
 */
@Service(ISearch.class)
@Component(immediate = true)
public class SearchQueryManagerImpl implements ISearch {
    private Logger logger = LoggerFactory.getLogger(SearchQueryBuilderImpl.class);

    public String[] doSearch(String type, String path, String keywords, ResourceResolver resourceResolver) {
        Session session = resourceResolver.adaptTo(Session.class);
        try {
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            //it is possible to set dc:format as a parameter
            String sQuery = "select * from [%s] AS s WHERE ISDESCENDANTNODE([%s]) and CONTAINS(s.*, '%s') and CONTAINS(s.[jcr:content/metadata/dc:format], 'pdf')";
            Query query = queryManager.createQuery(String.format(sQuery, type, path, keywords),
                Query.JCR_SQL2);
            QueryResult qr = query.execute();
            NodeIterator nodes = qr.getNodes();
            List<String> result = new ArrayList<String>();
            while (nodes.hasNext()){
                Node node = nodes.nextNode();
                try {
                    result.add(node.getPath());
                } catch (RepositoryException e) {
                    logger.debug(e.getMessage());
                }
            }
            return result.toArray(new String[0]);
        } catch (RepositoryException e) {
            logger.debug(e.getMessage());
        }
        return new String[0];
    }
}
