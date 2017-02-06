package demo.dev.demoapplication.ChooseGames;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import Model.ModelGame;
import butterknife.BindView;
import butterknife.ButterKnife;
import demo.dev.demoapplication.R;
import demo.dev.demoapplication.Utility.Constants;
import demo.dev.demoapplication.Utility.Utils;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Saina on 1/27/17.
 */

class AdapterChooseGamesRecycler extends RealmRecyclerViewAdapter<ModelGame, AdapterChooseGamesRecycler.GameChooserViewHolder> {
    private Realm realm;
    private ItemChangeListener itemChangeListener;

    AdapterChooseGamesRecycler(@NonNull Context context,
                               @Nullable OrderedRealmCollection<ModelGame> data,
                               boolean autoUpdate, ItemChangeListener itemChangeListener) {
        super(context, data, autoUpdate);
        realm = Realm.getDefaultInstance();
        this.itemChangeListener = itemChangeListener;
    }

    @Override
    public AdapterChooseGamesRecycler.GameChooserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameChooserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game_chooser_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(final AdapterChooseGamesRecycler.GameChooserViewHolder viewHolder, int position) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        if (getData() != null) {
            viewHolder.checkBox.setText(getData().get(position).getName());
            /**
             * Эхлээд заавал checkedChangeListener-т нь null утга ѳгч байгаад set хийснийхээ дараа ахиад listener-т нь
             * утга ѳгѳх хэрэгтэй.
             * */
            viewHolder.checkBox.setOnCheckedChangeListener(null);
            viewHolder.checkBox.setChecked(getData().get(position).isSelected());
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    realm.beginTransaction();
                    int selectedCount = realm.where(ModelGame.class).equalTo("isSelected", true).findAll().size();
                    if (itemChangeListener != null) {
                        if (selectedCount < Constants.GAME_LIMIT || !b) {
                            ModelGame modelGame = getData().get(adapterPosition);
                            modelGame.setSelected(b);
                            itemChangeListener.onItemChanged(viewHolder.checkBox, modelGame);
                        } else {
                            viewHolder.checkBox.setChecked(false);
                            Utils.showToast(context, context.getResources().getString(R.string.checked_games_limited));
                        }
                    }
                    realm.commitTransaction();
                }
            });
        }
    }

    class GameChooserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_checkbox)
        AppCompatCheckBox checkBox;

        GameChooserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
