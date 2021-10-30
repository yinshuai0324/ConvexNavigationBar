/*
 * CircleBottomBar library for Android
 * Copyright (c) 2018 Mehran Jafari (https://github.com/mehrtarh/CircleButtonBar).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itparsa.circlenavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Size;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class CircleNavigationView extends BottomNavigationView {

    private static final String TAG = "SpaceNavigationView";

    private static final String CURRENT_SELECTED_ITEM_BUNDLE_KEY = "currentItem";

    private static final String BADGES_ITEM_BUNDLE_KEY = "badgeItem";

    private static final String CHANGED_ICON_AND_TEXT_BUNDLE_KEY = "changedIconAndText";

    private static final String CENTRE_BUTTON_ICON_KEY = "centreButtonIconKey";

    private static final String SPACE_BACKGROUND_COLOR_KEY = "backgroundColorKey";

    private static final String BADGE_FULL_TEXT_KEY = "badgeFullTextKey";

    private static final String VISIBILITY = "visibilty";

    private static final int NOT_DEFINED = -777; //random number, not - 1 because it is Color.WHITE

    private static final int MAX_SPACE_ITEM_SIZE = 4;

    private static final int MIN_SPACE_ITEM_SIZE = 2;
    private final int spaceNavigationHeight = (int) getResources().getDimension(com.itparsa.circlenavigation.R.dimen.space_navigation_height);
    private final int mainContentHeight = (int) getResources().getDimension(com.itparsa.circlenavigation.R.dimen.main_content_height);
    private final int centreContentWight = (int) getResources().getDimension(com.itparsa.circlenavigation.R.dimen.centre_content_width);
    private final int centreButtonSize = (int) getResources().getDimension(com.itparsa.circlenavigation.R.dimen.space_centre_button_default_size);
    private List<CircleItem> spaceItems = new ArrayList<>();
    private List<View> spaceItemList = new ArrayList<>();
    private List<RelativeLayout> badgeList = new ArrayList<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Object> badgeSaveInstanceHashMap = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, CircleItem> changedItemAndIconHashMap = new HashMap<>();
    private CircleOnClickListener spaceOnClickListener;
    private CircleOnLongClickListener spaceOnLongClickListener;
    private Bundle savedInstanceState;
    private CentreButton centreButton;
    private RelativeLayout centreBackgroundView;
    private LinearLayout leftContent, rightContent;
    private BezierView centreContent;
    private Typeface customFont;
    private Context context;
    private int spaceItemIconSize = NOT_DEFINED;

    private int spaceItemIconOnlySize = NOT_DEFINED;

    private int spaceItemTextSize = NOT_DEFINED;

    private int spaceBackgroundColor = NOT_DEFINED;

    private int centreButtonColor = NOT_DEFINED;

    private int activeCentreButtonIconColor = NOT_DEFINED;

    private int inActiveCentreButtonIconColor = NOT_DEFINED;

    private int activeCentreButtonBackgroundColor = NOT_DEFINED;

    private int centreButtonIcon = NOT_DEFINED;

    private int centerButtonSelectedIcon = NOT_DEFINED;

    private int activeSpaceItemColor = NOT_DEFINED;

    private int inActiveSpaceItemColor = NOT_DEFINED;

    private int centreButtonRippleColor = NOT_DEFINED;

    private int currentSelectedItem = 0;

    private int contentWidth;

    private boolean isCentreButtonSelectable = false;

    private boolean isCentrePartLinear = false;

    private boolean isTextOnlyMode = false;

    private boolean isIconOnlyMode = false;

    private boolean isCustomFont = false;

    private boolean isCentreButtonIconColorFilterEnabled = true;

    private boolean shouldShowBadgeWithNinePlus = true;

    private int centerButtonResurceBackground = NOT_DEFINED;

    /**
     * Constructors
     */
    public CircleNavigationView(Context context) {
        this(context, null);
    }

    public CircleNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }


//    public void setCenterBackGroundResource(int resource)
//
//    {
//        centerBackGroundResource = resource;
//    }

    /**
     * Init custom attributes
     *
     * @param attrs attributes
     */
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            Resources resources = getResources();

            TypedArray typedArray = context.obtainStyledAttributes(attrs, com.itparsa.circlenavigation.R.styleable.CircleNavigationView);
            spaceItemIconSize = typedArray.getDimensionPixelSize(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_space_item_icon_size, resources.getDimensionPixelSize(com.itparsa.circlenavigation.R.dimen.space_item_icon_default_size));
            spaceItemIconOnlySize = typedArray.getDimensionPixelSize(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_space_item_icon_only_size, resources.getDimensionPixelSize(com.itparsa.circlenavigation.R.dimen.space_item_icon_only_size));
            spaceItemTextSize = typedArray.getDimensionPixelSize(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_space_item_text_size, resources.getDimensionPixelSize(com.itparsa.circlenavigation.R.dimen.space_item_text_default_size));
            spaceItemIconOnlySize = typedArray.getDimensionPixelSize(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_space_item_icon_only_size, resources.getDimensionPixelSize(com.itparsa.circlenavigation.R.dimen.space_item_icon_only_size));
            spaceBackgroundColor = typedArray.getColor(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_space_background_color, resources.getColor(com.itparsa.circlenavigation.R.color.space_default_color));
            centreButtonColor = typedArray.getColor(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_inactive_center_button_background_color, resources.getColor(com.itparsa.circlenavigation.R.color.centre_button_color));
            activeSpaceItemColor = typedArray.getColor(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_active_item_color, resources.getColor(com.itparsa.circlenavigation.R.color.space_white));
            inActiveSpaceItemColor = typedArray.getColor(com.itparsa.circlenavigation.R.styleable.CircleNavigationView_inactive_item_color, resources.getColor(com.itparsa.circlenavigation.R.color.default_inactive_item_color));
            centreButtonIcon = typedArray.getResourceId(R.styleable.CircleNavigationView_center_button_icon, R.drawable.near_me);
            centerButtonSelectedIcon = typedArray.getResourceId(R.styleable.CircleNavigationView_center_button_selected_icon, R.drawable.near_me);
            isCentrePartLinear = typedArray.getBoolean(R.styleable.CircleNavigationView_center_part_linear, false);
            activeCentreButtonIconColor = typedArray.getColor(R.styleable.CircleNavigationView_active_center_button_icon_color, resources.getColor(R.color.space_white));
            inActiveCentreButtonIconColor = typedArray.getColor(R.styleable.CircleNavigationView_inactive_center_button_icon_color, resources.getColor(com.itparsa.circlenavigation.R.color.default_inactive_item_color));
            activeCentreButtonBackgroundColor = typedArray.getColor(R.styleable.CircleNavigationView_active_center_button_background_color, resources.getColor(com.itparsa.circlenavigation.R.color.centre_button_color));

            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*
         * Set default colors and sizes
         */
        if (spaceBackgroundColor == NOT_DEFINED)
            spaceBackgroundColor = ContextCompat.getColor(context, com.itparsa.circlenavigation.R.color.space_default_color);

        if (centreButtonColor == NOT_DEFINED)
            centreButtonColor = ContextCompat.getColor(context, com.itparsa.circlenavigation.R.color.centre_button_color);

        if (centreButtonIcon == NOT_DEFINED)
            centreButtonIcon = R.drawable.near_me;

        if (centreButtonIcon == NOT_DEFINED)
            centerButtonSelectedIcon = centreButtonIcon;

        if (activeSpaceItemColor == NOT_DEFINED)
            activeSpaceItemColor = ContextCompat.getColor(context, com.itparsa.circlenavigation.R.color.space_white);

        if (inActiveSpaceItemColor == NOT_DEFINED)
            inActiveSpaceItemColor = ContextCompat.getColor(context, com.itparsa.circlenavigation.R.color.default_inactive_item_color);

        if (spaceItemTextSize == NOT_DEFINED)
            spaceItemTextSize = (int) getResources().getDimension(com.itparsa.circlenavigation.R.dimen.space_item_text_default_size);

        if (spaceItemIconSize == NOT_DEFINED)
            spaceItemIconSize = (int) getResources().getDimension(com.itparsa.circlenavigation.R.dimen.space_item_icon_default_size);

        if (spaceItemIconOnlySize == NOT_DEFINED)
            spaceItemIconOnlySize = (int) getResources().getDimension(com.itparsa.circlenavigation.R.dimen.space_item_icon_only_size);

        if (centreButtonRippleColor == NOT_DEFINED)
            centreButtonRippleColor = ContextCompat.getColor(context, com.itparsa.circlenavigation.R.color.colorBackgroundHighlightWhite);

        if (activeCentreButtonIconColor == NOT_DEFINED)
            activeCentreButtonIconColor = ContextCompat.getColor(context, R.color.space_white);

        if (inActiveCentreButtonIconColor == NOT_DEFINED)
            inActiveCentreButtonIconColor = ContextCompat.getColor(context, com.itparsa.circlenavigation.R.color.default_inactive_item_color);

        /*
         * Set main layout size and color
         */
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = spaceNavigationHeight;
        setBackgroundColor(ContextCompat.getColor(context, R.color.space_transparent));
        setLayoutParams(params);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        /*
         * Restore current item index from savedInstance
         */
        restoreCurrentItem();

        /*
         * Trow exceptions if items size is greater than 4 or lesser than 2
         */
        if (spaceItems.size() < MIN_SPACE_ITEM_SIZE && !isInEditMode()) {
            throw new NullPointerException("Your space item count must be greater than 1 ," +
                    " your current items count isa : " + spaceItems.size());
        }

        if (spaceItems.size() > MAX_SPACE_ITEM_SIZE && !isInEditMode()) {
            throw new IndexOutOfBoundsException("Your items count maximum can be 4," +
                    " your current items count is : " + spaceItems.size());
        }

        /*
         * Get left or right content width
         */
        contentWidth = (width - spaceNavigationHeight) / 2;

        /*
         * Removing all view for not being duplicated
         */
        removeAllViews();

        /*
         * Views initializations and customizing
         */
        initAndAddViewsToMainView();

        /*
         * Redraw main view to make subviews visible
         */
        postRequestLayout();

        /*
         * Retore Translation height
         */

        restoreTranslation();
    }

    //private methods

    /**
     * Views initializations and customizing
     */
    private void initAndAddViewsToMainView() {

        RelativeLayout baseView = new RelativeLayout(context);
        baseView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        RelativeLayout mainContent = new RelativeLayout(context);
        centreBackgroundView = new RelativeLayout(context);

        leftContent = new LinearLayout(context);
        rightContent = new LinearLayout(context);

        centreContent = buildBezierView();

        centreButton = new CentreButton(context);
//        centreButton.setSize(FloatingActionButton.SIZE_NORMAL);
//        centreButton.setUseCompatPadding(false);
        //centreButton.setRippleColor(centreButtonRippleColor);
        if (centerButtonResurceBackground != NOT_DEFINED) {
            centreButton.setFabBackground(centerButtonResurceBackground);

        }

        centreButton.setFabIconResource(centreButtonIcon);
//        centreButton.setImageResource(centreButtonIcon);


        if (isCentreButtonIconColorFilterEnabled || isCentreButtonSelectable)
            centreButton.setFabIconColor(inActiveCentreButtonIconColor);
//            centreButton.getDrawable().setColorFilter(inActiveCentreButtonIconColor, PorterDuff.Mode.SRC_IN);

        centreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spaceOnClickListener != null)
                    spaceOnClickListener.onCentreButtonClick();
                if (isCentreButtonSelectable)
                    updateSpaceItems(-1);
            }
        });
        centreButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (spaceOnLongClickListener != null)
                    spaceOnLongClickListener.onCentreButtonLongClick();

                return true;
            }
        });

        /*
         * Set fab layout params
         */
        RelativeLayout.LayoutParams fabParams = new RelativeLayout.LayoutParams(centreButtonSize, centreButtonSize);
        fabParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        /*
         * Main content size
         */
        RelativeLayout.LayoutParams mainContentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mainContentHeight);
        mainContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /*
         * Centre content size
         */
        RelativeLayout.LayoutParams centreContentParams = new RelativeLayout.LayoutParams(centreContentWight, spaceNavigationHeight);
        centreContentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        centreContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /*
         * Centre Background View content size and position
         */
        RelativeLayout.LayoutParams centreBackgroundViewParams = new RelativeLayout.LayoutParams(centreContentWight, mainContentHeight);
        centreBackgroundViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        centreBackgroundViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /*
         * Left content size
         */
        RelativeLayout.LayoutParams leftContentParams = new RelativeLayout.LayoutParams(contentWidth, mainContentHeight);
        leftContentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftContentParams.addRule(LinearLayout.HORIZONTAL);
        leftContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /*
         * Right content size
         */
        RelativeLayout.LayoutParams rightContentParams = new RelativeLayout.LayoutParams(contentWidth, mainContentHeight);
        rightContentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightContentParams.addRule(LinearLayout.HORIZONTAL);
        rightContentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        /*
         * Adding views background colors
         */
        setBackgroundColors();

        /*
         * Adding view to centreContent
         */
        centreContent.addView(centreButton, fabParams);

        /*
         * Adding views to mainContent
         */
        baseView.addView(centreBackgroundView, centreBackgroundViewParams);
        baseView.addView(leftContent, leftContentParams);
        baseView.addView(rightContent, rightContentParams);


        /*
         * Adding views to mainView
         */

        baseView.addView(centreContent, centreContentParams);
        baseView.addView(mainContent, mainContentParams);

        /*
         * Restore changed icons and texts from savedInstance
         */
        //restoreChangedIconsAndTexts();

        /*
         * Adding current space items to left and right content
         */
        addSpaceItems(leftContent, rightContent);
        addView(baseView);

        updateSpaceItems(-1);
    }

    /**
     * Adding given space items to content
     *
     * @param leftContent  to left content
     * @param rightContent and right content
     */
    private void addSpaceItems(LinearLayout leftContent, LinearLayout rightContent) {

        /*
         * Removing all views for not being duplicated
         */
        if (leftContent.getChildCount() > 0 || rightContent.getChildCount() > 0) {
            leftContent.removeAllViews();
            rightContent.removeAllViews();
        }

        /*
         * Clear spaceItemList and badgeList for not being duplicated
         */
        spaceItemList.clear();
        badgeList.clear();

        /*
         * Getting LayoutInflater to inflate space item view from XML
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < spaceItems.size(); i++) {
            final int index = i;
            int targetWidth;

            if (spaceItems.size() > MIN_SPACE_ITEM_SIZE) {
                targetWidth = contentWidth / 2;
            } else {
                targetWidth = contentWidth;
            }

            RelativeLayout.LayoutParams textAndIconContainerParams = new RelativeLayout.LayoutParams(
                    targetWidth, mainContentHeight);
            RelativeLayout textAndIconContainer;
            if (inflater != null) {
                textAndIconContainer = (RelativeLayout) inflater.inflate(R.layout.circle_item_view, this, false);

                textAndIconContainer.setLayoutParams(textAndIconContainerParams);

                ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                TextView spaceItemText = (TextView) textAndIconContainer.findViewById(R.id.space_text);
                RelativeLayout badgeContainer = (RelativeLayout) textAndIconContainer.findViewById(R.id.badge_container);
                spaceItemIcon.setImageResource(spaceItems.get(i).getItemIcon());
                spaceItemText.setText(spaceItems.get(i).getItemName());
                spaceItemText.setTextSize(TypedValue.COMPLEX_UNIT_PX, spaceItemTextSize);


                /*
                 * Set custom font to space item textView
                 */
                if (isCustomFont)
                    spaceItemText.setTypeface(customFont);

                /*
                 * Hide item icon and show only text
                 */
                if (isTextOnlyMode)
                    Utils.changeViewVisibilityGone(spaceItemIcon);

                /*
                 * Hide item text and change icon size
                 */
                ViewGroup.LayoutParams iconParams = spaceItemIcon.getLayoutParams();
                if (isIconOnlyMode) {
                    iconParams.height = spaceItemIconOnlySize;
                    iconParams.width = spaceItemIconOnlySize;
                    spaceItemIcon.setLayoutParams(iconParams);
                    Utils.changeViewVisibilityGone(spaceItemText);
                } else {
                    iconParams.height = spaceItemIconSize;
                    iconParams.width = spaceItemIconSize;
                    spaceItemIcon.setLayoutParams(iconParams);
                }

                /*
                 * Adding space items to item list for future
                 */
                spaceItemList.add(textAndIconContainer);

                /*
                 * Adding badge items to badge list for future
                 */
                badgeList.add(badgeContainer);

                /*
                 * Adding sub views to left and right sides
                 */
                if (spaceItems.size() == MIN_SPACE_ITEM_SIZE && leftContent.getChildCount() == 1) {
                    rightContent.addView(textAndIconContainer, textAndIconContainerParams);
                } else if (spaceItems.size() > MIN_SPACE_ITEM_SIZE && leftContent.getChildCount() == 2) {
                    rightContent.addView(textAndIconContainer, textAndIconContainerParams);
                } else {
                    leftContent.addView(textAndIconContainer, textAndIconContainerParams);
                }

                /*
                 * Changing current selected item tint
                 */
                if (i == currentSelectedItem) {
                    spaceItemText.setTextColor(activeSpaceItemColor);
                    spaceItemText.setVisibility(VISIBLE);
                    iconParams.height = spaceItemIconOnlySize;
                    iconParams.width = spaceItemIconOnlySize;
                    spaceItemIcon.setLayoutParams(iconParams);
                    Utils.changeImageViewTint(spaceItemIcon, activeSpaceItemColor);
                } else {
                    spaceItemText.setTextColor(inActiveSpaceItemColor);
                    spaceItemText.setVisibility(GONE);
                    iconParams.height = spaceItemIconOnlySize;
                    iconParams.width = spaceItemIconOnlySize;
                    spaceItemIcon.setLayoutParams(iconParams);
                    Utils.changeImageViewTint(spaceItemIcon, inActiveSpaceItemColor);
                }

                textAndIconContainer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateSpaceItems(index);
                    }
                });

                textAndIconContainer.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (spaceOnLongClickListener != null)
                            spaceOnLongClickListener.onItemLongClick(index, spaceItems.get(index).getItemName());
                        return true;
                    }
                });
            }

            /*
             * Restore available badges from saveInstance
             */
            restoreBadges();
        }
    }

    /**
     * Update selected item and change it's and non selected item tint
     *
     * @param selectedIndex item in index
     */
    private void updateSpaceItems(final int selectedIndex) {

        /*
         * return if item already selected
         */
        if (currentSelectedItem == selectedIndex) {
            if (spaceOnClickListener != null && selectedIndex >= 0)
                spaceOnClickListener.onItemReselected(selectedIndex, spaceItems.get(selectedIndex).getItemName());

            return;
        }

        if (isCentreButtonSelectable) {
            /*
             * Selects the centre button as current
             */
            if (selectedIndex == -1) {
                if (centreButton != null) {

                    if (centerButtonResurceBackground == NOT_DEFINED && activeCentreButtonBackgroundColor != NOT_DEFINED) {
//                        centreButton.setBackgroundTintList(ColorStateList.valueOf(activeCentreButtonBackgroundColor));
                        centreButton.setFabColor(activeCentreButtonBackgroundColor);
//                        centreButton.setImageResource(centerBackGroundResource);
                    }

                    centreButton.setFabIconResource(centerButtonSelectedIcon);

                    centreButton.setFabIconColor(activeCentreButtonIconColor);
//                    centreButton.getDrawable().setColorFilter(activeCentreButtonIconColor, PorterDuff.Mode.SRC_IN);

                }
            }

            /*
             * Removes selection from centre button
             */
            if (currentSelectedItem == -1) {
                if (centreButton != null) {

//                    centreButton.setImageResource(centreButtonIcon);
                    centreButton.setFabIconResource(centreButtonIcon);

//                    centreButton.getDrawable().setColorFilter(inActiveCentreButtonIconColor, PorterDuff.Mode.SRC_IN);
                    centreButton.setFabIconColor(inActiveCentreButtonIconColor);
                    if (centerButtonResurceBackground == NOT_DEFINED && activeCentreButtonBackgroundColor != NOT_DEFINED) {
//                        centreButton.setBackgroundTintList(ColorStateList.valueOf(centreButtonColor));
                        centreButton.setFabColor(centreButtonColor);
                    }

                }
            }
        }

        /*
         * Change active and inactive icon and text color
         */
        for (int i = 0; i < spaceItemList.size(); i++) {
            if (i == selectedIndex) {
                RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(selectedIndex);
                ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                TextView spaceItemText = (TextView) textAndIconContainer.findViewById(R.id.space_text);
                spaceItemText.setVisibility(VISIBLE);
                ViewGroup.LayoutParams iconParams = spaceItemIcon.getLayoutParams();
                iconParams.height = spaceItemIconOnlySize;
                iconParams.width = spaceItemIconOnlySize;
                spaceItemIcon.setLayoutParams(iconParams);

                if (selectedIndex >= 0 && spaceItems.get(selectedIndex).getItemSelectedIcon() != 0) {
                    spaceItemIcon.setImageResource(spaceItems.get(i).getItemSelectedIcon());
                }

                if (selectedIndex >= 0 && spaceItems.get(selectedIndex).getItemSelectedColor() != 0) {
                    spaceItemText.setTextColor(spaceItems.get(selectedIndex).getItemSelectedColor());
                    Utils.changeImageViewTint(spaceItemIcon, spaceItems.get(selectedIndex).getItemSelectedColor());
                } else {
                    spaceItemText.setTextColor(activeSpaceItemColor);
                    Utils.changeImageViewTint(spaceItemIcon, activeSpaceItemColor);
                }


//                spaceItemText.setTextColor(activeSpaceItemColor);
//                Utils.changeImageViewTint(spaceItemIcon, activeSpaceItemColor);
            } else if (i == currentSelectedItem) {
                RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(i);
                ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
                TextView spaceItemText = (TextView) textAndIconContainer.findViewById(R.id.space_text);
                ViewGroup.LayoutParams iconParams = spaceItemIcon.getLayoutParams();
                iconParams.height = spaceItemIconOnlySize;
                iconParams.width = spaceItemIconOnlySize;
                spaceItemIcon.setLayoutParams(iconParams);
                spaceItemText.setVisibility(GONE);
                spaceItemIcon.setImageResource(spaceItems.get(i).getItemIcon());
//                if (selectedIndex >= 0 && spaceItems.get(selectedIndex).getItemSelectedColor() != 0) {
//                    spaceItemText.setTextColor(spaceItems.get(selectedIndex).getItemSelectedColor());
//                    Utils.changeImageViewTint(spaceItemIcon, spaceItems.get(selectedIndex).getItemSelectedColor());
//                } else {
                spaceItemText.setTextColor(inActiveSpaceItemColor);
                Utils.changeImageViewTint(spaceItemIcon, inActiveSpaceItemColor);
//                }
            }

        }

        /*
         * Set a listener that gets fired when the selected item changes
         *
         * @param listener a listener for monitoring changes in item selection
         */
        if (spaceOnClickListener != null && selectedIndex >= 0)
            spaceOnClickListener.onItemClick(selectedIndex, spaceItems.get(selectedIndex).getItemName());

        /*
         * Change current selected item index
         */
        currentSelectedItem = selectedIndex;
    }

    /**
     * Set views background colors
     */
    private void setBackgroundColors() {
        rightContent.setBackgroundColor(spaceBackgroundColor);
        centreBackgroundView.setBackgroundColor(spaceBackgroundColor);
        leftContent.setBackgroundColor(spaceBackgroundColor);
    }

    /**
     * Indicate event queue that we have changed the View hierarchy during a layout pass
     */
    private void postRequestLayout() {
        CircleNavigationView.this.getHandler().post(new Runnable() {
            @Override
            public void run() {
                CircleNavigationView.this.requestLayout();
            }
        });
    }

    /**
     * Restore current item index from savedInstance
     */
    private void restoreCurrentItem() {
        Bundle restoredBundle = savedInstanceState;
        if (restoredBundle != null) {
            if (restoredBundle.containsKey(CURRENT_SELECTED_ITEM_BUNDLE_KEY))
                currentSelectedItem = restoredBundle.getInt(CURRENT_SELECTED_ITEM_BUNDLE_KEY, 0);
        }
    }

    /**
     * Restore available badges from saveInstance
     */
    @SuppressWarnings("unchecked")
    private void restoreBadges() {
        Bundle restoredBundle = savedInstanceState;

        if (restoredBundle != null) {
            if (restoredBundle.containsKey(BADGE_FULL_TEXT_KEY)) {
                shouldShowBadgeWithNinePlus = restoredBundle.getBoolean(BADGE_FULL_TEXT_KEY);
            }

            if (restoredBundle.containsKey(BADGES_ITEM_BUNDLE_KEY)) {
                badgeSaveInstanceHashMap = (HashMap<Integer, Object>) savedInstanceState.getSerializable(BADGES_ITEM_BUNDLE_KEY);
                if (badgeSaveInstanceHashMap != null) {
                    for (Integer integer : badgeSaveInstanceHashMap.keySet()) {
                        BadgeHelper.forceShowBadge(
                                badgeList.get(integer),
                                (BadgeItem) badgeSaveInstanceHashMap.get(integer),
                                shouldShowBadgeWithNinePlus);
                    }
                }
            }
        }
    }


    /**
     * Creating bezier view with params
     *
     * @return created bezier view
     */
    private BezierView buildBezierView() {
        BezierView bezierView = new BezierView(context, spaceBackgroundColor);
        bezierView.build(centreContentWight, spaceNavigationHeight - mainContentHeight, isCentrePartLinear);
        return bezierView;
    }

    /**
     * Throw Array Index Out Of Bounds Exception
     *
     * @param itemIndex item index to show on logs
     */
    private void throwArrayIndexOutOfBoundsException(int itemIndex) {
        throw new ArrayIndexOutOfBoundsException("Your item index can't be 0 or greater than space item size," +
                " your items size is " + spaceItems.size() + ", your current index is :" + itemIndex);
    }

    //public methods

    /**
     * Initialization with savedInstanceState to save current selected
     * position and current badges
     *
     * @param savedInstanceState bundle to saveInstance
     */
    public void initWithSaveInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    /**
     * Save badges and current position
     *
     * @param outState bundle to saveInstance
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_SELECTED_ITEM_BUNDLE_KEY, currentSelectedItem);
        outState.putInt(CENTRE_BUTTON_ICON_KEY, centreButtonIcon);
        outState.putInt(SPACE_BACKGROUND_COLOR_KEY, spaceBackgroundColor);
        outState.putBoolean(BADGE_FULL_TEXT_KEY, shouldShowBadgeWithNinePlus);
        outState.putFloat(VISIBILITY, this.getTranslationY());

        if (badgeSaveInstanceHashMap.size() > 0)
            outState.putSerializable(BADGES_ITEM_BUNDLE_KEY, badgeSaveInstanceHashMap);
        if (changedItemAndIconHashMap.size() > 0)
            outState.putSerializable(CHANGED_ICON_AND_TEXT_BUNDLE_KEY, changedItemAndIconHashMap);
    }

    /**
     * Set centre circle button background color
     *
     * @param centreButtonColor target color
     */
    public void setCentreButtonColor(@ColorInt int centreButtonColor) {
        this.centreButtonColor = centreButtonColor;
    }

    /**
     * Set main background color
     *
     * @param spaceBackgroundColor target color
     */
    public void setSpaceBackgroundColor(@ColorInt int spaceBackgroundColor) {
        this.spaceBackgroundColor = spaceBackgroundColor;
    }

    /**
     * Set centre button icon
     *
     * @param centreButtonIcon target icon
     */
    public void setCentreButtonIcon(int centreButtonIcon) {
        this.centreButtonIcon = centreButtonIcon;
    }

    /**
     * Set active centre button color
     *
     * @param activeCentreButtonBackgroundColor color to change
     */
    public void setActiveCentreButtonBackgroundColor(@ColorInt int activeCentreButtonBackgroundColor) {
        this.activeCentreButtonBackgroundColor = activeCentreButtonBackgroundColor;
    }

    /**
     * Set active item text color
     *
     * @param activeSpaceItemColor color to change
     */
    public void setActiveSpaceItemColor(@ColorInt int activeSpaceItemColor) {
        this.activeSpaceItemColor = activeSpaceItemColor;
    }

    /**
     * Set inactive item text color
     *
     * @param inActiveSpaceItemColor color to change
     */
    public void setInActiveSpaceItemColor(@ColorInt int inActiveSpaceItemColor) {
        this.inActiveSpaceItemColor = inActiveSpaceItemColor;
    }

    /**
     * Set item icon size
     *
     * @param spaceItemIconSize target size
     */
    public void setSpaceItemIconSize(int spaceItemIconSize) {
        this.spaceItemIconSize = spaceItemIconSize;
    }

    /**
     * Set item icon size if showIconOnly() called
     *
     * @param spaceItemIconOnlySize target size
     */
    public void setSpaceItemIconSizeInOnlyIconMode(int spaceItemIconOnlySize) {
        this.spaceItemIconOnlySize = spaceItemIconOnlySize;
    }

    /**
     * Set item text size
     *
     * @param spaceItemTextSize target size
     */
    public void setSpaceItemTextSize(int spaceItemTextSize) {
        this.spaceItemTextSize = spaceItemTextSize;
    }

    /**
     * Set centre button pressed state color
     *
     * @param centreButtonRippleColor Target color
     */
    public void setCentreButtonRippleColor(int centreButtonRippleColor) {
        this.centreButtonRippleColor = centreButtonRippleColor;
    }

    /**
     * Show only text in item
     */
    public void showTextOnly() {
        isTextOnlyMode = true;
    }

    /**
     * Show only icon in item
     */
    public void showIconOnly() {
        isIconOnlyMode = true;
    }

    /**
     * Makes centre button selectable
     */
    public void setCentreButtonSelectable(boolean isSelectable) {
        this.isCentreButtonSelectable = isSelectable;
    }

    /**
     * Add space item to navigation
     *
     * @param spaceItem item to add
     */
    public void addCircleItem(CircleItem spaceItem) {
        spaceItems.add(spaceItem);
    }

    /**
     * Change current selected item to centre button
     */
    public void setCentreButtonSelected() {
        if (!isCentreButtonSelectable)
            throw new ArrayIndexOutOfBoundsException("Please be more careful, you must set the centre button selectable");
        else {
            currentSelectedItem = 0;
            updateSpaceItems(-1);
        }
    }

    /**
     * Set space item and centre click
     *
     * @param spaceOnClickListener space click listener
     */
    public void setCircleOnClickListener(CircleOnClickListener spaceOnClickListener) {
        this.spaceOnClickListener = spaceOnClickListener;
    }

    /**
     * Set space item and centre button long click
     *
     * @param spaceOnLongClickListener space long click listener
     */
    public void setSpaceOnLongClickListener(CircleOnLongClickListener spaceOnLongClickListener) {
        this.spaceOnLongClickListener = spaceOnLongClickListener;
    }

    /**
     * Change current selected item to given index
     * Note: -1 represents the centre button
     *
     * @param indexToChange given index
     */
    public void changeCurrentItem(int indexToChange) {
        if (indexToChange < -1 || indexToChange > spaceItems.size())
            throw new ArrayIndexOutOfBoundsException("Please be more careful, we do't have such item : " + indexToChange);
        else {
            updateSpaceItems(indexToChange);
        }
    }

    /**
     * Show badge at index
     *
     * @param itemIndex index
     * @param badgeText badge count text
     */
    public void showBadgeAtIndex(int itemIndex, int badgeText, @ColorInt int badgeColor, int badgeTextSizeSP, @ColorInt int badgeTexColor, Typeface badgeFont) {
        if (itemIndex < 0 || itemIndex > spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
        } else {
            RelativeLayout badgeView = badgeList.get(itemIndex);

            /*
             * Set circle background to badge view
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                badgeView.setBackground(BadgeHelper.makeShapeDrawable(badgeColor));
            }

            BadgeItem badgeItem = new BadgeItem(itemIndex, badgeText, badgeColor, badgeTexColor, badgeTextSizeSP, badgeFont);
            BadgeHelper.showBadge(badgeView, badgeItem, shouldShowBadgeWithNinePlus);
            badgeSaveInstanceHashMap.put(itemIndex, badgeItem);
        }
    }

    public void showBadgeAtIndexWithoutText(int itemIndex, @Size(min = 0) int badgeCircleSizeDP, @ColorInt int badgeColor) {


        if (itemIndex < 0 || itemIndex > spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
        } else {
            RelativeLayout badgeView = badgeList.get(itemIndex);

            /*
             * Set circle background to badge view
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                badgeView.setBackground(BadgeHelper.makeShapeDrawable(badgeColor));
            }

            BadgeItem badgeItem = new BadgeItem(itemIndex, badgeCircleSizeDP, badgeColor);
            BadgeHelper.showBadge(badgeView, badgeItem, shouldShowBadgeWithNinePlus);
            badgeSaveInstanceHashMap.put(itemIndex, badgeItem);
        }

    }

    public void showBadgeAtIndex(int itemIndex, int badgeText, @ColorInt int badgeColor,
                                 int badgeTextSizeSP, @ColorInt int badgeTexColor) {
        showBadgeAtIndex(itemIndex, badgeText, badgeColor, badgeTextSizeSP, badgeTexColor, null);
    }

    /**
     * Restore translation height from saveInstance
     */
    @SuppressWarnings("unchecked")
    private void restoreTranslation() {
        Bundle restoredBundle = savedInstanceState;

        if (restoredBundle != null) {
            if (restoredBundle.containsKey(VISIBILITY)) {
                this.setTranslationY(restoredBundle.getFloat(VISIBILITY));
            }

        }
    }

    /**
     * Hide badge at index
     *
     * @param index badge index
     * @deprecated Use {@link #hideBadgeAtIndex(int index)} instead.
     */
    @Deprecated
    public void hideBudgeAtIndex(final int index) {
        if (badgeList.get(index).getVisibility() == GONE) {
            Log.d(TAG, "Badge at index: " + index + " already hidden");
        } else {
            BadgeHelper.hideBadge(badgeList.get(index));
            badgeSaveInstanceHashMap.remove(index);
        }
    }

    /**
     * Hide badge at index
     *
     * @param index badge index
     */
    public void hideBadgeAtIndex(final int index) {
        if (badgeList.get(index).getVisibility() == GONE) {
            Log.d(TAG, "Badge at index: " + index + " already hidden");
        } else {
            BadgeHelper.hideBadge(badgeList.get(index));
            badgeSaveInstanceHashMap.remove(index);
        }
    }

    /**
     * Hiding all available badges
     *
     * @deprecated Use {@link #hideAllBadges()} instead.
     */
    @Deprecated
    public void hideAllBudges() {
        for (RelativeLayout badge : badgeList) {
            if (badge.getVisibility() == VISIBLE)
                BadgeHelper.hideBadge(badge);
        }
        badgeSaveInstanceHashMap.clear();
    }

    /**
     * Hiding all available badges
     */
    public void hideAllBadges() {
        for (RelativeLayout badge : badgeList) {
            if (badge.getVisibility() == VISIBLE)
                BadgeHelper.hideBadge(badge);
        }
        badgeSaveInstanceHashMap.clear();
    }

    /**
     * Change badge text at index
     *
     * @param badgeIndex target index
     * @param badgeText  badge count text to change
     */
    public void changeBadgeTextAtIndex(int badgeIndex, int badgeText) {
        if (badgeSaveInstanceHashMap.get(badgeIndex) != null &&
                (((BadgeItem) badgeSaveInstanceHashMap.get(badgeIndex)).getIntBadgeText() != badgeText)) {
            BadgeItem currentBadgeItem = (BadgeItem) badgeSaveInstanceHashMap.get(badgeIndex);
            BadgeItem badgeItemForSave = new BadgeItem(badgeIndex, badgeText, currentBadgeItem.getBadgeColor()
                    , currentBadgeItem.getBadgeTextColor(), currentBadgeItem.getBadgeTextSize(), currentBadgeItem.getBadgeFont());
            BadgeHelper.forceShowBadge(
                    badgeList.get(badgeIndex),
                    badgeItemForSave,
                    shouldShowBadgeWithNinePlus);
            badgeSaveInstanceHashMap.put(badgeIndex, badgeItemForSave);
        }
    }

    /**
     * Set custom font for space item textView
     *
     * @param customFont custom font
     */
    public void setFont(Typeface customFont) {
        isCustomFont = true;
        this.customFont = customFont;
    }

    public void setCentreButtonIconColorFilterEnabled(boolean enabled) {
        isCentreButtonIconColorFilterEnabled = enabled;
    }

    /**
     * Change centre button icon if space navigation already set up
     *
     * @param icon Target icon to change
     */
    public void changeCenterButtonIcon(int icon) {
        if (centreButton == null) {
            Log.e(TAG, "You should call setCentreButtonIcon() instead, " +
                    "changeCenterButtonIcon works if space navigation already set up");
        } else {
//            centreButton.setImageResource(icon);
            centreButton.setFabIconResource(icon);
            centreButtonIcon = icon;
        }
    }

    /**
     * Change item icon if space navigation already set up
     *
     * @param itemIndex Target position
     * @param newIcon   Icon to change
     */
    public void changeItemIconAtPosition(int itemIndex, int newIcon) {
        if (itemIndex < 0 || itemIndex > spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
        } else {
            CircleItem spaceItem = spaceItems.get(itemIndex);
            RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(itemIndex);
            ImageView spaceItemIcon = (ImageView) textAndIconContainer.findViewById(R.id.space_icon);
            spaceItemIcon.setImageResource(newIcon);
            spaceItem.setItemIcon(newIcon);
            changedItemAndIconHashMap.put(itemIndex, spaceItem);
        }
    }

    /**
     * Change item text if space navigation already set up
     *
     * @param itemIndex Target position
     * @param newText   Text to change
     */
    public void changeItemTextAtPosition(int itemIndex, String newText) {
        if (itemIndex < 0 || itemIndex > spaceItems.size()) {
            throwArrayIndexOutOfBoundsException(itemIndex);
        } else {
            CircleItem spaceItem = spaceItems.get(itemIndex);
            RelativeLayout textAndIconContainer = (RelativeLayout) spaceItemList.get(itemIndex);
            TextView spaceItemIcon = (TextView) textAndIconContainer.findViewById(R.id.space_text);
            spaceItemIcon.setText(newText);
            spaceItem.setItemName(newText);
            changedItemAndIconHashMap.put(itemIndex, spaceItem);
        }
    }

    /**
     * Change space background color if space view already set up
     *
     * @param color Target color to change
     */
    public void changeSpaceBackgroundColor(@ColorInt int color) {
        if (color == spaceBackgroundColor) {
            Log.d(TAG, "changeSpaceBackgroundColor: color already changed");
            return;
        }

        spaceBackgroundColor = color;
        setBackgroundColors();
        centreContent.changeBackgroundColor(color);
    }


    /**
     * If you want to show full badge text or show 9+
     *
     * @param shouldShowBadgeWithNinePlus false for full text
     */
    public void shouldShowFullBadgeText(boolean shouldShowBadgeWithNinePlus) {
        this.shouldShowBadgeWithNinePlus = shouldShowBadgeWithNinePlus;
    }

    /**
     * set active centre button color
     *
     * @param color target color
     */
    public void setActiveCentreButtonIconColor(@ColorInt int color) {
        activeCentreButtonIconColor = color;
    }

    /**
     * set inactive centre button color
     *
     * @param color target color
     */
    public void setInActiveCentreButtonIconColor(@ColorInt int color) {
        inActiveCentreButtonIconColor = color;
    }


    public void setCenterButtonSelectedIcon(int centerButtonSelectedIcon) {
        this.centerButtonSelectedIcon = centerButtonSelectedIcon;
    }

    public void setCenterButtonResourceBackground(
            @DrawableRes int centerButtonResourceBackground) {
        centerButtonResurceBackground = centerButtonResourceBackground;
    }
}
