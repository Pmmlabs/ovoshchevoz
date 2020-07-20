package ru.pmmlabs.ovoshchevoz;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.SparseIntArray;

public class SoundManager {

    private SoundPool mSoundPool;

    private SparseIntArray mSoundPoolMap = new SparseIntArray();

    private Handler mHandler = new Handler();

    private static final int MAX_STREAMS = 2;
    private static final int STOP_DELAY_MILLIS = 3000;

    public SoundManager() {
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Put the sounds to their correspondig keys in sound pool.
     */
    public void addSound(Context context, int soundID) {
        mSoundPoolMap.put(soundID, mSoundPool.load(context, soundID, 1));
    }

    /**
     * Find sound with the key and play it
     */
    public void playSound(int soundID) {
        boolean hasSound = mSoundPoolMap.indexOfKey(soundID) >= 0;
        if(!hasSound){
            return;
        }

        final int soundId = mSoundPool.play(mSoundPoolMap.get(soundID), 1, 1, 1, 0, 1f);
        scheduleSoundStop(soundId);
    }

    /**
     * Schedule the current sound to stop after set milliseconds
     */
    private void scheduleSoundStop(final int soundId){
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mSoundPool.stop(soundId);
            }
        }, STOP_DELAY_MILLIS);
    }
}