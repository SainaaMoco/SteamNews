package demo.dev.demoapplication;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import demo.dev.demoapplication.Utility.Utils;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Saina on 2/6/17.
 */
public class ActivityBase extends AppCompatActivity {
    public Realm realm;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("steamNewsSharedPreference", MODE_PRIVATE);
    }

    protected void sendRequest(Observable<JsonObject> observable, Subscriber<JsonObject> subscriber) {
        if (Utils.isNetworkConnected(this)) {
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(subscriber);
        } else {
            Utils.showAlertDialog(this, getResources().getString(R.string.check_network_connection));
        }
    }

    protected void saveToShared(String value, String key) {
        sharedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    protected void saveToShared(boolean value, String key) {
        sharedPreferences.edit()
                .putBoolean(key, value)
                .apply();
    }

    protected void saveToShared(int value, String key) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply();
    }

    protected String getStringFromShared(String key) {
        return sharedPreferences.getString(key, "");
    }

    protected boolean getBooleanFromShared(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    protected int getIntFromShared(String key) {
        return sharedPreferences.getInt(key, 0);
    }
}
