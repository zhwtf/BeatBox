package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenghao on 2017-07-06.
 */

public class BeatBox {
    //添加两个常量：一个用于日志记录，另一个用于存储声音资源文件目录名
    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        // This old constructor is deprecated, but we need it for
        // compatibility.
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);

        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            /*
            AssetManager.list(String)方法能列出指定目录中的所有文件名。因此，只要传入声音资
源所在的目录，就能看到其中的所有.wav文件。
             */
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }

        }
    }


    //load方法载入音频
    /*
    调用mSoundPool.load(AssetFileDescriptor, int)方法可以把文件载入SoundPool待播。
为方便管理、重播或卸载音频文件，mSoundPool.load(...)方法会返回一个int型ID。这实际就
是存储在mSoundId中的ID。调用openFd(String)方法有可能抛出IOException，load(Sound)
方法也是如此。
     */
    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);

    }

    public List<Sound> getSounds() {
        return mSounds;
    }




    //播放音频
    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    /*
    检查通过以后，就可以调用SoundPool.play(int, float, float, int, int, float)方法
播放音频了。这些参数依次是：音频ID、左音量、右音量、优先级（无效）、是否循环以及播放
速率。我们需要最大音量和常速播放，所以传入值1.0。是否循环参数传入0值，代表不循环。（如
果想无限循环，可以传入1。我们觉得这会非常令人讨厌。
     */
    public void release() {
        mSoundPool.release();
    }


}
