package com.saizad.mvvm.components.form.fields;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sa.easyandroidform.StringUtils;
import com.sa.easyandroidform.fields.NonEmptyStringField;
import com.saizad.mvvm.components.form.FieldValidatorKt;

import javax.annotation.Nonnull;

import io.reactivex.exceptions.CompositeException;


public class NameField extends NonEmptyStringField {

    public NameField(@Nonnull String fieldId, @NonNull String firstName, @NonNull String lastName) {
        this(fieldId, firstName + " " + lastName);
    }

    public NameField(@NonNull String fieldId, @Nullable String ogField) {
        this(fieldId, ogField, true);
    }

    public NameField(@NonNull String fieldId, @Nullable String ogField, boolean isMandatory) {
        super(fieldId, ogField, isMandatory);
    }

    public NameField(@Nonnull String fieldId) {
        super(fieldId, null, true);
    }

    @Nullable
    public String getFirstName() {
        return getName(0);
    }

    @Nullable
    public String getLastName() {
        return getName(1);
    }

    @Nullable
    private String getName(int index) {
        if (isValid()) {
            final String[] split = StringUtils.stripTrailingLeadingNewLines(getField()).split(" ");
            return split[index];
        }
        return null;
    }

    @Override
    public void validate() throws CompositeException {
        super.validate();
        FieldValidatorKt.validateName(getField());
    }
}
