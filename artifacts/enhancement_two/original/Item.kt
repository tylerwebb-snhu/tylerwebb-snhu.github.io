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
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            itemName
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

class InvalidItemException(message: String): Exception(message)