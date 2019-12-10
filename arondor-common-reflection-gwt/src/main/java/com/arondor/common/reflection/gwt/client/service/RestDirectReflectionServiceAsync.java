package com.arondor.common.reflection.gwt.client.service;

import java.util.Collection;
import java.util.logging.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.xstream.catalog.GWTAccessibleClassCatalogParser;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Directly calls a REST URL, parses and fetches the classes information
 * 
 * @author Francois Barre
 *
 */
public class RestDirectReflectionServiceAsync implements GWTReflectionServiceAsync
{
    private static final Logger LOG = Logger.getLogger(RestDirectReflectionServiceAsync.class.getName());

    private final AccessibleClassCatalog catalog;

    public RestDirectReflectionServiceAsync(AccessibleClassCatalog catalog)
    {
        this.catalog = catalog;
    }

    @Override
    public void getAccessibleClass(final String className, final AsyncCallback<AccessibleClass> callback)
    {
        Scheduler.get().scheduleDeferred(new ScheduledCommand()
        {
            @Override
            public void execute()
            {
                callback.onSuccess(catalog.getAccessibleClass(className));
            }
        });
    }

    @Override
    public void getImplementingAccessibleClasses(final String className,
            final AsyncCallback<Collection<AccessibleClass>> callback)
    {
        Scheduler.get().scheduleDeferred(new ScheduledCommand()
        {
            @Override
            public void execute()
            {
                callback.onSuccess(catalog.getImplementingAccessibleClasses(className));
            }
        });
    }

    public static void fetchReflection(final String url, final AsyncCallback<RestDirectReflectionServiceAsync> callback)
    {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setCallback(new RequestCallback()
        {
            @Override
            public void onResponseReceived(Request request, Response response)
            {
                String xml = response.getText();
                LOG.info("Recieved " + xml.length() + " bytes for " + url);

                GWTAccessibleClassCatalogParser parser = new GWTAccessibleClassCatalogParser();
                LOG.info("Parsing ...");
                final AccessibleClassCatalog catalog = parser.parse(xml);
                LOG.info("Parsing ... DONE.");

                RestDirectReflectionServiceAsync serviceAsync = new RestDirectReflectionServiceAsync(catalog);
                callback.onSuccess(serviceAsync);
            }

            @Override
            public void onError(Request request, Throwable exception)
            {
                callback.onFailure(
                        new IllegalArgumentException("Could not get reflection information from " + url, exception));
            }
        });

        try
        {
            requestBuilder.send();
        }
        catch (RequestException e)
        {
            throw new IllegalArgumentException(
                    "Could not query reflection information from " + url + ", exception : " + e.getMessage(), e);
        }
    }

}
