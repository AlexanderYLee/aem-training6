package apps.aem_training6.components.search_button;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import java.util.Locale;

public class SearchButtonProps extends WCMUsePojo {
    private String method;
    private String path;
    private String keywords;
    private Locale locale;

    @Override
    public void activate() throws Exception {
        method = getProperties().get("method", "QueryBuilder");
        path = getProperties().get("path", "/content/dam");
        keywords = getProperties().get("keywords", "QUERY BUILDER");
        Page currentPage = get("currentPage", Page.class);
        locale = currentPage.getLanguage(false);
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
    public String getLocale() {
        return locale.toString();
    }
}