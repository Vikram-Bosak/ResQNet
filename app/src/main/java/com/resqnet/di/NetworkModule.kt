package com.resqnet.di

import android.content.Context
import com.resqnet.network.bluetooth.BluetoothManager
import com.resqnet.network.wifidirect.WiFiDirectManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBluetoothManager(@ApplicationContext context: Context): BluetoothManager {
        return BluetoothManager(context)
    }

    @Provides
    @Singleton
    fun provideWiFiDirectManager(@ApplicationContext context: Context): WiFiDirectManager {
        return WiFiDirectManager(context)
    }
}
