package demo.dev.demoapplication;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Saina on 1/30/17.
 */

interface ApiService {
    @GET("ISteamApps/GetAppList/v2/")
    Observable<JsonObject> getGameList();
}
