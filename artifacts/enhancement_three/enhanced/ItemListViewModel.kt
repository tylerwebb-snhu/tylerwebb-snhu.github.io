package com.horizon.doveinventory.feature_item.presentation.item_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.doveinventory.feature_item.domain.model.ItemFilter
import com.horizon.doveinventory.feature_item.domain.use_case.ItemUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ItemListViewModel @Inject constructor(
    private val itemUseCases: ItemUseCases
): ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // Tracks which category filters (legend room, general warehouse, etc.) are currently active
    private val _activeFilters = MutableStateFlow<Set<ItemFilter>>(emptySet())
    val activeFilters = _activeFilters.asStateFlow()

    /* The list comes directly from a database query instead of fetching every row and
    *  filtering in Kotlin like before. Any change to the search text or active filters
    *  triggers flatMapLatest to switch to a fresh query via the searchItemsUseCase.
    */
    val items = _searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_activeFilters) { text, filters -> text to filters }
        .flatMapLatest { (text, filters) ->
            itemUseCases.searchItemsUseCase(text, filters)
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000),
            emptyList()
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    // Adds the filter if it isn't active yet, removes it if it already is.
    fun onToggleFilter(filter: ItemFilter) {
        _activeFilters.update { current ->
            if (filter in current) current - filter else current + filter
        }
    }

    fun clearSearch() {
        _searchText.value = ""
    }
}