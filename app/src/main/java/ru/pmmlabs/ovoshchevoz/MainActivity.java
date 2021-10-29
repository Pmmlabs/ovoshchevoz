package ru.pmmlabs.ovoshchevoz;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;


public class MainActivity extends FragmentActivity {
    Spinner spinner;
    private static final Object[][] wordSets = {
            {R.drawable.ru, R.raw.ru, "Гонки"},
            {R.drawable.gb, R.raw.en, "Beatles"},
            {R.drawable.aria, R.raw.aria, "Ария"},
            {R.drawable.slot, R.raw.slot, "Слот"},
            {R.drawable.slot, R.raw.kish, "КиШ"},
            {R.drawable.slot, R.raw.gaga, "Lady Gaga"},
            {R.drawable.disco, R.raw.discoteka_avaria, "Дискотека Авария"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // Image spinner
        //Integer[] drawables = new Integer[wordSets.length];
        //for (int i = 0, wordSetsLength = wordSets.length; i < wordSetsLength; i++)
        //    drawables[i]=wordSets[i][0];
        //SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(getBaseContext(), drawables);

        // Strings spinner
        String[] names = new String[wordSets.length];
        for (int i = 0, wordSetsLength = wordSets.length; i < wordSetsLength; i++)
            names[i]=wordSets[i][2].toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row, R.id.setName, names);

        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
    }

    public void buttonClick(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerNames", new String[]{
                ((EditText)findViewById(R.id.player1Name)).getText().toString(),
                ((EditText)findViewById(R.id.player2Name)).getText().toString()
        });
        intent.putExtra("lang", (int) wordSets[spinner.getSelectedItemPosition()][1]);
        startActivity(intent);
    }
}