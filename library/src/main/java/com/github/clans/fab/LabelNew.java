package com.github.clans.fab;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

public class LabelNew extends Button {

    public LabelNew(Context context) {
        super(context);
    }

    public LabelNew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LabelNew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
