package com.runnerwar.Data.User

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.runnerwar.Model.User

// Contains the methods used for accessing the database
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_data WHERE _id = :idLoggedUser")
    suspend fun getUserLogged( idLoggedUser: String ): User

    @Query("SELECT * FROM user_data WHERE _id = :idLoggedUser LIMIT 1")
    fun readDataLoggedUser( idLoggedUser: String ): LiveData<User>

    @Query("UPDATE user_data SET accountname = :newUserName WHERE _id = :idLoggedUser ")
    suspend fun updateAccountName(idLoggedUser: String, newUserName: String)

    @Query("UPDATE user_data SET points = points + :newPoints WHERE _id = :idLoggedUser ")
    suspend fun updatePoints(idLoggedUser: String, newPoints: Int)

    @Query("UPDATE user_data SET coins = coins + :newCoins WHERE _id = :idLoggedUser ")
    suspend fun addCoins(idLoggedUser: String, newCoins: Int)

    @Query("UPDATE user_data SET faction = :newFaction WHERE _id = :idLoggedUser ")
    suspend fun updateFaction(idLoggedUser: String, newFaction: String)

    @Query("DELETE FROM user_data WHERE _id = :idLoggedUser")
    suspend fun deleteUser(idLoggedUser: String)

    @Query("DELETE FROM user_data")
    suspend fun cleanDataBase()
    


}