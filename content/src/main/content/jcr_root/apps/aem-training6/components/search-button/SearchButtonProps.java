package apps.aem_training6.components.search_button;

import com.adobe.cq.sightly.WCMUse;

public class SearchButtonProps extends WCMUse {
    private String method;
    private String path;
    private String keywords;

    @Override
    public void activate() throws Exception {
        method = getProperties().get("method", "QueryBuilder");      
        path = getProperties().get("path", "/content/dam");
        keywords = getProperties().get("keywords", "QUERY BUILDER");
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getKeywords() {
        return keywords;
    }
}