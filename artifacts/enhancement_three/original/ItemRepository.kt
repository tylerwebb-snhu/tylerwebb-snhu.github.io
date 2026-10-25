package com.horizon.doveinventory.feature_item.domain.repository

import com.horizon.doveinventory.feature_item.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    fun getItems(): Flow<List<Item>>

    suspend fun getItemByID(itemID: Int): Item?

    suspend fun upsertItem(item: Item)

    suspend fun deleteItem(item: Item)
}