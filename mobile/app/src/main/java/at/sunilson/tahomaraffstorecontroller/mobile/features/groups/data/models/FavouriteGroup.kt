package at.sunilson.tahomaraffstorecontroller.mobile.features.groups.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup

@kotlinx.serialization.Serializable
@Entity(foreignKeys = [ForeignKey(LocalExecutionActionGroup::class, arrayOf("id"), arrayOf("id"), ForeignKey.CASCADE)])
data class FavouriteGroup(@PrimaryKey val id: String)
