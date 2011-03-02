package com.fsck.splitview;

import com.fsck.splitview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;


class SplitView extends LinearLayout implements OnTouchListener{

    final float scale = getContext().getResources().getDisplayMetrics().density;


    private int mHandleId;
	private View mHandle;

    private int mPrimaryContentId;
    private View mPrimaryContent;

    private int mSecondaryContentId;
    private View mSecondaryContent;

    private boolean mDragging;
    private float mPointerOffset;

    public SplitView(Context context, AttributeSet attrs) {
                super(context, attrs);
	
		TypedArray viewAttrs = context.obtainStyledAttributes(attrs, R.styleable.SplitView);
    
		RuntimeException e = null;
    mHandleId = viewAttrs.getResourceId(R.styleable.SplitView_handle, 0);
		if (mHandleId == 0) {
		e= new IllegalArgumentException(viewAttrs.getPositionDescription() +
					": The required attribute handle must refer to a valid child view.");
		}

    mPrimaryContentId = viewAttrs.getResourceId(R.styleable.SplitView_primaryContent, 0);
		if (mPrimaryContentId == 0) {
		e= new IllegalArgumentException(viewAttrs.getPositionDescription() +
					": The required attribute primaryContent must refer to a valid child view.");
		}

    
    mSecondaryContentId = viewAttrs.getResourceId(R.styleable.SplitView_secondaryContent, 0);
		if (mSecondaryContentId == 0) {
	    e = new IllegalArgumentException(viewAttrs.getPositionDescription() +
					": The required attribute secondaryContent must refer to a valid child view.");
		}



		viewAttrs.recycle();
		
		if (e != null) {
			throw e;
		}

    
    
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mHandle = findViewById(mHandleId); 
        if (mHandle == null ) {
            String name = getResources().getResourceEntryName(mHandleId);
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id." + name + "'");

        }
        mPrimaryContent = findViewById(mPrimaryContentId); 
        if (mPrimaryContent == null ) {
            String name = getResources().getResourceEntryName(mPrimaryContentId);
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id." + name + "'");

        }
        mSecondaryContent = findViewById(mSecondaryContentId); 
        if (mSecondaryContent == null ) {
            String name = getResources().getResourceEntryName(mSecondaryContentId);
            throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id." + name + "'");

        }
        
        mHandle.setOnTouchListener(this);
        setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent me) {
         
         ViewGroup.LayoutParams thisParams = getLayoutParams();
        // Only capture drag events if we start 
        if (view != mHandle ) {
            return false;
        }

        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            mDragging = true;
            if (getOrientation() == VERTICAL) {
            mPointerOffset = me.getRawY() - mPrimaryContent.getMeasuredHeight();
            } else {
            mPointerOffset = me.getRawX() - mPrimaryContent.getMeasuredWidth();
            }
        }
        if (me.getAction() == MotionEvent.ACTION_UP) {
            mDragging = false;
        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
            ViewGroup.LayoutParams params = mPrimaryContent.getLayoutParams();
            if (getOrientation() == VERTICAL) {
                    int newHeight =  (int) ((scale * me.getRawY()) - mPointerOffset);
                params.height = newHeight;
            } else {
                int newWidth =  (int) ((scale * me.getRawX()) - mPointerOffset);
                params.width = newWidth;
            }   
            mPrimaryContent.setLayoutParams(params);



        }

        return true;
    }

};
