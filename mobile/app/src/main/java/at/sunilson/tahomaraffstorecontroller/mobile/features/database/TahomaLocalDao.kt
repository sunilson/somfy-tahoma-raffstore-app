package at.sunilson.tahomaraffstorecontroller.mobile.features.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import at.sunilson.tahomaraffstorecontroller.mobile.entities.FavouriteGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.FavouriteDevice
import at.sunilson.tahomaraffstorecontroller.mobile.entities.ExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.entities.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface TahomaLocalDao {

    @Delete
    suspend fun deleteFavouriteDevice(favouriteDevice: FavouriteDevice)

    @Query("SELECT * FROM FavouriteDevice")
    fun getFavouriteDevices(): Flow<List<FavouriteDevice>>

    @Query("SELECT * FROM FavouriteDevice WHERE id=:id")
    suspend fun getFavouriteDeviceOnce(id: String): FavouriteDevice?

    @Upsert
    suspend fun upsertFavouriteDevice(favouriteDevice: FavouriteDevice)

    @Query("DELETE FROM FavouriteDevice WHERE id=:id")
    suspend fun deleteFavouriteDevice(id: String)

    @Upsert
    suspend fun upsertFavouriteExecutionActionGroup(favouriteGroup: FavouriteGroup)

    @Query("SELECT * FROM FavouriteGroup WHERE id=:id")
    suspend fun getFavouriteGroup(id: String): FavouriteGroup?

    @Query("SELECT * FROM FavouriteGroup")
    fun getFavouriteGroups(): Flow<List<FavouriteGroup>>

    @Query("DELETE FROM FavouriteGroup WHERE id=:id")
    suspend fun deleteFavouriteExecutionActionGroup(id: String)

}