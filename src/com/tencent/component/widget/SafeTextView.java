package com.tencent.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 插桩代码,仅为保持代码格式不变,便于本工程完成后移植到实际项目中
 */
public class SafeTextView extends TextView {
    public SafeTextView(Context context) {
        super(context);
    }

    public SafeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SafeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
