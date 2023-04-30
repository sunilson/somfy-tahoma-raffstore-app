package at.sunilson.tahomaraffstorecontroller.mobile.features.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import at.sunilson.tahomaraffstorecontroller.mobile.features.groups.data.models.FavouriteGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.Execution
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.data.models.LocalExecutionActionGroup
import at.sunilson.tahomaraffstorecontroller.mobile.features.localapi.domain.entities.Device
import at.sunilson.tahomaraffstorecontroller.mobile.features.raffstores.data.models.FavouriteDevice
import kotlinx.coroutines.flow.Flow

@Dao
interface TahomaLocalDao {

    @Upsert
    suspend fun upsertDevice(device: Device)

    @Delete
    suspend fun deleteDevice(device: Device)

    @Delete
    suspend fun deleteDevices(devices: List<Device>)

    @Delete
    suspend fun deleteFavouriteDevice(favouriteDevice: FavouriteDevice)

    @Upsert
    suspend fun upsertDevices(devices: List<Device>)

    @Query("SELECT * FROM Device")
    fun getAllDevices(): Flow<List<Device>>

    @Query("SELECT * FROM Device")
    suspend fun getAllDevicesOnce(): List<Device>

    @Query("SELECT * FROM FavouriteDevice")
    fun getFavouriteDevices(): Flow<List<FavouriteDevice>>

    @Query("SELECT * FROM Device WHERE id=:id")
    suspend fun getDeviceOnce(id: String): Device

    @Update
    suspend fun updateDevice(device: Device)

    @Query("UPDATE Device SET isMoving=:isMoving WHERE id=:id")
    suspend fun setDeviceIsMoving(id: String, isMoving: Boolean)

    @Query("SELECT * FROM FavouriteDevice WHERE id=:id")
    suspend fun getFavouriteDeviceOnce(id: String): FavouriteDevice?

    @Upsert
    suspend fun upsertFavouriteDevice(favouriteDevice: FavouriteDevice)

    @Query("DELETE FROM FavouriteDevice WHERE id=:id")
    suspend fun deleteFavouriteDevice(id: String)

    @Upsert
    suspend fun upsertExecution(device: Execution)

    @Upsert
    suspend fun upsertExecutions(devices: List<Execution>)

    @Query("DELETE FROM Execution WHERE id=:id")
    suspend fun deleteExecution(id: String)

    @Query("DELETE FROM Execution")
    suspend fun deleteAllExecutions()

    @Query("SELECT * FROM Execution")
    fun getAllExecutions(): Flow<List<Execution>>

    @Query("SELECT * FROM Execution")
    suspend fun getAllExecutionsOnce(): List<Execution>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExecutionActionGroup(executionActionGroup: LocalExecutionActionGroup)

    @Upsert
    suspend fun upsertFavouriteExecutionActionGroup(favouriteGroup: FavouriteGroup)

    @Query("SELECT * FROM FavouriteGroup WHERE id=:id")
    suspend fun getFavouriteGroup(id: String): FavouriteGroup?

    @Query("SELECT * FROM FavouriteGroup")
    fun getFavouriteGroups(): Flow<List<FavouriteGroup>>

    @Query("DELETE FROM FavouriteGroup WHERE id=:id")
    suspend fun deleteFavouriteExecutionActionGroup(id: String)

    @Query("SELECT * FROM LocalExecutionActionGroup WHERE id=:id")
    suspend fun getExecutionActionGroupOnce(id: String): LocalExecutionActionGroup?

    @Query("SELECT * FROM LocalExecutionActionGroup")
    fun getAllExecutionActionGroups(): Flow<List<LocalExecutionActionGroup>>

    @Query("SELECT * FROM LocalExecutionActionGroup")
    suspend fun getAllExecutionActionGroupsOnce(): List<LocalExecutionActionGroup>

    @Query("DELETE FROM LocalExecutionActionGroup WHERE id=:id")
    suspend fun deleteExecutionActionGroup(id: String)
}