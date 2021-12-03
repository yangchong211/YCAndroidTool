package com.yc.netlib.floating;




import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MoveType {
    static final int fixed = 0;
    public static final int inactive = 1;
    public static final int active = 2;
    public static final int slide = 3;
    public static final int back = 4;

    @IntDef({fixed, inactive, active, slide, back})
    @Retention(RetentionPolicy.SOURCE)
    @interface MOVE_TYPE {
    }
}
