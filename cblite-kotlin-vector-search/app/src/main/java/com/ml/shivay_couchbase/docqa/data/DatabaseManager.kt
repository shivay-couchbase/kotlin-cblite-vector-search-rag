package com.ml.shivay_couchbase.docqa.data

import android.content.Context
import com.couchbase.lite.*

object DatabaseManager {

    private lateinit var database: Database

    fun init(context: Context, dbName: String = "myDatabase") {
        CouchbaseLite.init(context)
        database = Database(dbName)
    }

    fun getDatabase(): Database = database
}
