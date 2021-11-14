package net.cacheux.dummyapi.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.cacheux.dummyapi.datasource.room.entity.RoomUser

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY `index` LIMIT :limit OFFSET :page * :limit")
    fun getUserList(page: Int = 0, limit: Int = 10): Flow<List<RoomUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUsers(users: List<RoomUser>)

    @Query("SELECT COUNT(*) FROM user")
    fun getUserCount(): Int
}