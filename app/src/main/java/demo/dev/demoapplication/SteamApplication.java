package demo.dev.demoapplication;

import android.app.Application;

import demo.dev.demoapplication.Utility.ServiceGenerator;
import io.realm.Realm;

/**
 * Created by Saina on 1/30/17.
 */

public class SteamApplication extends Application {
    private static ApiService apiService;

    public static ApiService getApiService() {
        return apiService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        apiService = ServiceGenerator.createService(ApiService.class);
    }
}
