package com.resqnet.di

import android.content.Context
import com.resqnet.data.database.ResQNetDatabase
import com.resqnet.data.database.MessageDao
import com.resqnet.data.database.DeviceDao
import com.resqnet.data.repository.MessageRepository
import com.resqnet.data.repository.DeviceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideResQNetDatabase(@ApplicationContext context: Context): ResQNetDatabase {
        return ResQNetDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideMessageDao(database: ResQNetDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    @Singleton
    fun provideDeviceDao(database: ResQNetDatabase): DeviceDao {
        return database.deviceDao()
    }

    @Provides
    @Singleton
    fun provideMessageRepository(messageDao: MessageDao): MessageRepository {
        return MessageRepository(messageDao)
    }

    @Provides
    @Singleton
    fun provideDeviceRepository(deviceDao: DeviceDao): DeviceRepository {
        return DeviceRepository(deviceDao)
    }
}
