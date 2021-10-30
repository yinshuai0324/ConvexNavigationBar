package com.itparsa.circlenavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.itparsa.circlenavigation.R;

@SuppressWarnings("unused")
public class FloatingActionButton extends AppCompatTextView {
    public static final int FAB_TYPE_CIRCLE = 0;
    public static final int FAB_TYPE_SQUARE = 1;
    public static final int FAB_TYPE_ROUNDED_SQUARE = 2;

    public static final int FAB_SIZE_NORMAL = 10;
    public static final int FAB_SIZE_MINI = 11;

    public static final int FAB_ICON_START = 30;
    public static final int FAB_ICON_TOP = 31;
    public static final int FAB_ICON_END = 32;
    public static final int FAB_ICON_BOTTOM = 33;
    public static final int FAB_BACKGROUND_NOT_DEFINED = -1;

    private int fabType;
    private int fabSize;
    private String fabText;
    private boolean fabTextAllCaps;
    private int fabTextColor;
    private float fabElevation;
    private int fabColor;
    private int fabBackground;
    private Drawable fabIcon;
    private int fabIconColor;
    private int fabIconPosition;

    private boolean isCreated;

    public FloatingActionButton(Context context) {
        this(context, null, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initTypedArray(attrs);
    }

    private void initTypedArray(AttributeSet attrs) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, 0, 0);

        fabType = ta.getInt(R.styleable.FloatingActionButton_fabType, FAB_TYPE_CIRCLE);
        fabSize = ta.getInt(R.styleable.FloatingActionButton_fabSizes, FAB_SIZE_NORMAL);
        fabIconPosition = ta.getInt(R.styleable.FloatingActionButton_fabIconPosition, FAB_ICON_START);
        fabText = ta.getString(R.styleable.FloatingActionButton_fabText);
        fabTextAllCaps = ta.getBoolean(R.styleable.FloatingActionButton_fabTextAllCaps, false);
        fabTextColor = ta.getColor(R.styleable.FloatingActionButton_fabTextColor, ContextCompat.getColor(getContext(), R.color.colorFabText));
        fabElevation = ta.getDimension(R.styleable.FloatingActionButton_fabElevation, getResources().getDimension(R.dimen.fab_default_elevation));
        fabColor = ta.getColor(R.styleable.FloatingActionButton_fabColor, ContextCompat.getColor(getContext(), R.color.colorAccent));
        fabBackground = FAB_BACKGROUND_NOT_DEFINED;
        fabIcon = ta.getDrawable(R.styleable.FloatingActionButton_fabIcon);
        fabIconColor = ta.getColor(R.styleable.FloatingActionButton_fabIconColor, ContextCompat.getColor(getContext(), R.color.colorFabIcon));

        ta.recycle();
    }

    private void buildView() {
        isCreated = true;
        setGravity(Gravity.CENTER);
        initFabBackground();
        initFabIconColor();
        initFabIconPosition();
        initFabText();
        initFabPadding();
        initFabShadow();
    }

    private void initFabBackground() {
        Drawable backgroundDrawable;
        if (fabBackground == FAB_BACKGROUND_NOT_DEFINED) {
            switch (fabType) {
                case FAB_TYPE_SQUARE:
                    backgroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.fab_square_bg);
                    break;
                case FAB_TYPE_ROUNDED_SQUARE:
                    backgroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.fab_rounded_square_bg);
                    break;
                default:
                    backgroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.fab_circle_bg);
                    break;
            }

            backgroundDrawable.mutate().setColorFilter(fabColor, PorterDuff.Mode.SRC_IN);
        } else
            backgroundDrawable = ContextCompat.getDrawable(getContext(), fabBackground);

        Drawable selectableDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            selectableDrawable = new RippleDrawable(ColorStateList.valueOf(Color.argb(150, 255, 255, 255)),
                    null, backgroundDrawable);
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.setExitFadeDuration(400);
            stateListDrawable.setAlpha(45);
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.argb(150, 255, 255, 255)));
            stateListDrawable.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));

            selectableDrawable = stateListDrawable;
        }

        LayerDrawable backgroundLayers = new LayerDrawable(new Drawable[]{
                backgroundDrawable,
                selectableDrawable});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(backgroundLayers);
        } else {
            setBackgroundDrawable(backgroundLayers);
        }
    }

    private void initFabIconColor() {
        if (fabIcon != null) {
            fabIcon.mutate().setColorFilter(fabIconColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void initFabIconPosition() {
        if (fabIcon == null) {
            return;
        }

        int h = fabIcon.getIntrinsicHeight();
        int w = fabIcon.getIntrinsicWidth();
        fabIcon.setBounds(0, 0, w, h);
        setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.fab_text_horizontal_margin_mini));

        switch (fabIconPosition) {
            case FAB_ICON_TOP:
                setCompoundDrawables(null, fabIcon, null, null);
                break;
            case FAB_ICON_END:
                setCompoundDrawables(null, null, fabIcon, null);
                break;
            case FAB_ICON_BOTTOM:
                setCompoundDrawables(null, null, null, fabIcon);
                break;
            default:
                setCompoundDrawables(fabIcon, null, null, null);
                break;
        }
    }

    private void initFabText() {
        if (fabText == null || fabText.length() == 0) {
            return;
        }

        int maxLength = 25;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        setFilters(fArray);

        setEllipsize(TextUtils.TruncateAt.END);
        setTextColor(fabTextColor);
        setText(fabText);
        setAllCaps(fabTextAllCaps);
    }

    private void initFabPadding() {
        int paddingSize = fabSize == FAB_SIZE_MINI
                ? getResources().getDimensionPixelSize(R.dimen.fab_text_horizontal_margin_mini)
                : getResources().getDimensionPixelSize(R.dimen.fab_text_horizontal_margin_normal);
        setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
    }

    private void initFabShadow() {
        ViewCompat.setElevation(this, fabElevation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams thisParams = getLayoutParams();

        boolean noText = fabText == null || fabText.length() == 0;
        boolean topBottom = fabIconPosition == FAB_ICON_TOP || fabIconPosition == FAB_ICON_BOTTOM;

        if (fabSize == FAB_SIZE_MINI) {
            setMinHeight(getResources().getDimensionPixelSize(R.dimen.fab_size_mini));
            setMinWidth(getResources().getDimensionPixelSize(R.dimen.fab_size_mini));
            if (noText) {
                thisParams.width = getResources().getDimensionPixelSize(R.dimen.fab_size_mini);
                thisParams.height = getResources().getDimensionPixelSize(R.dimen.fab_size_mini);
            } else {
                thisParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (topBottom) {
                    thisParams.height = getResources().getDimensionPixelSize(R.dimen.fab_size_mini) * 2;
                } else {
                    thisParams.height = getResources().getDimensionPixelSize(R.dimen.fab_size_mini);
                }
            }
        } else {
            setMinHeight(getResources().getDimensionPixelSize(R.dimen.fab_size_normal));
            setMinWidth(getResources().getDimensionPixelSize(R.dimen.fab_size_normal));
            if (noText) {
                thisParams.width = getResources().getDimensionPixelSize(R.dimen.fab_size_normal);
                thisParams.height = getResources().getDimensionPixelSize(R.dimen.fab_size_normal);
            } else {
                thisParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                if (topBottom) {
                    thisParams.height = getResources().getDimensionPixelSize(R.dimen.fab_size_normal) * 2;
                } else {
                    thisParams.height = getResources().getDimensionPixelSize(R.dimen.fab_size_normal);
                }
            }

        }

        this.setLayoutParams(thisParams);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isCreated) {
            buildView();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isCreated) {
            buildView();
        }
    }

    public int getFabType() {
        return fabType;
    }

    public void setFabType(int fabType) {
        this.fabType = fabType;
        buildView();
    }

    public int getFabSize() {
        return fabSize;
    }

    public void setFabSize(int fabSize) {
        this.fabSize = fabSize;
        buildView();
    }

    public String getFabText() {
        return fabText;
    }

    public void setFabText(String fabText) {
        this.fabText = fabText;
        buildView();
    }

    public boolean isFabTextAllCaps() {
        return fabTextAllCaps;
    }

    public void setFabTextAllCaps(boolean fabTextAllCaps) {
        this.fabTextAllCaps = fabTextAllCaps;
        buildView();
    }

    public int getFabTextColor() {
        return fabTextColor;
    }

    public void setFabTextColor(int fabTextColor) {
        this.fabTextColor = fabTextColor;
        buildView();
    }

    public float getFabElevation() {
        return fabElevation;
    }

    public int getFabBackground() {
        return fabBackground;
    }

    public void setFabBackground(@DrawableRes int fabBackground) {
        this.fabBackground = fabBackground;
        buildView();
    }

    public void setFabElevation(float fabElevation) {
        this.fabElevation = fabElevation;
        buildView();
    }

    public int getFabColor() {
        return fabColor;
    }

    public void setFabColor(int fabColor) {
        this.fabColor = fabColor;
        buildView();
    }

    public Drawable getFabIcon() {
        return fabIcon;
    }

    public void setFabIcon(Drawable fabIcon) {
        this.fabIcon = fabIcon;
        buildView();
    }

    public void setFabIconResource(@DrawableRes int resId) {
        Drawable icon = getResources().getDrawable(resId);
        setFabIcon(icon);
    }

    public int getFabIconColor() {
        return fabIconColor;
    }

    public void setFabIconColor(int fabIconColor) {
        this.fabIconColor = fabIconColor;
        buildView();
    }

    public int getFabIconPosition() {
        return fabIconPosition;
    }

    public void setFabIconPosition(int fabIconPosition) {
        this.fabIconPosition = fabIconPosition;
        buildView();
    }

}
