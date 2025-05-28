package com.matrixtec.androwebglplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private Context context;
    private List<Game> gameList;

    public GameAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game currentGame = gameList.get(position);
        holder.gameNameTextView.setText(currentGame.getName());
        holder.gameDescriptionTextView.setText(currentGame.getDescription());

        // Load image using Picasso library (add dependency in build.gradle - app level)
        Picasso.get().load(currentGame.getImageUrl()).placeholder(android.R.drawable.ic_menu_gallery).into(holder.gameImageView);

        holder.playButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, GameDetailActivity.class);
            intent.putExtra("gameLink", currentGame.getLink());
            intent.putExtra("gameName", currentGame.getName());
            intent.putExtra("gameOrientation", currentGame.getOrientation());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public ImageView gameImageView;
        public TextView gameNameTextView;
        public TextView gameDescriptionTextView;
        public Button playButton;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameImageView = itemView.findViewById(R.id.gameImageView);
            gameNameTextView = itemView.findViewById(R.id.gameNameTextView);
            gameDescriptionTextView = itemView.findViewById(R.id.gameDescriptionTextView);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }
}
