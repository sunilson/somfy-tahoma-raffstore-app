package at.sunilson.tahomaraffstorecontroller.mobile.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@kotlinx.serialization.Serializable
@Entity
data class FavouriteDevice(@PrimaryKey val id: String)
