package demo.dev.demoapplication.ChooseGames;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.dev.demoapplication.Model.ModelGame;
import demo.dev.demoapplication.R;
import demo.dev.demoapplication.Utility.Constants;
import demo.dev.demoapplication.Utility.Utils;
import io.realm.Case;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Saina on 1/27/17.
 */

@SuppressLint("LongLogTag")
class AdapterChooseGamesRecycler extends RealmRecyclerViewAdapter<ModelGame,
        RecyclerView.ViewHolder> {
    private final String TAG = "AdapterChooseGamesRecycler";
    private OrderedRealmCollection<ModelGame> data;
    private Realm realm;
    private ItemChangeListener itemChangeListener;
    private int VIEW_TYPE_ITEM = 0;
    private Filter filter;

    AdapterChooseGamesRecycler(@NonNull Context context,
                               @Nullable OrderedRealmCollection<ModelGame> data,
                               boolean autoUpdate, ItemChangeListener itemChangeListener) {
        super(context, data, autoUpdate);
        realm = Realm.getDefaultInstance();
        this.data = data;
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
            if (data != null) {
                ((GameChooserItemViewHolder) viewHolder).checkBox.setText(data.get(position).getName());
                /**
                 * Эхлээд заавал checkedChangeListener-т нь null утга ѳгч байгаад set хийснийхээ
                 * дараа ахиад listener-т нь утга ѳгѳх хэрэгтэй.
                 * */
                ((GameChooserItemViewHolder) viewHolder).checkBox.setOnCheckedChangeListener(null);
                ((GameChooserItemViewHolder) viewHolder).checkBox.setChecked(data.get(position).isSelected());
                ((GameChooserItemViewHolder) viewHolder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        realm.beginTransaction();
                        int selectedCount = realm.where(ModelGame.class).equalTo("isSelected", true).findAll().size();
                        if (itemChangeListener != null) {
                            if (selectedCount < Constants.GAME_LIMIT || !b) {
                                ModelGame modelGame = data.get(adapterPosition);
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

    public Filter getFilter() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filter = new GameFilter(AdapterChooseGamesRecycler.this);
            }
        });

        return filter;
    }

    private void filterResults(String text) {
        text = text == null ? null : text.toLowerCase().trim();
        if (text == null || "".equals(text)) {
            data = realm.where(ModelGame.class).findAll();
        } else {
            data = realm.where(ModelGame.class)
                    .contains("name", text, Case.INSENSITIVE)
                    .findAll();
        }
        updateData(data);
    }

    private class GameFilter extends Filter {
        private final AdapterChooseGamesRecycler adapter;

        private GameFilter(AdapterChooseGamesRecycler adapter) {
            super();
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filterResults(constraint.toString());
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
