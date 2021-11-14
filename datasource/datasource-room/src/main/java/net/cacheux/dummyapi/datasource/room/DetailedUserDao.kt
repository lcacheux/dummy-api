package net.cacheux.dummyapi.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.cacheux.dummyapi.datasource.room.entity.RoomDetailedUser

@Dao
interface DetailedUserDao {
    @Query("SELECT * FROM detailed_user WHERE id = :id")
    fun getDetailedUser(id: String): Flow<RoomDetailedUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDetailedUser(user: RoomDetailedUser)
}