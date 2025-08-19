package com.tontinepro.data.local.dao

import androidx.room.*
import com.tontinepro.data.local.entities.BulleEntity

@Dao
interface BulleDao {

    @Query("SELECT * FROM bulles WHERE id = :id")
    suspend fun getBulleById(id: String): BulleEntity?

    @Query("SELECT b.* FROM bulles b INNER JOIN bulle_members bm ON b.id = bm.bulleId WHERE bm.userId = :userId")
    suspend fun getUserBulle(userId: String): BulleEntity?

    @Query("SELECT * FROM bulles")
    suspend fun getAllBulles(): List<BulleEntity>

    @Query("SELECT * FROM bulles WHERE formule = :formule")
    suspend fun getBullesByFormule(formule: String): List<BulleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBulle(bulle: BulleEntity)

    @Update
    suspend fun updateBulle(bulle: BulleEntity)

    @Delete
    suspend fun deleteBulle(bulle: BulleEntity)

    @Query("DELETE FROM bulles")
    suspend fun clearAll()
}