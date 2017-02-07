package demo.dev.demoapplication.ChooseGames;

import android.support.v7.widget.AppCompatCheckBox;

import demo.dev.demoapplication.Model.ModelGame;

/**
 * Created by Saina on 2/6/17.
 */

interface ItemChangeListener {
    void onItemChanged(AppCompatCheckBox checkBox, ModelGame modelGame);
}
