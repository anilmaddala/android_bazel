package com.example.notekeeper

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp(Application::class)
class NoteKeeperApplication : Hilt_NoteKeeperApplication()
