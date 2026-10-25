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

    /*
    * Filters at the database level instead of pulling every row and filtering in Kotlin.
    * Each category flag has an "active" parameter. If that filter is not selected, the clause
    * is skipped. When noFiltersActive = true, equal to when no category is selected) every row
    * flows through.
    */
    @Query("""
        SELECT * FROM item
        WHERE (itemName LIKE '%' || :query || '%' OR itemRefNumber LIKE '%' || :query || '%')
        AND (
            :noFiltersActive = 1
            OR (:legendRoomActive = 1 AND isLegendRoomItem = 1)
            OR (:generalWarehouseActive = 1 AND isGeneralWarehouseItem = 1)
            OR (:vehicleActive = 1 AND isVehicleItem = 1)
            OR (:dirtyActive = 1 AND isDirtyItem = 1)
            OR (:brokenActive = 1 AND isBrokenItem = 1)
    )
    ORDER BY itemName COLLATE NOCASE
    """)
    fun searchItems(
        query: String,
        noFiltersActive: Boolean,
        legendRoomActive: Boolean,
        generalWarehouseActive: Boolean,
        vehicleActive: Boolean,
        dirtyActive: Boolean,
        brokenActive: Boolean
    ): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE itemID = :itemId")
    suspend fun getItemByID(itemId: Int): Item?

    @Upsert
    suspend fun upsertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)
}