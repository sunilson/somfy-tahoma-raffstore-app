package at.sunilson.tahomaraffstorecontroller.mobile.features.widget.domain.entities

import com.google.errorprone.annotations.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class FavouriteWidgetEntry(val id: Long, val label: String, val deeplink: String): java.io.Serializable