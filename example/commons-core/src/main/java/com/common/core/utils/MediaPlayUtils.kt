package com.common.core.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer

/**
 * @author liulun
 * @version V1.0
 * @Description: 語音播放
 * @date 2016/12/24 11:58
 */
class MediaPlayUtils(private val mContext: Context) {
    private var mMediaPlayer: MediaPlayer? = null
    var isComplete: Boolean = false
        private set
    private var mAssetsRes: String? = null
    private var mOnMediaPlayListener: OnMediaPlayListener? = null

    fun setOnMediaPlayListener(onMediaPlayListener: OnMediaPlayListener) {
        mOnMediaPlayListener = onMediaPlayListener
    }

    @Throws(Exception::class)
    fun play(assetsRes: String) {
        mAssetsRes = assetsRes
        val mediaPlayer: MediaPlayer
        if (mMediaPlayer == null) {
            mediaPlayer = createPlayer()
            mMediaPlayer = mediaPlayer
        } else {
            mediaPlayer = mMediaPlayer as MediaPlayer
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
        val fileDescriptor = mContext.assets.openFd(assetsRes)
        mediaPlayer.setDataSource(fileDescriptor.fileDescriptor,
                fileDescriptor.startOffset,
                fileDescriptor.length)
        mediaPlayer.prepareAsync()
    }

    fun start() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
        }
    }

    fun stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.reset()
        }
    }

    /**
     * 暂停播放
     */
    fun pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.pause()
        }
    }

    @Throws(Exception::class)
    fun createPlayer(): MediaPlayer {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener { mp ->
            isComplete = false
            mp.start()
            if (mOnMediaPlayListener != null) {
                mOnMediaPlayListener!!.onPrepared(mAssetsRes)
            }
        }
        mediaPlayer.setOnCompletionListener {
            if (mOnMediaPlayListener != null) {
                mOnMediaPlayListener!!.onCompletion(mAssetsRes)
            }
            isComplete = true
        }
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            if (mOnMediaPlayListener != null) {
                mOnMediaPlayListener!!.onError(mAssetsRes)
            }
            true
        }
        return mediaPlayer
    }

    interface OnMediaPlayListener {
        fun onPrepared(assetsRes: String?)

        fun onCompletion(assetsRes: String?)

        fun onError(assetsRes: String?)
    }
}
