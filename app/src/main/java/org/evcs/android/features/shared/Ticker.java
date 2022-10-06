package org.evcs.android.features.shared;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;

public final class Ticker {

    private final BroadcastReceiver mTickReceiver;
    private Context mContext;
    private OnTickListener mOnTickListener;
    private Boolean mTicketReceiverRegistered = false;

    public Ticker(@NonNull Context context) {
        mContext = context;
        mTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mOnTickListener == null) return;

                mOnTickListener.onTick();
            }
        };
    }

    public void setListener(@NonNull OnTickListener listener) {
        if (mOnTickListener == null) {
            mContext.registerReceiver(mTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
            mTicketReceiverRegistered = true;
        }

        this.mOnTickListener = listener;
    }

    public void dispose() {
        if (mTicketReceiverRegistered){
            mContext.unregisterReceiver(mTickReceiver);
        }
        mOnTickListener = null;
    }

    public interface OnTickListener {

        void onTick();

    }

}
