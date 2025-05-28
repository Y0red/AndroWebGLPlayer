package com.matrixtec.androwebglplayer;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ModalFragment.NoticeDialogListener {

    private RecyclerView gamesRecyclerView;
    private GameAdapter gameAdapter;
    private List<Game> gameList = new ArrayList<>();
    private GameFetcher gameFetcher;

    Dialog dialog;
    Button canceleBtn, okBtn;

    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                ModalFragment modal = new ModalFragment(MainActivity.this);
                CreateDialog(modal);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        gamesRecyclerView = findViewById(R.id.gamesRecyclerView);
        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameAdapter = new GameAdapter(this, gameList);
        gamesRecyclerView.setAdapter(gameAdapter);

        gameFetcher = new GameFetcher();
        fetchGameList();

       //new StartGameDialogFragment().show(this.getSupportFragmentManager(), "GAME_DIALOG");
       // showNoticeDialog();

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        canceleBtn = dialog.findViewById(R.id.cancelButton);
        okBtn = dialog.findViewById(R.id.okButton);

        canceleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "yes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
        frameLayout = findViewById(R.id.dialog_Frame);


    }
    private  void CreateDialog(ModalFragment newmodal)
    {
        //dialog.show();

       FragmentActivity frag = new FragmentActivity( getSupportFragmentManager().beginTransaction().replace(R.id.dialog_Frame, newmodal)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit());
        frameLayout.setVisibility(View.VISIBLE);


        Button cb = frag.findViewById(R.id.cancelButton);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "no", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private OnBackPressedDispatcherOwner requireActivity() {
        return this;
    }

    private void fetchGameList() {
        gameFetcher.fetchGames(new GameFetcher.Callback() {
            @Override
            public void onGamesFetched(List<Game> fetchedGames) {
                gameList.clear();
                gameList.addAll(fetchedGames);
                gameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Error fetching games: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it.
        DialogFragment dialog = new NoticeDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }
    @Override
    public void onDialogPositiveClick() {
       // Toast.makeText(MainActivity.this, "yes", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick() {
       // Toast.makeText(MainActivity.this, "no", Toast.LENGTH_SHORT).show();
    }
}

