package com.final_year_project.kisaan10.localDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Blogs::class], version = 1)
abstract class KissanDatabase : RoomDatabase(){
    abstract fun blogsDAO():BlogsDAO

    companion object{
        private var INSTANCE: KissanDatabase? =null
        fun getDatabase(context: Context):KissanDatabase{
            if(INSTANCE ==null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        KissanDatabase::class.java,
                        "kisaanDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}