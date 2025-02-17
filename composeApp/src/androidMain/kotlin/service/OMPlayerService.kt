package io.ikutsu.osumusic.service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import io.ikutsu.osumusic.R
import org.koin.android.ext.android.inject

class OMPlayerService: MediaSessionService() {

    private val mediaSession: MediaSession by inject()

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = mediaSession

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val notificationProvider = DefaultMediaNotificationProvider(this)
        notificationProvider.setSmallIcon(R.drawable.ic_notification)
        this.setMediaNotificationProvider(notificationProvider)
        setListener(SessionServiceListener())
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        mediaSession.player.stop()
        stopSelf()
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        mediaSession.apply {
            release()
            player.release()
        }
        clearListener()
        super.onDestroy()
    }

    @UnstableApi
    inner class SessionServiceListener: Listener {
        override fun onForegroundServiceStartNotAllowedException() {
            if (
                Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
    }
}