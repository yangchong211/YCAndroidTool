package com.yc.cpumonitor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class MyLinearLayout extends LinearLayout {

	private boolean touchEventsDisabled = true;
	
	public MyLinearLayout(Context context) {
		this(context,null);
	}
	
	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return touchEventsDisabled;
	}
	
	public void interceptChildTouchEvents(boolean b) {
		touchEventsDisabled = b;
	}
}
