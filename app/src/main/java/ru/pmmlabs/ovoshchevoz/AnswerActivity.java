package ru.pmmlabs.ovoshchevoz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import static ru.pmmlabs.ovoshchevoz.GameActivity.GUESSED;
import static ru.pmmlabs.ovoshchevoz.GameActivity.PLAYER;
import static ru.pmmlabs.ovoshchevoz.GameActivity.PLAYER_NAME;


public class AnswerActivity extends FragmentActivity {
    int player = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        player = getIntent().getIntExtra(PLAYER, 1);
        TextView playerName = findViewById(R.id.playerName);
        String playerNameString = getIntent().getStringExtra(PLAYER_NAME);
        playerName.setText(playerNameString);
        playerName = findViewById(R.id.playerNameShadow);
        playerName.setText(playerNameString);
    }

    public void yesButtonClick(View v){
        sendResult(true);
    }

    public void noButtonClick(View v){
        sendResult(false);
    }

    private void sendResult(boolean guessed) {
        Intent data = new Intent();
        data.putExtra(GUESSED, guessed);
        data.putExtra(PLAYER, player);
        setResult(RESULT_OK, data);
        finish();
    }

}