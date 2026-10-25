package com.horizon.doveinventory.feature_item.presentation.item_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.doveinventory.feature_item.domain.use_case.ItemUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class ItemListViewModel @Inject constructor(
    private val itemUseCases: ItemUseCases
): ViewModel() {

    private val _state = mutableStateOf(ItemsState())
    val state: State<ItemsState> = _state

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _items = MutableStateFlow(state.value.items)
    val items = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_items) { text, items ->
            if (text.isBlank()) {
                items
            } else {
                items.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000),
            _items.value
        )

    private var getItemsJob: Job? = null

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    fun clearSearch() {
        _searchText.value = ""
    }

    init {
        getItems()
    }

    /*
    * Cancels any ongoing job to retrieve items, starts a new job to get items through
    * the get items use case. After the items are retrieved, the ItemsState is updated
    * to include a list of the retrieved items.
    * TODO: Once inventory feature is complete, this needs to only pull items that match
    *       the area of the warehouse being counted.
    */
    fun getItems() {
        getItemsJob?.cancel()
        getItemsJob = itemUseCases.getItemsUseCase()
            .onEach { items ->
                _state.value = state.value.copy(
                    items = items
                )
                _items.value = items
            }
            .launchIn(viewModelScope)
    }
}