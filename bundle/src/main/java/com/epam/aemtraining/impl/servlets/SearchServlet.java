package com.epam.aemtraining.impl.servlets;

import com.epam.aemtraining.ISearch;
import com.epam.aemtraining.SearchServiceConfig;
import com.epam.aemtraining.impl.ServiceFinder;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@SlingServlet(paths = {"/services/search"})
public class SearchServlet extends SlingSafeMethodsServlet {
    
    private Logger logger = LoggerFactory.getLogger(SearchServlet.class);

    @Reference
    private ServiceFinder serviceFinder;

    @Reference
    private SearchServiceConfig searchServiceConfig;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        logger.debug("Method is " + searchServiceConfig.getMethod());
        logger.debug("Path is " + searchServiceConfig.getPath());
        logger.debug("Keywords are " + searchServiceConfig.getKeywords());
        ISearch searchService = serviceFinder.getService(searchServiceConfig.getMethod());
        if (searchService!=null){
            logger.debug("SearchService is " + searchService.getClass().getName());
            searchService.doSearch(searchServiceConfig.getPath(), searchServiceConfig.getKeywords());
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.write("<p>SearchServlet is running!</p>");
        writer.flush();
        writer.close();
    }
}
