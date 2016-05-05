package com.epam.aemtraining.impl;

import com.epam.aemtraining.ISearch;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 *
 * Created by Aliaksandr_Li on 5/5/2016.
 */
@Service(ISearch.class)
@Component(immediate = true)
public class SearchQueryManagerImpl implements ISearch {
    public String[] doSearch(String path, String keywords) {
        return new String[0];
    }
}
