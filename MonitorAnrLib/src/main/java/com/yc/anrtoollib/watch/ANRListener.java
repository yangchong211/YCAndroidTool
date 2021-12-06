package com.yc.anrtoollib.watch;

import androidx.annotation.NonNull;

public interface ANRListener {
    /**
     * Called when an ANR is detected.
     *
     * @param error The error describing the ANR.
     */
    void onAppNotResponding(@NonNull ANRError error);

}
