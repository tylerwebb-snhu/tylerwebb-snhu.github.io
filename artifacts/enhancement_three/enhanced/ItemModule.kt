package com.horizon.doveinventory.di

import android.app.Application
import androidx.room.Room
import com.horizon.doveinventory.feature_item.data.data_source.ItemDatabase
import com.horizon.doveinventory.feature_item.data.repository.ItemRepositoryImpl
import com.horizon.doveinventory.feature_item.domain.repository.ItemRepository
import com.horizon.doveinventory.feature_item.domain.use_case.AddItemUseCase
import com.horizon.doveinventory.feature_item.domain.use_case.DeleteItemUseCase
import com.horizon.doveinventory.feature_item.domain.use_case.GetItemByIDUseCase
import com.horizon.doveinventory.feature_item.domain.use_case.GetItemsUseCase
import com.horizon.doveinventory.feature_item.domain.use_case.ItemUseCases
import com.horizon.doveinventory.feature_item.domain.use_case.SearchItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ItemModule {

    @Provides
    @Singleton
    fun provideItemDatabase(app: Application): ItemDatabase {
        return Room.databaseBuilder(
            app,
            ItemDatabase::class.java,
            ItemDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideItemRepository(db: ItemDatabase): ItemRepository {
        return ItemRepositoryImpl(db.itemDao)
    }

    @Provides
    @Singleton
    fun provideItemUseCases(repository: ItemRepository): ItemUseCases {
        return ItemUseCases(
            getItemsUseCase = GetItemsUseCase(repository),
            getItemByIDUseCase = GetItemByIDUseCase(repository),
            searchItemsUseCase = SearchItemsUseCase(repository),
            addItemUseCase = AddItemUseCase(repository),
            deleteItemUseCase = DeleteItemUseCase(repository)
        )
    }
}