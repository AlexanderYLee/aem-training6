package com.epam.aemtraining.impl;

import com.epam.aemtraining.ISearch;
import org.apache.felix.scr.annotations.*;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

/**
 *
 * Created by Aliaksandr_Li on 5/5/2016.
 */
@Service(ServiceFinder.class)
@Component(immediate = true)
public class ServiceFinder {
    private Logger logger = LoggerFactory.getLogger(ServiceFinder.class);

    private ComponentContext ctx;

    public ISearch getService(String method) {

        try {
            ServiceReference[] refs = ctx.getBundleContext().getServiceReferences(ISearch.class.getName(),
                    String.format("(component.name=*%s*)", method));
            if (refs.length==1){
                return (ISearch) ctx.getBundleContext().getService(refs[0]);
            }
        } catch (InvalidSyntaxException e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    @Activate
    protected void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

}
