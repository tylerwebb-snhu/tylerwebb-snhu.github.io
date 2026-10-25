package com.horizon.doveinventory.feature_item.presentation.item_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.horizon.doveinventory.R
import com.horizon.doveinventory.core.presentation.bottomnav.DefaultBottomNavBar
import com.horizon.doveinventory.core.util.Screen
import com.horizon.doveinventory.feature_item.domain.model.ItemFilter
import com.horizon.doveinventory.feature_item.presentation.item_list.components.ItemListItem

@Composable
fun ItemListScreen(
    navController: NavController,
    viewModel: ItemListViewModel = hiltViewModel()
) {
    ItemListContent(
        navController,
        viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListContent(
    navController: NavController,
    viewModel: ItemListViewModel
) {

    val searchText by viewModel.searchText.collectAsState()
    val items by viewModel.items.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val activeFilters by viewModel.activeFilters.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Column {
                Surface(
                    color = MaterialTheme.colorScheme.primary
                ) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = searchText,
                                onQueryChange = viewModel::onSearchTextChange,
                                onSearch = viewModel::onSearchTextChange,
                                expanded = isSearching,
                                onExpandedChange = { viewModel.onToggleSearch() },
                                placeholder = { Text(
                                    text = stringResource(R.string.search)
                                ) },
                                leadingIcon = { Icon(
                                    Icons.Outlined.Search,
                                    contentDescription = null
                                ) },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            viewModel.clearSearch()
                                        },
                                        content = {
                                            Icon(
                                                Icons.Outlined.Clear,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        expanded = false,
                        onExpandedChange = {},
                        content = {}
                    )

                }

                // Lets the user narrow results by category alongside the text search above
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ItemFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = filter in activeFilters,
                            onClick = { viewModel.onToggleFilter(filter) },
                            label = { Text(text = filterLabel(filter)) }
                        )
                    }
                }
            }
        },
        bottomBar = {
            DefaultBottomNavBar(navController, 1)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onClick = {
                    navController.navigate(Screen.AddItemScreen.route)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_item)
                    )
                    Text(
                        text = stringResource(R.string.add_item)
                    )
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                if (isSearching) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(items) { item ->
                            ItemListItem(
                                item = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        navController.navigate(
                                            Screen.ItemDetailsScreen.route +
                                                    "?itemID=${item.itemID}"
                                        )
                                    }
                            )
                        }
                    }
                }
            }
        }
    )
}

// Maps each filter to its chip label. Kept as its own function so ItemListContent doesn't
// have to embed a when expression.
@Composable
private fun filterLabel(filter: ItemFilter): String {
    return when (filter) {
        ItemFilter.LEGEND_ROOM -> stringResource(R.string.filter_legend_room)
        ItemFilter.GENERAL_WAREHOUSE -> stringResource(R.string.filter_general_warehouse)
        ItemFilter.VEHICLE -> stringResource(R.string.filter_vehicle)
        ItemFilter.DIRTY -> stringResource(R.string.filter_dirty)
        ItemFilter.BROKEN -> stringResource(R.string.filter_broken)
    }
}