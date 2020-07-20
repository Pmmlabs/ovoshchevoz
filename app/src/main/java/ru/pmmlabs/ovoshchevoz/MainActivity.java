package ru.pmmlabs.ovoshchevoz;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

    }

    public void buttonClick(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerNames", new String[]{
                ((EditText)findViewById(R.id.player1Name)).getText().toString(),
                ((EditText)findViewById(R.id.player2Name)).getText().toString()
        });
        startActivity(intent);
    }
}