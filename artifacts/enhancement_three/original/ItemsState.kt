package com.horizon.doveinventory.feature_item.presentation.item_list

import com.horizon.doveinventory.feature_item.domain.model.Item

data class ItemsState(
    val items: List<Item> = emptyList<Item>()
)
