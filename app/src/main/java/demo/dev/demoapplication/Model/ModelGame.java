package demo.dev.demoapplication.Model;

import android.annotation.SuppressLint;

import com.google.gson.JsonObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Saina on 1/30/17.
 */

@SuppressLint("ParcelCreator")
public class ModelGame extends RealmObject {
    @PrimaryKey
    private String appId;
    private String name;
    private boolean isSelected;

    public ModelGame() {
    }

    public ModelGame(JsonObject asJsonObject) {
        setAppId(asJsonObject.get("appid").getAsString());
        setName(asJsonObject.get("name").getAsString());
        isSelected = false;
    }

    public String getAppId() {
        return appId;
    }

    private void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "\nappid: " + appId + "," + "name: " + name;
    }
}
