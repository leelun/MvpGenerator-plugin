package com.common.core.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * @author liulun
 * @version V1.0
 * @Description: 語音播放
 * @date 2016/12/24 11:58
 */
public class MediaPlayUtils {
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private boolean mIsComplete;
    private String mAssetsRes;
    private OnMediaPlayListener mOnMediaPlayListener;

    public void setOnMediaPlayListener(OnMediaPlayListener onMediaPlayListener) {
        mOnMediaPlayListener = onMediaPlayListener;
    }

    public MediaPlayUtils(Context context) {
        mContext = context;
    }
    public boolean isComplete(){
        return mIsComplete;
    }
    public void play(String assetsRes) throws Exception {
        mAssetsRes = assetsRes;
        MediaPlayer mediaPlayer;
        if (mMediaPlayer == null) {
            mediaPlayer = createPlayer();
            mMediaPlayer = mediaPlayer;
        } else {
            mediaPlayer = mMediaPlayer;
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        AssetFileDescriptor fileDescriptor = mContext.getAssets().openFd(assetsRes);
        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                fileDescriptor.getStartOffset(),
                fileDescriptor.getLength());
        mediaPlayer.prepareAsync();
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public MediaPlayer createPlayer() throws Exception {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsComplete = false;
                mp.start();
                if (mOnMediaPlayListener != null) {
                    mOnMediaPlayListener.onPrepared(mAssetsRes);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mOnMediaPlayListener != null) {
                    mOnMediaPlayListener.onCompletion(mAssetsRes);
                }
                mIsComplete = true;
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mOnMediaPlayListener != null) {
                    mOnMediaPlayListener.onError(mAssetsRes);
                }
                return true;
            }
        });
        return mediaPlayer;
    }

    public interface OnMediaPlayListener {
        void onPrepared(String assetsRes);

        void onCompletion(String assetsRes);

        void onError(String assetsRes);
    }
}
