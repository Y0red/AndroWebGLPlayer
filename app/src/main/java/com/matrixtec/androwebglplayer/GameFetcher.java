package com.matrixtec.androwebglplayer;

import java.util.ArrayList;
import java.util.List;

public class GameFetcher {

    // Simulate fetching games from a server
    public interface Callback {
        void onGamesFetched(List<Game> games);
        void onError(String errorMessage);
    }

    public void fetchGames(Callback callback) {
        // Simulate a network delay
        new android.os.Handler().postDelayed(() -> {
            List<Game> games = new ArrayList<>();
            games.add(new Game(
                    "https://games.fun.et/MonsterTruck/icons/icon-256.png",
                    "Monster Truck",
                    "Embark on an epic quest!",
                    "https://games.fun.et/MonsterTruck/index.html",
                    "landscape"
            ));
            games.add(new Game(
                    "https://games.fun.et/RopeMerge/icons/icon-512.png",
                    "Rope Merge",
                    "Challenge your brain with tricky puzzles.",
                    "https://games.fun.et/RopeMerge/index.html",
                    "portrait"
            ));
            games.add(new Game(
                    "https://games.fun.et/merge2048/logo_512x512.png",
                    "Merge 2048",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/merge2048/index.html",
                    "portrait"
            ));
            games.add(new Game(
                    "https://games.fun.et/2048/banner/banner.png",
                    "2048",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/2048/index.html",
                    "portrait"
            ));
            games.add(new Game(
                    "https://games.fun.et/taximash/banner/banner.png",
                    "Taxi Mash",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/taximash/index.html",
                    "portrait"
            ));
            games.add(new Game(
                    "https://games.fun.et/addistile/banner/banner.png",
                    "addisTile",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/addistile/",
                    "portrait"
            ));
            games.add(new Game(
                    "https://games.fun.et/fruitsnake/icons/icon-256.png",
                    "Snake",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/fruitsnake/index.html",
                    "landscape"
            ));
            games.add(new Game(
                    "https://games.fun.et/ninjarun/icons/icon-256.png",
                    "Ninja Run",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/ninjarun/index.html",
                    "landscape"
            ));
            games.add(new Game(
                    "https://games.fun.et/watermelonfruit2048/icons/icon-512.png",
                    "watermelonfruit2048",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/watermelonfruit2048/index.html",
                    "portrait"
            ));
            games.add(new Game(
                    "https://games.fun.et/stickmonkey/icons/icon-256.png",
                    "stickmonkey",
                    "Blast through waves of alien invaders.",
                    "https://games.fun.et/stickmonkey/index.html",
                    "portrait"
            ));
            callback.onGamesFetched(games);
            // In a real scenario, handle potential errors here
            // callback.onError("Failed to fetch data");
        }, 2000); // Simulate a 2-second delay
    }
}