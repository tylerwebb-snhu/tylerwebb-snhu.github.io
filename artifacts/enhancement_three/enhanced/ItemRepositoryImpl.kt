package com.horizon.doveinventory.feature_item.data.repository

import com.horizon.doveinventory.feature_item.data.data_source.ItemDao
import com.horizon.doveinventory.feature_item.domain.model.Item
import com.horizon.doveinventory.feature_item.domain.model.ItemFilter
import com.horizon.doveinventory.feature_item.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class ItemRepositoryImpl(
    private val dao: ItemDao
): ItemRepository {

    override fun getItems(): Flow<List<Item>> {
        return dao.getItems()
    }

    /* Translates the Set<ItemFilter> the rest of the app works with into the individual
    *  boolean parameters the ADO query needs. Nothing above this layer has to know the
    *  query is built this way.
    */
    override fun searchItems(query: String, activeFilters: Set<ItemFilter>): Flow<List<Item>> {
        return dao.searchItems(
            query = query,
            noFiltersActive = activeFilters.isEmpty(),
            legendRoomActive = ItemFilter.LEGEND_ROOM in activeFilters,
            generalWarehouseActive = ItemFilter.GENERAL_WAREHOUSE in activeFilters,
            vehicleActive = ItemFilter.VEHICLE in activeFilters,
            dirtyActive = ItemFilter.DIRTY in activeFilters,
            brokenActive = ItemFilter.BROKEN in activeFilters
        )
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