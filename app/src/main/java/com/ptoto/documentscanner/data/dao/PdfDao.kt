package com.ptoto.documentscanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ptoto.documentscanner.data.models.PdfEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PdfDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdf(pdf: PdfEntity): Long

    @Update
    suspend fun updatePdf(pdf: PdfEntity): Int

    @Delete
    suspend fun deletePdf(pdf: PdfEntity): Int

    @Query("SELECT * FROM pdfTable")
    fun getAllPdfs(): Flow<List<PdfEntity>>

    @Query("SELECT * FROM pdfTable")
    fun getAllPdfsWithFlow(): List<PdfEntity>
}