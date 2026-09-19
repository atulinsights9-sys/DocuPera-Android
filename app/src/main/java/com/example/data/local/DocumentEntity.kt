package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.*
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: String,
    val category: String,
    val style: String,
    val language: String,
    val pageSize: String,
    val orientation: String,
    val senderName: String,
    val senderTitle: String,
    val senderAddress: String,
    val senderContact: String,
    val recipientName: String,
    val recipientTitle: String,
    val recipientAddress: String,
    val dateString: String,
    val refNumber: String,
    val clientName: String,
    val currencySymbol: String,
    val amountTotal: String,
    val sectionsJson: String, // JSON representation of List<DocumentSection>
    val footerText: String,
    val showPageNumbers: Boolean,
    val isPro: Boolean,
    val isWatermarked: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

// Extension converters
fun DocumentEntity.toDocumentData(): DocumentData {
    val sections = mutableListOf<DocumentSection>()
    try {
        val array = JSONArray(sectionsJson)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val bulletsArray = obj.optJSONArray("bulletPoints")
            val bullets = mutableListOf<String>()
            if (bulletsArray != null) {
                for (j in 0 until bulletsArray.length()) {
                    bullets.add(bulletsArray.getString(j))
                }
            }
            sections.add(
                DocumentSection(
                    id = obj.optString("id", "sec_$i"),
                    heading = obj.optString("heading", ""),
                    body = obj.optString("body", ""),
                    bulletPoints = bullets,
                    isHeader = obj.optBoolean("isHeader", false),
                    isSignature = obj.optBoolean("isSignature", false)
                )
            )
        }
    } catch (_: Exception) {
        // Fallback if parsing fails
    }

    val docType = try { DocType.valueOf(type) } catch (_: Exception) { DocType.CUSTOM_DOC }
    val docCat = try { DocCategory.valueOf(category) } catch (_: Exception) { DocCategory.CONTENT }
    val docStyle = try { DocStyle.valueOf(style) } catch (_: Exception) { DocStyle.PROFESSIONAL }
    val docLang = try { DocLanguage.valueOf(language) } catch (_: Exception) { DocLanguage.EN }
    val docPageSize = try { DocPageSize.valueOf(pageSize) } catch (_: Exception) { DocPageSize.A4 }
    val docOrient = try { DocOrientation.valueOf(orientation) } catch (_: Exception) { DocOrientation.PORTRAIT }

    return DocumentData(
        id = id,
        title = title,
        type = docType,
        category = docCat,
        style = docStyle,
        language = docLang,
        pageSize = docPageSize,
        orientation = docOrient,
        metadata = DocumentMetadata(
            senderName = senderName,
            senderTitle = senderTitle,
            senderAddress = senderAddress,
            senderContact = senderContact,
            recipientName = recipientName,
            recipientTitle = recipientTitle,
            recipientAddress = recipientAddress,
            dateString = dateString,
            refNumber = refNumber,
            clientName = clientName,
            currencySymbol = currencySymbol,
            amountTotal = amountTotal
        ),
        sections = sections,
        footerText = footerText,
        showPageNumbers = showPageNumbers,
        isPro = isPro,
        isWatermarked = isWatermarked,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun DocumentData.toEntity(): DocumentEntity {
    val array = JSONArray()
    for (sec in sections) {
        val obj = JSONObject()
        obj.put("id", sec.id)
        obj.put("heading", sec.heading)
        obj.put("body", sec.body)
        val bulletsArray = JSONArray()
        for (b in sec.bulletPoints) {
            bulletsArray.put(b)
        }
        obj.put("bulletPoints", bulletsArray)
        obj.put("isHeader", sec.isHeader)
        obj.put("isSignature", sec.isSignature)
        array.put(obj)
    }

    return DocumentEntity(
        id = id,
        title = title,
        type = type.name,
        category = category.name,
        style = style.name,
        language = language.name,
        pageSize = pageSize.name,
        orientation = orientation.name,
        senderName = metadata.senderName,
        senderTitle = metadata.senderTitle,
        senderAddress = metadata.senderAddress,
        senderContact = metadata.senderContact,
        recipientName = metadata.recipientName,
        recipientTitle = metadata.recipientTitle,
        recipientAddress = metadata.recipientAddress,
        dateString = metadata.dateString,
        refNumber = metadata.refNumber,
        clientName = metadata.clientName,
        currencySymbol = metadata.currencySymbol,
        amountTotal = metadata.amountTotal,
        sectionsJson = array.toString(),
        footerText = footerText,
        showPageNumbers = showPageNumbers,
        isPro = isPro,
        isWatermarked = isWatermarked,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
