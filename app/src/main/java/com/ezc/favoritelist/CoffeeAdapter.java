package com.ezc.favoritelist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.ViewHolder> {

    private ArrayList<CoffeeItem> coffeeItems;
    private Context context;
    private FavDB favDB;

    public CoffeeAdapter(ArrayList<CoffeeItem> coffeeItems, Context context) {
        this.coffeeItems = coffeeItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CoffeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB(context);
        // create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new CoffeeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CoffeeAdapter.ViewHolder holder, int position) {
        final CoffeeItem coffeeItem = coffeeItems.get(position);

        readCursorData(coffeeItem, holder);
        holder.imageView.setImageResource(coffeeItem.getImageResourse());
        holder.titleTextView.setText(coffeeItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return coffeeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;
        Button favBtn;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            favBtn = itemView.findViewById(R.id.favBtn);

            // add to fav btn
            favBtn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                CoffeeItem coffeeItem = coffeeItems.get(position);
                if (coffeeItem.getFavStatus().equals("0")) {
                    coffeeItem.setFavStatus("1");
                    favDB.insertIntoTheDatabase(coffeeItem.getTitle(), coffeeItem.getImageResourse(),
                            coffeeItem.getKey_id(), coffeeItem.getFavStatus());
                    favBtn.setBackgroundResource(R.drawable.ic_favorite_color);
                } else {
                    coffeeItem.setFavStatus("0");
                    favDB.remove_fav(coffeeItem.getKey_id());
                    favBtn.setBackgroundResource(R.drawable.ic_favorite);
                }
            });
        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firsStart", false);
        editor.apply();
    }

    private void readCursorData(CoffeeItem coffeeItem, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data(coffeeItem.getKey_id());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                coffeeItem.setFavStatus(item_fav_status);

                // check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_color);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }
}
