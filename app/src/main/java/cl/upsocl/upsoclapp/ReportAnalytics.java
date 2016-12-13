package cl.upsocl.upsoclapp;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.upsocl.upsoclapp.AnalyticsApplication;

/**
 * Created by emily.pagua on 12-12-2016.
 */

public class ReportAnalytics {

    private AnalyticsApplication application;
    private Context context;


    public ReportAnalytics(AnalyticsApplication application, Context context) {
        this.application =application;
        this.context =  context;
    }

    public void sendReportGoogleAnalytics(String link, String category) {

        Tracker mTracker = application.getDefaultTracker();

        // [START custom_event]
        mTracker.setScreenName(link);
        mTracker.setReferrer(link);
        mTracker.send(new HitBuilders.EventBuilder()
                .setLabel(link)
                .setCategory(link)
                .setAction(category)
                .build());

        GoogleAnalytics.getInstance(context).dispatchLocalHits();
    }

}
