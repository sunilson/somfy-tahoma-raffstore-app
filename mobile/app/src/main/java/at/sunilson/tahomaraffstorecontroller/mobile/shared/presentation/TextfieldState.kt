package at.sunilson.tahomaraffstorecontroller.mobile.shared.presentation

import androidx.annotation.StringRes
import java.io.Serializable

data class TextFieldState(
    val input: String,
    @StringRes val error: Int? = null
) : Serializable {
    companion object {
        fun empty() = TextFieldState("", null)
    }
}
