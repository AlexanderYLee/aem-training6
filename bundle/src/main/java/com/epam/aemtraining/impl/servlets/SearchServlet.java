package com.epam.aemtraining.impl.servlets;

import com.epam.aemtraining.ISearch;
import com.epam.aemtraining.SearchServiceConfig;
import com.epam.aemtraining.impl.ServiceFinder;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@SlingServlet(paths = {"/services/search"})
public class SearchServlet extends SlingSafeMethodsServlet {
    
    private Logger logger = LoggerFactory.getLogger(SearchServlet.class);

    @Reference
    private SearchServiceConfig config;

    @Reference
    private ServiceFinder serviceFinder;

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        String keywords = request.getParameter("keywords");
        String path = request.getParameter("path");
        ISearch searchService = serviceFinder.getService(method);
        if (searchService!=null){
            ResourceResolver resourceResolver = null;
            try {
                Map<String, Object> prop = new HashMap<String, Object>();
                prop.put(ResourceResolverFactory.SUBSERVICE, "ISearch");
                resourceResolver = resolverFactory.getServiceResourceResolver(prop);
                //resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
            } catch (LoginException e) {
                logger.debug(e.getMessage());
            }
            if (resourceResolver!=null){
                String[] found = searchService.doSearch(config.getType(), path, keywords, resourceResolver);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("text/html");
                PrintWriter writer = response.getWriter();
                writer.write(String.format("<p>Search provided by %s</p><br>", method));
                writer.write(String.format("<p>Found %d records</p><br>", found.length));
                if (found.length>0){
                    for(String s: found){
                        writer.write(String.format("<a href=\"%s\">%s</a><br>", s, s));
                    }
                }
                writer.flush();
                writer.close();
            }
        }
        response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
    }
}
