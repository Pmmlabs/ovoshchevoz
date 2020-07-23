package ru.pmmlabs.ovoshchevoz;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;


public class MainActivity extends FragmentActivity {
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(getBaseContext(),
                new Integer[]{R.drawable.ru, R.drawable.gb});
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
    }

    public void buttonClick(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerNames", new String[]{
                ((EditText)findViewById(R.id.player1Name)).getText().toString(),
                ((EditText)findViewById(R.id.player2Name)).getText().toString()
        });
        int[] wordsFiles = new int[]{R.raw.ru, R.raw.en};
        intent.putExtra("lang", wordsFiles[spinner.getSelectedItemPosition()]);
        startActivity(intent);
    }
}