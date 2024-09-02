package io.ikutsu.osumusic.core.di

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import io.ikutsu.osumusic.MainActivity
import io.ikutsu.osumusic.player.player.OMPlayerController
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
actual val platformModule: Module = module {
    single(
        createdAtStart = true
    ) {
        ExoPlayer.Builder(androidContext())
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .build()
    } bind ExoPlayer::class

    single {
        MediaSession.Builder(androidContext(), get<ExoPlayer>())
            .setSessionActivity(
                PendingIntent.getActivity(
                    androidContext(),
                    0,
                    Intent(androidContext(), MainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }

    singleOf(::OMPlayerController)
}