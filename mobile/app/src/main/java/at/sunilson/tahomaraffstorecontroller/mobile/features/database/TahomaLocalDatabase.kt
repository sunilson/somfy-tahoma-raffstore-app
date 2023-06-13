package at.sunilson.tahomaraffstorecontroller.mobile.features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import at.sunilson.tahomaraffstorecontroller.mobile.entities.FavouriteGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.FavouriteDevice
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule

@Database(
    entities = [
        FavouriteGroup::class,
        FavouriteDevice::class
    ],
    version = 7
)
@TypeConverters(Converters::class)
abstract class TahomaLocalDatabase : RoomDatabase() {
    abstract fun dao(): TahomaLocalDao
}