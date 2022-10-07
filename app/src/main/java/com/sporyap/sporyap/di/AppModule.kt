package com.sporyap.sporyap.di

import com.sporyap.sporyap.data.ApiNetworkAdapter
import com.sporyap.sporyap.data.AppApi
import com.sporyap.sporyap.data.rp.*
import com.sporyap.sporyap.use_case.sport.SportUseCase
import com.sporyap.sporyap.use_case.sport.SportUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAccountRepository(appApi: AppApi) : AccountRepository{
        return AccountRepository(appApi)
    }

    @Provides
    @Singleton
    fun providesCorporateRepository(appApi: AppApi) : CorporateRepository{
        return CorporateRepository(appApi)
    }

    @Provides
    @Singleton
    fun providesSportRepository(appApi: AppApi) : SportRepository{
        return SportRepository(appApi)
    }

    @Provides
    @Singleton
    fun providesLocationRepository(appApi: AppApi) : LocationRepository{
        return LocationRepository(appApi)
    }

    @Provides
    @Singleton
    fun providesEventRepository(appApi: AppApi) : EventRepository{
        return EventRepository(appApi)
    }

    @Provides
    @Singleton
    fun providesAppApi(): AppApi{
        return ApiNetworkAdapter.appApi()
    }

    @Provides
    @Singleton
    fun provideSportUseCases(sportRepository: SportRepository) : SportUseCases{
        return SportUseCases(sportUseCase = SportUseCase(sportRepository))
    }
}