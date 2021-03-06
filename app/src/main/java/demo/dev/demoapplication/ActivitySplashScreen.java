package demo.dev.demoapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import demo.dev.demoapplication.ChooseGames.ActivityChooseGames;
import demo.dev.demoapplication.Main.MainActivity;
import demo.dev.demoapplication.Model.ModelGame;
import demo.dev.demoapplication.databinding.ActivitySplashScreenBinding;
import rx.Observable;
import rx.Subscriber;

public class ActivitySplashScreen extends ActivityBase {
    private ActivitySplashScreenBinding binding;
    private Observable<JsonObject> requestObservable;
    private Subscriber<JsonObject> requestSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        binding.progress.getIndeterminateDrawable()
                .setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);

        getGameList();

        if (realm.isEmpty()) {
            sendRequest(requestObservable, requestSubscriber);
        } else {
            startNextActivityWithAnimation();
        }
    }

    /**
     * Тоглоомуудын жагсаалт авах хүсэлт явуулаад баазад хадгалах.
     * Ямар ч хариугүй таг болчихоод байхаар нь 25000 гэж хязгаар тавьсан.
     */

    //// TODO: 2/10/17 25000 гэсэн хязгаарлалтаа авах.
    private void getGameList() {
        requestObservable = SteamApplication.getApiService().getGameList();
        requestSubscriber = new Subscriber<JsonObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JsonObject jsonObject) {
                JsonArray jsonArray = jsonObject
                        .get("applist").getAsJsonObject()
                        .get("apps").getAsJsonArray();

                realm.beginTransaction();

                for (int i = 0; i < 25000; i++) {
                    ModelGame modelGame = new ModelGame(jsonArray.get(i).getAsJsonObject());
                    realm.copyToRealmOrUpdate(modelGame);
                }

                realm.commitTransaction();
                startNextActivityWithAnimation();
            }
        };
    }

    private void startNextActivityWithAnimation() {
        Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animBounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                int selectedCount = realm.where(ModelGame.class).equalTo("isSelected", true).findAll().size();
                if (selectedCount > 0) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), ActivityChooseGames.class));
                }
                overridePendingTransition(R.anim.transition_slide_in, R.anim.transition_slide_out);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.icLauncher.startAnimation(animBounce);
    }
}
