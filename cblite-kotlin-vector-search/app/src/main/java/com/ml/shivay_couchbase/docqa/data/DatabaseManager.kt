package com.ml.shivay_couchbase.docqa.data

import android.content.Context
import android.util.Log
import com.couchbase.lite.*

object DatabaseManager {

    private lateinit var database: Database
    private const val TAG = "DatabaseManager"
    private var isVectorSearchEnabled = false

    fun init(context: Context, dbName: String = "myDatabase") {
        try {
            CouchbaseLite.init(context)
            Log.i(TAG, "CouchbaseLite initialized successfully")


             // Try to enable Vector Search
            try {
                CouchbaseLite.enableVectorSearch()
                isVectorSearchEnabled = true
                Log.i(TAG, "Vector Search enabled successfully")
            } catch (e: CouchbaseLiteException) {
                Log.e(TAG, "Failed to enable Vector Search: ${e.message}")
                isVectorSearchEnabled = false
            }
            
            database = Database(dbName)
            Log.i(TAG, "Database '$dbName' opened successfully")

          
          
        } catch (e: CouchbaseLiteException) {
            Log.e(TAG, "Failed to initialize database: ${e.message}")
            throw RuntimeException("Database initialization failed", e)
        }
    }

    fun getDatabase(): Database = database

    fun isVectorSearchEnabled(): Boolean = isVectorSearchEnabled
}