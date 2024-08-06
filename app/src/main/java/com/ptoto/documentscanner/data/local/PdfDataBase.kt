package com.ptoto.documentscanner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ptoto.documentscanner.data.dao.PdfDao
import com.ptoto.documentscanner.data.local.converters.DateTypeConverter
import com.ptoto.documentscanner.data.models.PdfEntity

@Database(entities = [PdfEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class PdfDataBase : RoomDatabase() {

    abstract val pdfDao: PdfDao

    companion object {
        private var INSTANCES: PdfDataBase? = null

        fun getInstance(context: Context): PdfDataBase {
            synchronized(this) {
                return INSTANCES ?: Room.databaseBuilder(
                    context.applicationContext,
                    PdfDataBase::class.java,
                    "pdf_db"
                ).build().also {
                    INSTANCES = it
                }
            }
        }
    }

}