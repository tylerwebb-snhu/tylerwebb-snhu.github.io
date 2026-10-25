package com.horizon.doveinventory.feature_item.domain.use_case

import com.horizon.doveinventory.feature_item.domain.model.Item
import com.horizon.doveinventory.feature_item.domain.model.ItemFilter
import com.horizon.doveinventory.feature_item.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class SearchItemsUseCase(
    private val repository: ItemRepository
) {

    operator fun invoke(query: String, activeFilters: Set<ItemFilter>): Flow<List<Item>> {
        return repository.searchItems(query, activeFilters)
    }
}