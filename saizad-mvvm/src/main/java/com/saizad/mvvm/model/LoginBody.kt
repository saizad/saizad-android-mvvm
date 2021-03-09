package com.saizad.mvvm.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.sa.easyandroidform.fields.EmailField
import com.sa.easyandroidform.fields.MandatoryStringField
import com.sa.easyandroidform.fields.PasswordField
import com.sa.easyandroidform.form.FormModel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class LoginBody(
    @SerializedName("password") val password: String?,
    @SerializedName("username") var username: String?,
    @SerializedName("email") var email: String?
) : Parcelable {

    class UsernameForm(username: String? = null) : FormModel<LoginBody>(
        ArrayList(
            listOf(
                MandatoryStringField("username", username),
                PasswordField("password")
            )
        )
    ) {
        val passwordField: PasswordField = requiredFindField("password")
        private val usernameField: MandatoryStringField = requiredFindField("username")

        override fun buildForm(): LoginBody {
            return LoginBody(
                passwordField.requiredField().trim(),
                usernameField.requiredField().trim(),
                null
            )
        }

    }

    class EmailLoginForm(email: String? = null) : FormModel<LoginBody>(
        ArrayList(listOf(
            EmailField("email", email),
            PasswordField("password"))
        )
    ) {
        val passwordField: PasswordField = requiredFindField("password")
        val emailField: EmailField = requiredFindField("email")

        override fun buildForm(): LoginBody {
            return LoginBody(
                passwordField.requiredField().trim(),
                null,
                emailField.requiredField().trim()
            )
        }
    }
}