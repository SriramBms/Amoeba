package com.eo5.amoeba.interfaces;

import android.view.View;

/**
 * Created by Sriram on 3/15/2016.
 */
public interface ReplicateListener {
    void onClick(View v);
    void fissionBefore();
    void fissionAfter();
}
