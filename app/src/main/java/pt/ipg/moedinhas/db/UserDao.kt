package pt.ipg.moedinhas.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): User?
    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): User?
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>





    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(user: User)
}

