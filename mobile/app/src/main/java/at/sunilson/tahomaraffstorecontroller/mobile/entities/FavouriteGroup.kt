package at.sunilson.tahomaraffstorecontroller.mobile.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.serialization.Serializable
@Entity
data class FavouriteGroup(@PrimaryKey val id: String)
