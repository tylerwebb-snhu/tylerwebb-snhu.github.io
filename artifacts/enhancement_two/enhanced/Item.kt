package com.horizon.doveinventory.feature_item.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey val itemID: Int? = null,
    val itemName: String,
    val itemCount: Int,
    val itemRefNumber: String,
    val isUserCreated: Boolean,
    val isLegendRoomItem: Boolean,
    val isGeneralWarehouseItem: Boolean,
    val isDirtyItem: Boolean,
    val isBrokenItem: Boolean,
    val isVehicleItem: Boolean
) {
    fun doesMatchSearchQuery(query: String, activeFilters: Set<ItemFilter> = emptySet()): Boolean {
        // Now checks both the name and the reference number, not just the name.
        val matchingCombinations = listOf(
            itemName,
            itemRefNumber
        )

        val matchesText = matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }

        // No filters selected means no category narrowing, every item passes this check
        val matchesFilters = activeFilters.isEmpty() || activeFilters.any { matchesFilter(it) }

        return matchesText && matchesFilters
    }

    // Maps each filter to the flag it represents. Kept separate from doesMatchSearchQuery
    // so the matching logic doesn't turn into one long if/else.
    private fun matchesFilter(filter: ItemFilter): Boolean {
        return when (filter) {
            ItemFilter.LEGEND_ROOM -> isLegendRoomItem
            ItemFilter.GENERAL_WAREHOUSE -> isGeneralWarehouseItem
            ItemFilter.VEHICLE -> isVehicleItem
            ItemFilter.DIRTY -> isDirtyItem
            ItemFilter.BROKEN -> isBrokenItem
        }
    }
}

class InvalidItemException(message: String): Exception(message)