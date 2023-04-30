package at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device

@kotlinx.serialization.Serializable
@Entity(foreignKeys = [ForeignKey(Device::class, arrayOf("id"), arrayOf("id"), ForeignKey.CASCADE)])
data class FavouriteDevice(@PrimaryKey val id: String)
