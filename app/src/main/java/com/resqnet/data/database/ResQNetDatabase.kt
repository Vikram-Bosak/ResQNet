package com.resqnet.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.resqnet.data.models.Device
import com.resqnet.data.models.Message
import com.resqnet.data.database.converters.*

@Database(
    entities = [Message::class, Device::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    MessageStatusConverter::class,
    MessagePriorityConverter::class,
    MessageTypeConverter::class,
    DeviceTypeConverter::class,
    ConnectionTypeConverter::class,
    LocationDataConverter::class
)
abstract class ResQNetDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun deviceDao(): DeviceDao

    companion object {
        @Volatile
        private var INSTANCE: ResQNetDatabase? = null

        fun getDatabase(context: Context): ResQNetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ResQNetDatabase::class.java,
                    "resqnet_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
