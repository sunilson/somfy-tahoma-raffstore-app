package at.sunilson.tahomaraffstorecontroller.mobile.features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.data.models.FavouriteGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.data.models.FavouriteDevice

@Database(
    entities = [
        Device::class,
        Execution::class,
        LocalExecutionActionGroup::class,
        FavouriteGroup::class,
        FavouriteDevice::class
    ],
    version = 6
)
@TypeConverters(Converters::class)
abstract class TahomaLocalDatabase : RoomDatabase() {
    abstract fun dao(): TahomaLocalDao
}