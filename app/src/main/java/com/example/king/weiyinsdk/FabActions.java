package com.example.king.weiyinsdk;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class FabActions {

    Context context;

    RelativeLayout frameLayout;

    ImageView mainImageButton;

    RotateAnimation rotateOpenAnimation;

    RotateAnimation rotateCloseAnimation;

    ArrayList<View> promotedActions;

    ObjectAnimator objectAnimator[];

    private int px;

    private static final int ANIMATION_TIME = 66;

    private boolean isMenuOpened;

    public void setup(Context activityContext, RelativeLayout layout) {
        context = activityContext;
        promotedActions = new ArrayList<>();
        frameLayout = layout;
        px = dp2px(activityContext, 56) + 16;
        openRotation();
        closeRotation();
    }


    public void closeOrOpen() {
        if (isMenuOpened) {
            closePromotedActions().start();
            isMenuOpened = false;
        } else {
            isMenuOpened = true;
            openPromotedActions().start();
        }
    }

    public ImageView addMainItem(int resId) {
        ImageView imageView = (ImageView) View.inflate(context, R.layout.fab_main, null);
        setMargin(imageView, getScreenWidth(context) - dp2px(context, 64));

        imageView.setImageResource(resId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isMenuOpened) {
                    closePromotedActions().start();
                    isMenuOpened = false;
                } else {
                    isMenuOpened = true;
                    openPromotedActions().start();
                }
            }
        });

        frameLayout.addView(imageView);

        mainImageButton = imageView;

        return imageView;
    }


    public void addItem(int resId, String text, View.OnClickListener onClickListener) {
        View view = View.inflate(context, R.layout.fab_item, null);
        setMargin(view, getScreenWidth(context) - dp2px(context, 120));

        TextView textView = (TextView) view.findViewById(R.id.fab_item_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.fab_item_icon);

        imageView.setImageResource(resId);
        textView.setText(text);
        view.setOnClickListener(onClickListener);

        promotedActions.add(view);

        frameLayout.addView(view);
    }

    public AnimatorSet closePromotedActions() {

        if (objectAnimator == null) {
            objectAnimatorSetup();
        }

        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setCloseAnimation(promotedActions.get(i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mainImageButton.startAnimation(rotateCloseAnimation);
                mainImageButton.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainImageButton.setClickable(true);
                hidePromotedActions();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        return animation;
    }

    public AnimatorSet openPromotedActions() {

        if (objectAnimator == null) {
            objectAnimatorSetup();
        }


        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setOpenAnimation(promotedActions.get(i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mainImageButton.startAnimation(rotateOpenAnimation);
                mainImageButton.setClickable(false);
                showPromotedActions();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });


        return animation;
    }

    private void objectAnimatorSetup() {

        objectAnimator = new ObjectAnimator[promotedActions.size()];
    }

    private ObjectAnimator setCloseAnimation(View promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, -px * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        } else {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X, -px * (promotedActions.size() - position), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    private ObjectAnimator setOpenAnimation(View promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, 0f, -px * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {
            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X, 0f, -px * (promotedActions.size() - position));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    private void setMargin(View view, int left) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.leftMargin = left;
        layoutParams.topMargin = getScreenHeight(context) - dp2px(context, 144);
        view.setLayoutParams(layoutParams);
    }

    private void hidePromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.GONE);
        }
    }

    private void showPromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void openRotation() {

        rotateOpenAnimation = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateOpenAnimation.setFillAfter(true);
        rotateOpenAnimation.setFillEnabled(true);
        rotateOpenAnimation.setDuration(ANIMATION_TIME);
    }

    private void closeRotation() {

        rotateCloseAnimation = new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateCloseAnimation.setFillAfter(true);
        rotateCloseAnimation.setFillEnabled(true);
        rotateCloseAnimation.setDuration(ANIMATION_TIME);
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        @SuppressWarnings("deprecation")
        int mResWidth = (int) (wm.getDefaultDisplay().getWidth());

        return mResWidth;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        @SuppressWarnings("deprecation")
        int mResHeight = (int) (wm.getDefaultDisplay().getHeight());
        return mResHeight;

    }
}
