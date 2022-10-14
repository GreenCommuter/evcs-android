package org.evcs.android.network.service

interface MessageReceiver {
    fun onReceive(s: String?)
}