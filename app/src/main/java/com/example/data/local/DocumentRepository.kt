package com.example.data.local

import android.content.Context
import com.example.data.model.DocumentData
import com.example.data.model.TemplateLibrary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DocumentRepository(private val dao: DocumentDao) {

    val allDocuments: Flow<List<DocumentData>> = dao.getAllDocuments().map { list ->
        list.map { it.toDocumentData() }
    }

    val documentCount: Flow<Int> = dao.getDocumentCount()

    fun getDocumentsByCategory(category: String): Flow<List<DocumentData>> {
        return dao.getDocumentsByCategory(category).map { list ->
            list.map { it.toDocumentData() }
        }
    }

    fun searchDocuments(query: String): Flow<List<DocumentData>> {
        return dao.searchDocuments(query).map { list ->
            list.map { it.toDocumentData() }
        }
    }

    suspend fun getDocumentById(id: Long): DocumentData? = withContext(Dispatchers.IO) {
        dao.getDocumentById(id)?.toDocumentData()
    }

    suspend fun saveDocument(document: DocumentData): Long = withContext(Dispatchers.IO) {
        val updated = document.copy(updatedAt = System.currentTimeMillis())
        dao.insertOrUpdateDocument(updated.toEntity())
    }

    suspend fun deleteDocument(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteDocumentById(id)
    }

    suspend fun duplicateDocument(document: DocumentData): Long = withContext(Dispatchers.IO) {
        val copyDoc = document.copy(
            id = 0,
            title = "${document.title} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertOrUpdateDocument(copyDoc.toEntity())
    }

    suspend fun seedInitialSampleDocumentsIfEmpty() = withContext(Dispatchers.IO) {
        val sample1 = TemplateLibrary.templates[0].initialData.copy(
            id = 0,
            createdAt = System.currentTimeMillis() - 86400000 * 2,
            updatedAt = System.currentTimeMillis() - 86400000 * 2
        )
        val sample2 = TemplateLibrary.templates[4].initialData.copy(
            id = 0,
            createdAt = System.currentTimeMillis() - 86400000,
            updatedAt = System.currentTimeMillis() - 86400000
        )
        dao.insertOrUpdateDocument(sample1.toEntity())
        dao.insertOrUpdateDocument(sample2.toEntity())
    }
}
