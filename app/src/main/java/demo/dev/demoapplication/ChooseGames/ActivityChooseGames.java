package demo.dev.demoapplication.ChooseGames;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import demo.dev.demoapplication.ActivityBase;
import demo.dev.demoapplication.Main.MainActivity;
import demo.dev.demoapplication.Model.ModelGame;
import demo.dev.demoapplication.R;
import demo.dev.demoapplication.Utility.Constants;
import demo.dev.demoapplication.databinding.ActivityChooseGamesBinding;

public class ActivityChooseGames extends ActivityBase implements ItemChangeListener {
    private ActivityChooseGamesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_games);
        setSupportActionBar(binding.toolbar);
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerViewChooseGames.setLayoutManager(linearLayoutManager);
        binding.recyclerViewChooseGames.setHasFixedSize(true);
        binding.recyclerViewChooseGames.addItemDecoration(new DividerItemDecoration(this,
                linearLayoutManager.getOrientation()));
        binding.recyclerViewChooseGames.setAdapter(new AdapterChooseGamesRecycler(this,
                realm.where(ModelGame.class).findAllAsync(), true, this));

        resetToolbarTitle();
    }

    @Override
    public void onItemChanged(AppCompatCheckBox checkBox, ModelGame modelGame) {
        resetToolbarTitle();
    }

    private void resetToolbarTitle() {
        int selectedCount = realm.where(ModelGame.class).equalTo("isSelected", true).findAll().size();
        binding.toolbarTitle.setText(String.valueOf(selectedCount + "/" + Constants.GAME_LIMIT));

        if (selectedCount < 10) {
            binding.buttonOk.setOnClickListener(null);
            binding.buttonOk.setVisibility(View.GONE);
        } else {
            binding.buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.transition_slide_in, R.anim.transition_slide_out);
                    finish();
                }
            });
            binding.buttonOk.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInUp)
                    .duration(1000)
                    .playOn(binding.buttonOk);
        }
    }
}