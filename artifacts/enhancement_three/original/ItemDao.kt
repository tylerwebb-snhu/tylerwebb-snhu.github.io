package com.horizon.doveinventory.feature_item.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.horizon.doveinventory.feature_item.domain.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE itemID = :itemId")
    suspend fun getItemByID(itemId: Int): Item?

    @Upsert
    suspend fun upsertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)
}