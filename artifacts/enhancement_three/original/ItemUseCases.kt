package com.horizon.doveinventory.feature_item.domain.use_case

data class ItemUseCases(
    val getItemsUseCase: GetItemsUseCase,
    val getItemByIDUseCase: GetItemByIDUseCase,
    val deleteItemUseCase: DeleteItemUseCase,
    val addItemUseCase: AddItemUseCase
)