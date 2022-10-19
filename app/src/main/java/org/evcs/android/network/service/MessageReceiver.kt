package org.evcs.android.network.service

import android.content.Intent

interface MessageReceiver {
    fun openConsentDialog(consentIntent: Intent);
}