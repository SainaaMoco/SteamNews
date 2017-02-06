package demo.dev.demoapplication;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        realm = Realm.getDefaultInstance();
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
}
