package com.mgtj.airadio.base.decoration;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtj.airadio.base.utils.ViewUtils;


/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * See the license above for details.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;
    private int mDividerHeight;

    /**
     * 是否需要包含PaddingLeft & PaddingRight
     */
    private boolean mNeedPaddingLR;
    /**
     * 是否需要包含PaddingTop & PaddingBottom
     */
    private boolean mNeedPaddingTB;

    /**
     * 构造函数
     *
     * @param context       上下文
     * @param orientation   方向
     * @param dividerHeight 高度
     * @param color         颜色
     */
    public DividerItemDecoration(Context context, int orientation, int dividerHeight, int color) {
        this(context, orientation, dividerHeight, color, false, false);
    }

    public DividerItemDecoration(Context context, int orientation, int dividerHeight, int color, boolean needPaddingLR, boolean needPaddingTB) {
        mDividerHeight = dividerHeight;
        mDivider = new ColorDrawable(color);
        setOrientation(orientation);

        mNeedPaddingLR = needPaddingLR;
        mNeedPaddingTB = needPaddingTB;
    }

    public static DividerItemDecoration create(Context c, int color) {
        return new DividerItemDecoration(c, VERTICAL_LIST, ViewUtils.dip2pxInt(c, 0.5f), color);
    }

    public static DividerItemDecoration create(Context c, int color, boolean needPaddingLR, boolean needPaddingTB) {
        return new DividerItemDecoration(c, VERTICAL_LIST, ViewUtils.dip2pxInt(c, 0.5f), color, needPaddingLR, needPaddingTB);
    }


    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = 0;
        int right = parent.getWidth();
//        if (mNeedPaddingLR) {
//            left = parent.getPaddingLeft();
//            right -= parent.getPaddingRight();
//        }

        final int childCount = parent.getChildCount();
        if (childCount == 1) return;
        for (int i = 0; i < childCount; i++) {
            if (parent.getAdapter().getItemViewType(i) != 99
                    && parent.getAdapter().getItemViewType(i) != 100 && parent.getAdapter().getItemViewType(i) != 999) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin
                        + Math.round(ViewCompat.getTranslationY(child));
                final int bottom = top + mDividerHeight;
                if (mNeedPaddingLR) {
                    left = child.getPaddingLeft();
                    //  right = child.getPaddingRight();
                }
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = 0;
        int bottom = parent.getHeight();
        if (mNeedPaddingTB) {
            top = parent.getPaddingTop();
            bottom -= parent.getPaddingBottom();
        }


        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin
                    + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

}