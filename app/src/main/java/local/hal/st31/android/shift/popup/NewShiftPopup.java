package local.hal.st31.android.shift.popup;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import local.hal.st31.android.shift.R;
import razerdp.basepopup.BasePopupWindow;

public class NewShiftPopup extends BasePopupWindow {
    public NewShiftPopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_shift_add);
    }

    @Override
    protected Animator onCreateShowAnimator() {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(getDisplayAnimateView(), View.TRANSLATION_Y, getHeight() * 0.75f, 0);
        showAnimator.setDuration(1000);
        showAnimator.setInterpolator(new OvershootInterpolator(6));
        return showAnimator;
    }

    @Override
    protected Animator onCreateDismissAnimator() {
        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(getDisplayAnimateView(), View.TRANSLATION_Y, 0, getHeight() * 0.75f);
        showAnimator.setDuration(1000);
        showAnimator.setInterpolator(new OvershootInterpolator(-6));
        return showAnimator;
    }
}
