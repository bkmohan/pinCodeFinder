package com.android.apps.pincodefinder;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

/**
 * Loads a list of post offices by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class PostOfficeLoader extends AsyncTaskLoader<List<PostOffice>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = PostOfficeLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    private List<PostOffice> cachedData;

    /**
     * Constructs a new {@link PostOfficeLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public PostOfficeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * On starting the loader check if there is cached data from previous request.
     * If cached data found, skip {forceload()} to perform a network request
     */
    @Override
    protected void onStartLoading() {
        if (cachedData != null) {
            deliverResult(cachedData);
        } else {
            forceLoad();
        }
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<PostOffice> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of post office.
        List<PostOffice> postOffices = QueryUtils.fetchPostOfficeData(mUrl);
        return postOffices;
    }

    /**
     * This is to cache the loading result.
     * When loadInBackground() has finished, it passes its return value to deliverResult().
     *
     * @param data
     */
    @Override
    public void deliverResult(@Nullable List<PostOffice> data) {
        cachedData = data;
        super.deliverResult(data);
    }
}

