package com.common.quick.mvvm;

import android.view.View;

@FunctionalInterface
public interface BindingAction {
    void call(View view);
}
