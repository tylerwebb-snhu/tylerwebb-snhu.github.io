package com.horizon.doveinventory.feature_item.data.repository

import com.horizon.doveinventory.feature_item.data.data_source.ItemDao
import com.horizon.doveinventory.feature_item.domain.model.Item
import com.horizon.doveinventory.feature_item.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class ItemRepositoryImpl(
    private val dao: ItemDao
): ItemRepository {

    override fun getItems(): Flow<List<Item>> {
        return dao.getItems()
    }

    override suspend fun getItemByID(itemID: Int): Item? {
        return dao.getItemByID(itemID)
    }

    override suspend fun upsertItem(item: Item) {
        dao.upsertItem(item)
    }

    override suspend fun deleteItem(item: Item) {
        dao.deleteItem(item)
    }
}