package com.runnerwar.Repositories

import androidx.lifecycle.LiveData
import com.runnerwar.Data.User.UserDao
import com.runnerwar.api.RetrofitInstance
import com.runnerwar.util.Session
import com.runnerwar.Model.*
import retrofit2.Response
import retrofit2.awaitResponse

class UserRepository(private val userDao: UserDao, var loggedUser: String){

     var readAllData: LiveData<User> = userDao.readDataLoggedUser(Session.getIdUsuario())

    //Calls to API

    suspend fun newUser(user: UserForm): Response<RegisterResponse> {
        // Call to API to add new user
        return RetrofitInstance.api.newUser(user).awaitResponse()
    }

    suspend fun login(loginUser: LoginUser): Response<LoginResponse> {
        // Call to API to add new user
        return RetrofitInstance.api.login(loginUser).awaitResponse()
    }

    suspend fun update(user: UserUpdate) : Response<RegisterResponse> {
        return RetrofitInstance.api.updateUser(user).awaitResponse()
    }

    suspend fun changeFaction(faction: FactionForm) : Response<Codi> {
        return RetrofitInstance.api.changeFaction(faction).awaitResponse()
    }

    suspend fun dailyLogin(email: MailForm) : Response<Codi> {
        println("ENTRO EN EL REPO DEL USER")
        return RetrofitInstance.api.dailyLogin(email).awaitResponse()
    }

    suspend fun deleteUser(user: DeleteUser) : Response<Codi>{
        return RetrofitInstance.api.deleteUser(user).awaitResponse()
    }

    suspend fun updatePoints(lu: PointsUpdate): Response<LoginResponse> {
        return RetrofitInstance.api.updatePoints(lu).awaitResponse()
    }

    /*suspend fun updatePoints(lu: PointsUpdate): Response<LoginResponse> {
        return RetrofitInstance.api.updatePoints(lu).awaitResponse()
    }*/

    suspend fun searchUser(searchUser: SearchUser) : Response<SearchResponse>{
        return RetrofitInstance.api.searchUser(searchUser).awaitResponse()
    }

    suspend fun getallusers() : Response<List<UserLeaderboards>>{
        return RetrofitInstance.api.getUsers().awaitResponse()
    }

    suspend fun getfactions() : Response<ListFactions>{
        return RetrofitInstance.api.getFactions().awaitResponse()
    }

    suspend fun addFriend(user: Friendship) : Response<Codi>{
        return RetrofitInstance.api.addFriend(user).awaitResponse()
    }

    suspend fun deleteFriend(user: Friendship) : Response<Codi>{
        return RetrofitInstance.api.deleteFriend(user).awaitResponse()
    }

    suspend fun searchFriend(user: Friendship) : Response<Codi>{
        return RetrofitInstance.api.searchFriend(user).awaitResponse()
    }

    suspend fun addCoins( coins: Coins) : Response<Codi>{
        return RetrofitInstance.api.addCoins(coins).awaitResponse()
    }



    //Calls to Local Data Base

    suspend fun addUser(user: User){
        //Add to local Data Base
        userDao.addUser(user)
    }

    suspend fun getUserLogged() : User {
        return userDao.getUserLogged(Session.getIdUsuario())
    }

    suspend fun updateUser(newUsername: String){
        userDao.updateAccountName(Session.getIdUsuario(), newUsername)
    }

    suspend fun updateFaction(newFaction: String){
        userDao.updateFaction(Session.getIdUsuario(), newFaction)
    }

    suspend fun deleteUserFromLDB(){
        userDao.deleteUser(Session.getIdUsuario())
    }

    suspend fun deleteAllDataFromLDB(){
        userDao.cleanDataBase()
    }

    suspend fun updatePointsLDB(idLoggedUser: String,points: Int){
        userDao.updatePoints(idLoggedUser,points)
    }

    suspend fun updatePoints(points: Int) {
        userDao.updatePoints(Session.getIdUsuario(), points)
    }

    suspend fun addCoinsLDB(coins: Int) {
        userDao.addCoins(Session.getIdUsuario(), coins)
    }
}