package com.saizad.mvvm.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.saizad.mvvm.R;

public class CustomSelectorConstraintLayout extends ConstraintLayout {

    private static final int[] MANDATORY = {R.attr.mandatory};
    private static final int[] ERROR = {R.attr.error};
    private static final int[] EDITED = {R.attr.edited};
    private boolean isMandatory;
    private boolean isError;
    private boolean isEdited;

    public CustomSelectorConstraintLayout(Context context) {
        super(context);
    }

    public CustomSelectorConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSelectorConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if (isError) {
            mergeDrawableStates(drawableState, ERROR);
        } else if (isEdited) {
            mergeDrawableStates(drawableState, EDITED);
        } else if (isMandatory) {
            mergeDrawableStates(drawableState, MANDATORY);
        }
        return drawableState;
    }

    public void setMandatory(boolean mandatory) {
        neutral();
        isMandatory = mandatory;
        refreshDrawableState();
    }

    public void setError(boolean error) {
        neutral();
        isError = error;
        refreshDrawableState();
    }

    public void setEdited(boolean edited) {
        neutral();
        isEdited = edited;
        refreshDrawableState();
    }

    public void neutral() {
        isMandatory = false;
        isError = false;
        isEdited = false;
        refreshDrawableState();
    }
}
