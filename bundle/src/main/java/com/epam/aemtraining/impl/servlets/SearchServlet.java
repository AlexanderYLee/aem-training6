package com.epam.aemtraining.impl.servlets;

import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.epam.aemtraining.ISearch;
import com.epam.aemtraining.SearchServiceConfig;
import com.epam.aemtraining.impl.ServiceFinder;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@SlingServlet(resourceTypes = {"sling/servlet/default"}, extensions = {"searchResults"})
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
            String resourcePath = request.getRequestPathInfo().getResourcePath();
            Resource resource = request.getResourceResolver().resolve(resourcePath);
            Page page = resource.adaptTo(Page.class);
            Locale locale = page.getLanguage(true);
            ResourceBundle resourceBundle = request.getResourceBundle(locale);
            I18n i18n = new I18n(resourceBundle);
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
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter writer = response.getWriter();
                String dictionaryString = i18n.get("resultFormCaption");
                writer.write(String.format("<p>%s<br>", dictionaryString));
                dictionaryString = i18n.get("method");
                writer.write("<ul>");
                writer.write(String.format("<li>%s %s</li>", dictionaryString, method));
                dictionaryString = i18n.get("path");
                writer.write(String.format("<li>%s %s</li>", dictionaryString, path));
                dictionaryString = i18n.get("keywords");
                writer.write(String.format("<li>%s %s</li>", dictionaryString, keywords));
                writer.write("</ul>");
                writer.write("</p>");
                dictionaryString = i18n.get("searchResult", "", String.valueOf(found.length));
                writer.write(String.format("<p>%s</p><br>", dictionaryString));
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
