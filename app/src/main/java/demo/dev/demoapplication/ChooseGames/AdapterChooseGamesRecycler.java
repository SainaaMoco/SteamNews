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
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.dev.demoapplication.Model.ModelGame;
import demo.dev.demoapplication.R;
import demo.dev.demoapplication.Utility.Constants;
import demo.dev.demoapplication.Utility.Utils;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Saina on 1/27/17.
 */

class AdapterChooseGamesRecycler extends RealmRecyclerViewAdapter<ModelGame,
        RecyclerView.ViewHolder> {
    private Realm realm;
    private ItemChangeListener itemChangeListener;
    private int VIEW_TYPE_ITEM = 0;

    AdapterChooseGamesRecycler(@NonNull Context context,
                               @Nullable OrderedRealmCollection<ModelGame> data,
                               boolean autoUpdate, ItemChangeListener itemChangeListener) {
        super(context, data, autoUpdate);
        realm = Realm.getDefaultInstance();
        this.itemChangeListener = itemChangeListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new GameChooserItemViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_game_chooser_recyclerview, parent, false));
        } else {
            //// TODO: 2/7/17 Сонгогдсон item-ууд нь тусгаарлагддаг болгох.
            return new GameChooserLayoutViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_layout_game_chooser_recyclerview, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            if (getData() != null) {
                ((GameChooserItemViewHolder) viewHolder).checkBox.setText(getData().get(position).getName());
                /**
                 * Эхлээд заавал checkedChangeListener-т нь null утга ѳгч байгаад set хийснийхээ
                 * дараа ахиад listener-т нь утга ѳгѳх хэрэгтэй.
                 * */
                ((GameChooserItemViewHolder) viewHolder).checkBox.setOnCheckedChangeListener(null);
                ((GameChooserItemViewHolder) viewHolder).checkBox.setChecked(getData().get(position).isSelected());
                ((GameChooserItemViewHolder) viewHolder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        realm.beginTransaction();
                        int selectedCount = realm.where(ModelGame.class).equalTo("isSelected", true).findAll().size();
                        if (itemChangeListener != null) {
                            if (selectedCount < Constants.GAME_LIMIT || !b) {
                                ModelGame modelGame = getData().get(adapterPosition);
                                modelGame.setSelected(b);
                                itemChangeListener.onItemChanged(((GameChooserItemViewHolder) viewHolder).checkBox, modelGame);
                            } else {
                                ((GameChooserItemViewHolder) viewHolder).checkBox.setChecked(false);
                                Utils.showToast(context, context.getResources().getString(R.string.checked_games_limited));
                            }
                        }
                        realm.commitTransaction();
                    }
                });
            }
        }
    }

    class GameChooserItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_checkbox)
        AppCompatCheckBox checkBox;

        GameChooserItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class GameChooserLayoutViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_selected_items)
        LinearLayout selectedGames;

        GameChooserLayoutViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
