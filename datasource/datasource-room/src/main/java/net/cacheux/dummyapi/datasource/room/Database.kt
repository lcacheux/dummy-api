package net.cacheux.dummyapi.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import net.cacheux.dummyapi.datasource.room.entity.RoomDetailedUser
import net.cacheux.dummyapi.datasource.room.entity.RoomUser

@Database(entities = [RoomUser::class, RoomDetailedUser::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun detailedUserDao(): DetailedUserDao
}