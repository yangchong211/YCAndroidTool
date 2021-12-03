package com.yc.netlib.floating;




import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Screen {
    public static final int width = 0;
    public static final int height = 1;

    @IntDef({width, height})
    @Retention(RetentionPolicy.SOURCE)
    @interface screenType {
    }
}
