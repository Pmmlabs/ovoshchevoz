package ru.pmmlabs.ovoshchevoz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.plattysoft.leonids.ParticleSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends FragmentActivity {

    private static final String TAG = "GameActivity";
    public static final int REQUEST_CODE = 123;
    public static final String GUESSED = "guessed";
    public static final String PLAYER = "player";
    public static final String PLAYER_NAME = "playerName";
    public static final int POINTS_TO_WIN = 10;
    int timerSeconds = 10;
    int player1points = 0;
    int player2points = 0;
    int fileSize = 0;
    TextView wordTextView;
    TextView timerView;
    Timer timer;
    Set<String> usedWords = new HashSet<>();
    String[] playerNames;
    private SoundManager soundManager;
    private boolean stop = false;
    private int wordsFile;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntArray("points", new int[]{player1points, player2points});
        outState.putCharSequence("word", wordTextView.getText());
        outState.putBoolean("stop", stop);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int[] points = savedInstanceState.getIntArray("points");
        if (points != null) {
            player1points = points[0];
            player2points = points[1];
            refreshPoints();
        }
        wordTextView.setText(savedInstanceState.getCharSequence("word"));
        stop = savedInstanceState.getBoolean("stop");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerNames = getIntent().getStringArrayExtra("playerNames");
        wordsFile = getIntent().getIntExtra("lang", R.raw.ru);
        wordTextView = findViewById(R.id.wordTextView);
        timerView = findViewById(R.id.timerView);

        findViewById(R.id.player1win).setVisibility(View.INVISIBLE);
        findViewById(R.id.player2win).setVisibility(View.INVISIBLE);

        try {
            fileSize = linesCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nextWord();
        //startTimer();
        refreshPoints();

        ((TextView) findViewById(R.id.playerName1)).setText(playerNames[0]);
        ((TextView) findViewById(R.id.playerName2)).setText(playerNames[1]);

        ImageButton button1 = findViewById(R.id.button1);
        ImageButton button2 = findViewById(R.id.button2);
        View.OnTouchListener onTouchListener = getOnTouchListener();
        button1.setOnTouchListener(onTouchListener);
        button2.setOnTouchListener(onTouchListener);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        soundManager = new SoundManager();
        soundManager.addSound(this, R.raw.wrong);
        soundManager.addSound(this, R.raw.duck);
        soundManager.addSound(this, R.raw.ding);
        soundManager.addSound(this, R.raw.firework);
    }

    private void nextWord() {
        Random random = new Random();
        String word = "";
        do {
            int wordNumber = random.nextInt(fileSize);

            InputStreamReader isr = new InputStreamReader(getResources().openRawResource(wordsFile));
            BufferedReader br = new BufferedReader(isr);
            try {
                for (int i = 0; i < wordNumber && word != null; i++) {
                    word = br.readLine();
                }
                wordTextView.setText(word + (char) 0xA0); // non-breakable space to avoid cutting of last italic letter
                isr.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (usedWords.contains(word));
        usedWords.add(word);

        timerSeconds = 10;
    }

    @Override
    public void onBackPressed() {
        stopTimer();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (!this.isFinishing()){
            Log.d(TAG, "onpause");
            stopTimer();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (!stop) {
            Log.d(TAG, "onresume");
            startTimer();
        }
        super.onResume();
    }

    private void startTimer() {
        stopTimer();
        Log.d(TAG, "Start timer");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerView.setText(String.valueOf(timerSeconds));
                        timerSeconds--;
                        if (timerSeconds < 0) {
                            // time is up
                            soundManager.playSound(R.raw.wrong);
                            findViewById(R.id.wrongStub).setVisibility(View.VISIBLE);
                            Timer timerWrong = new Timer();
                            timerWrong.schedule(new TimerTask() {
                               @Override
                               public void run() {
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           findViewById(R.id.wrongStub).setVisibility(View.GONE);
                                           nextWord();
                                           startTimer();
                                       }
                                   });
                               }
                            }, 400);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        Log.d(TAG, "stopTimer");
        if (timer != null) {
            timer.cancel();
        }
    }

    void refreshPoints() {
        refreshPoints(R.id.points1, player1points);
        refreshPoints(R.id.points2, player2points);
    }

    private void refreshPoints(int textViewId, int points) {
        TextView pointsTextView = findViewById(textViewId);
        pointsTextView.setText(String.valueOf(points));
    }

    int linesCount() throws IOException {
        InputStream inputStream = getResources().openRawResource(wordsFile);
        InputStreamReader isr = new InputStreamReader(inputStream);
        LineNumberReader count = new LineNumberReader(isr);
        while (count.skip(Long.MAX_VALUE) > 0) {
            // Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
        }

        int result = count.getLineNumber();
        count.close();
        isr.close();
        inputStream.close();
        return result;
    }

    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "onTouch started");
                    handleClick(Integer.parseInt(v.getContentDescription().toString()));
                    v.performClick();
                    Log.d(TAG, "onTouch ended");
                    return true;
                }
                Log.d(TAG, "onTouch is not action down");
                return false;
            }
        };
    }

    private void handleClick(int player) {
        if (!stop) {
            stopTimer();
            soundManager.playSound(R.raw.duck);
            ImageButton button1 = findViewById(R.id.button1);
            ImageButton button2 = findViewById(R.id.button2);
            if (button1.isPressed() && button2.isPressed()) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "YES",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
            Log.d(TAG, "Answer intent is creating...");
            Intent intent = new Intent(this, AnswerActivity.class);
            intent.putExtra(PLAYER, player);
            intent.putExtra(PLAYER_NAME, playerNames[player - 1]);
            startActivityForResult(intent, REQUEST_CODE);
            Log.d(TAG, "Answer intent created");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            boolean guessed = data.getBooleanExtra(GUESSED, false);
            int player = data.getIntExtra(PLAYER, 1);
            if (guessed) {
                soundManager.playSound(R.raw.ding);
                switch (player) {
                    case 1:
                        player1points++;
                        break;
                    case 2:
                        player2points++;
                        break;
                }
                refreshPoints();
                if (player1points == POINTS_TO_WIN || player2points == POINTS_TO_WIN) {
                    findViewById(player1points == POINTS_TO_WIN ? R.id.player1win : R.id.player2win).setVisibility(View.VISIBLE);
                    wordTextView.setText(String.format("%s %s" + (char) 0xA0, getString(R.string.win_caption), player1points == POINTS_TO_WIN ? playerNames[0] : playerNames[1]));
                    stopTimer();
                    // fireworks
                    soundManager.playSound(R.raw.firework);
                    long delay = 0;
                    int[] fireworks = new int[]{R.id.firework1, R.id.firework3, R.id.firework, R.id.firework4, R.id.firework2};
                    for (int fw : fireworks) {
                        fireworks(findViewById(fw), delay);
                        delay += 400;
                    }
                    stop = true;
                } else {
                    nextWord();
                }
            }
        }
    }

    void fireworks(final View v, long delay) {
        Timer fireworksTimer = new Timer();
        final GameActivity gameActivity = this;
        fireworksTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ParticleSystem ps = new ParticleSystem(gameActivity, 150, R.drawable.star, 800);
                        ps.setScaleRange(0.7f, 12f);
                        ps.setSpeedRange(0.1f, 0.4f);
                        ps.setRotationSpeedRange(90, 180);
                        ps.setFadeOut(200, new AccelerateInterpolator());
                        ps.oneShot(v, 100);

                        ParticleSystem ps2 = new ParticleSystem(gameActivity, 150, R.drawable.star_red, 800);
                        ps2.setScaleRange(0.7f, 12f);
                        ps2.setSpeedRange(0.1f, 0.25f);
                        ps2.setRotationSpeedRange(90, 180);
                        ps2.setFadeOut(200, new AccelerateInterpolator());
                        ps2.oneShot(v, 100);
                    }
                });
            }
        }, delay);
    }
}