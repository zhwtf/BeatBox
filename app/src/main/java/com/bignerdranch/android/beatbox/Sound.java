package com.bignerdranch.android.beatbox;

/**
 * Created by zhenghao on 2017-07-06.
 */

public class Sound {
    private String mAssetPath;
    private String mName;


    public Sound(String assetPath) {
        mAssetPath = assetPath;
        // 将文件路径用"/"分割
        String[] components = assetPath.split("/");
        // 取最后一个，即文件名
        String filename = components[components.length - 1];
        // 在知道文件名结尾是 .wav 的情况下，直接将 .wav 替换为空
        mName = filename.replace(".wav", "");

    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }


}
