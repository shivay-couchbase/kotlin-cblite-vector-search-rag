package com.ml.shivay_couchbase.docqa.di

import android.app.Application
import com.ml.shivay_couchbase.docqa.data.ChunksDB
import com.ml.shivay_couchbase.docqa.data.DocumentsDB
import com.ml.shivay_couchbase.docqa.domain.embeddings.SentenceEmbeddingProvider
import com.ml.shivay_couchbase.docqa.domain.llm.GeminiRemoteAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// AppModule provides dependencies that are to be injected by Hilt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // SingletonComponent ensures that instances survive
    // across the application's lifespan
    // @Singleton creates a single instance in the app's lifespan

    @Provides
    @Singleton
    fun provideDocumentsDB(): DocumentsDB {
        return DocumentsDB()
    }

    @Provides
    @Singleton
    fun provideChunksDB(): ChunksDB {
        return ChunksDB()
    }

    @Provides
    @Singleton
    fun provideGeminiRemoteAPI(): GeminiRemoteAPI {
        return GeminiRemoteAPI()
    }

    @Provides
    @Singleton
    fun provideSentenceEncoder(context: Application): SentenceEmbeddingProvider {
        return SentenceEmbeddingProvider(context)
    }
}
