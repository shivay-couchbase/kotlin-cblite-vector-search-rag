package com.ml.shivay_couchbase.docqa.data

import com.couchbase.lite.*

class ChunksDB {

    private val database: Database = DatabaseManager.getDatabase()

    fun addChunk(chunk: Chunk) {
        val mutableDoc = MutableDocument()
        mutableDoc.setString("docFileName", chunk.docFileName)
        mutableDoc.setString("chunkData", chunk.chunkData)
        mutableDoc.setArray("chunkEmbedding", MutableArray().apply {
            chunk.chunkEmbedding.forEach { addFloat(it) }
        })
        database.save(mutableDoc)
    }

    fun getSimilarChunks(queryEmbedding: FloatArray, n: Int = 5): List<Pair<Float, Chunk>> {
        val query = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.database(database))

        val result = query.execute()
        val chunksWithScores = result.map { resultRow ->
            val doc = resultRow.getDictionary(database.name)
            val chunkEmbeddingArray = doc?.getArray("chunkEmbedding") ?: MutableArray()

            // Manually convert the MutableArray into a FloatArray
            val chunkEmbedding = FloatArray(chunkEmbeddingArray.count()) { i ->
                chunkEmbeddingArray.getFloat(i)
            }

            val chunk = Chunk(
                chunkId = doc?.getString("id")?.toLong() ?: 0,
                docFileName = doc?.getString("docFileName") ?: "",
                chunkData = doc?.getString("chunkData") ?: "",
                chunkEmbedding = chunkEmbedding
            )

            val score = cosineSimilarity(queryEmbedding, chunkEmbedding)
            Pair(score, chunk)
        }

        return chunksWithScores.sortedByDescending { it.first }.take(n)
    }
    fun cosineSimilarity(vectorA: FloatArray, vectorB: FloatArray): Float {
        if (vectorA.size != vectorB.size) return 0f

        val dotProduct = vectorA.zip(vectorB).sumOf { (a, b) -> (a * b).toDouble() }.toFloat()
        val magnitudeA = kotlin.math.sqrt(vectorA.sumOf { (it * it).toDouble() }).toFloat()
        val magnitudeB = kotlin.math.sqrt(vectorB.sumOf { (it * it).toDouble() }).toFloat()

        return if (magnitudeA != 0f && magnitudeB != 0f) {
            (dotProduct / (magnitudeA * magnitudeB))
        } else 0f
    }




    fun removeChunks(docId: Long) {
        val query = QueryBuilder
            .select(SelectResult.expression(Meta.id))
            .from(DataSource.database(database))
            .where(Expression.property("docId").equalTo(Expression.longValue(docId)))

        val result = query.execute()
        result.forEach { row ->
            val docId = row.getString("id") ?: return@forEach
            val doc = database.getDocument(docId)
            doc?.let { database.delete(it) }
        }
    }
}




































