package io.ikutsu.osumusic.home.presentation

sealed interface HomeAction {
    data object OnSettingClick : HomeAction
    data object OnPlayHistoryClick : HomeAction
}