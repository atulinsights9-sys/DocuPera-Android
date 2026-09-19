package com.example.data.pdf

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PdfExportService(private val context: Context) {

    suspend fun generateDocumentPdf(
        document: DocumentData,
        isProUser: Boolean = false,
        customBusinessFooter: String? = null
    ): File = withContext(Dispatchers.IO) {
        val pdfDoc = PdfDocument()

        val pageWidth = if (document.orientation == DocOrientation.PORTRAIT) {
            document.pageSize.widthPt
        } else {
            document.pageSize.heightPt
        }
        val pageHeight = if (document.orientation == DocOrientation.PORTRAIT) {
            document.pageSize.heightPt
        } else {
            document.pageSize.widthPt
        }

        val margin = 46f
        val contentWidth = pageWidth - (margin * 2)

        // Text Paints
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(15, 23, 42) // Slate 900
            textSize = 18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val sectionHeadingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(37, 99, 235) // Royal Blue 600
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(51, 65, 85) // Slate 700
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val metaLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(100, 116, 139) // Slate 500
            textSize = 8.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val metaValuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(30, 41, 59) // Slate 800
            textSize = 9f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(148, 163, 184) // Slate 400
            textSize = 8f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val watermarkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(26, 37, 99, 235) // 10% opacity blue
            textSize = 34f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(226, 232, 240) // Slate 200
            strokeWidth = 1f
        }

        val accentBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(37, 99, 235) // Royal Blue
        }

        // Layout measurements & multi-page planning
        var currentPageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
        var page = pdfDoc.startPage(pageInfo)
        var canvas = page.canvas

        fun drawHeaderAndFooter(c: Canvas, pgNum: Int) {
            // Top subtle accent bar
            c.drawRect(0f, 0f, pageWidth.toFloat(), 4f, accentBarPaint)

            // Bottom line
            val footerY = pageHeight - 24f
            c.drawLine(margin, footerY - 8f, pageWidth - margin, footerY - 8f, linePaint)

            // Footer text
            val footerText = customBusinessFooter?.takeIf { it.isNotBlank() } ?: document.footerText
            c.drawText(footerText, margin, footerY, footerPaint)

            if (document.showPageNumbers) {
                val pageStr = "Page $pgNum"
                val textW = footerPaint.measureText(pageStr)
                c.drawText(pageStr, pageWidth - margin - textW, footerY, footerPaint)
            }

            // Watermark for free tier
            if (!isProUser && document.isWatermarked) {
                c.save()
                c.rotate(-30f, pageWidth / 2f, pageHeight / 2f)
                c.drawText("DOCUPERA PREVIEW", pageWidth / 2f, pageHeight / 2f, watermarkPaint)
                c.restore()
            }
        }

        drawHeaderAndFooter(canvas, currentPageNumber)

        var currentY = margin + 12f
        val maxY = pageHeight - margin - 28f

        fun checkPageBreak(requiredHeight: Float) {
            if (currentY + requiredHeight > maxY) {
                pdfDoc.finishPage(page)
                currentPageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
                page = pdfDoc.startPage(pageInfo)
                canvas = page.canvas
                drawHeaderAndFooter(canvas, currentPageNumber)
                currentY = margin + 12f
            }
        }

        // 1. Draw Title
        canvas.drawText(document.title, margin, currentY + 14f, titlePaint)
        currentY += 24f
        canvas.drawLine(margin, currentY, margin + contentWidth, currentY, linePaint)
        currentY += 12f

        // 2. Draw Metadata Block (Sender, Recipient, Date, Reference)
        val meta = document.metadata
        val hasSender = meta.senderName.isNotBlank() || meta.senderAddress.isNotBlank()
        val hasRecipient = meta.recipientName.isNotBlank() || meta.recipientAddress.isNotBlank()

        if (hasSender || hasRecipient || meta.dateString.isNotBlank() || meta.refNumber.isNotBlank()) {
            checkPageBreak(50f)
            val halfW = contentWidth / 2f

            if (meta.dateString.isNotBlank()) {
                val dateText = "Date: ${meta.dateString}"
                val dw = metaValuePaint.measureText(dateText)
                canvas.drawText(dateText, pageWidth - margin - dw, currentY + 8f, metaValuePaint)
            }

            if (hasSender) {
                canvas.drawText("FROM:", margin, currentY + 8f, metaLabelPaint)
                var sy = currentY + 20f
                if (meta.senderName.isNotBlank()) {
                    canvas.drawText(meta.senderName, margin, sy, metaValuePaint)
                    sy += 12f
                }
                if (meta.senderTitle.isNotBlank()) {
                    canvas.drawText(meta.senderTitle, margin, sy, metaValuePaint)
                    sy += 12f
                }
                if (meta.senderAddress.isNotBlank()) {
                    val lines = wrapText(meta.senderAddress, halfW - 20f, metaValuePaint)
                    for (line in lines) {
                        canvas.drawText(line, margin, sy, metaValuePaint)
                        sy += 12f
                    }
                }
                if (meta.senderContact.isNotBlank()) {
                    canvas.drawText(meta.senderContact, margin, sy, metaValuePaint)
                    sy += 12f
                }
            }

            if (hasRecipient) {
                val rx = margin + halfW + 10f
                canvas.drawText("TO:", rx, currentY + 8f, metaLabelPaint)
                var ry = currentY + 20f
                if (meta.recipientName.isNotBlank()) {
                    canvas.drawText(meta.recipientName, rx, ry, metaValuePaint)
                    ry += 12f
                }
                if (meta.recipientTitle.isNotBlank()) {
                    canvas.drawText(meta.recipientTitle, rx, ry, metaValuePaint)
                    ry += 12f
                }
                if (meta.recipientAddress.isNotBlank()) {
                    val lines = wrapText(meta.recipientAddress, halfW - 20f, metaValuePaint)
                    for (line in lines) {
                        canvas.drawText(line, rx, ry, metaValuePaint)
                        ry += 12f
                    }
                }
            }

            currentY += 46f

            if (meta.refNumber.isNotBlank()) {
                checkPageBreak(20f)
                canvas.drawText(meta.refNumber, margin, currentY, titlePaint.apply { textSize = 11f })
                titlePaint.textSize = 18f
                currentY += 16f
            }

            if (meta.amountTotal.isNotBlank()) {
                checkPageBreak(24f)
                val amtBoxH = 22f
                val amtBoxW = 160f
                val amtX = pageWidth - margin - amtBoxW
                val bgPaint = Paint().apply { color = Color.rgb(238, 242, 255) }
                canvas.drawRect(amtX, currentY - 14f, amtX + amtBoxW, currentY - 14f + amtBoxH, bgPaint)
                val amtText = "Total: ${meta.amountTotal}"
                canvas.drawText(amtText, amtX + 8f, currentY + 2f, sectionHeadingPaint)
                currentY += 20f
            }

            canvas.drawLine(margin, currentY, margin + contentWidth, currentY, linePaint)
            currentY += 14f
        }

        // 3. Draw Document Sections
        for (section in document.sections) {
            if (section.heading.isNotBlank()) {
                checkPageBreak(26f)
                canvas.drawText(section.heading, margin, currentY + 10f, sectionHeadingPaint)
                currentY += 18f
            }

            if (section.body.isNotBlank()) {
                val paragraphs = section.body.split("\n")
                for (para in paragraphs) {
                    if (para.isBlank()) {
                        currentY += 6f
                        continue
                    }
                    val lines = wrapText(para, contentWidth, bodyPaint)
                    for (line in lines) {
                        checkPageBreak(14f)
                        canvas.drawText(line, margin, currentY + 9f, bodyPaint)
                        currentY += 14f
                    }
                }
                currentY += 6f
            }

            if (section.bulletPoints.isNotEmpty()) {
                for (bullet in section.bulletPoints) {
                    val lines = wrapText(bullet, contentWidth - 16f, bodyPaint)
                    for ((i, line) in lines.withIndex()) {
                        checkPageBreak(14f)
                        if (i == 0) {
                            // Draw bullet circle
                            canvas.drawCircle(margin + 5f, currentY + 5f, 2f, accentBarPaint)
                        }
                        canvas.drawText(line, margin + 14f, currentY + 9f, bodyPaint)
                        currentY += 14f
                    }
                }
                currentY += 6f
            }

            if (section.isSignature) {
                currentY += 10f
            }
        }

        pdfDoc.finishPage(page)

        // Save to cache directory
        val exportDir = File(context.cacheDir, "docupera_exports")
        if (!exportDir.exists()) exportDir.mkdirs()

        val safeTitle = document.title.replace("[^a-zA-Z0-9.-]".toRegex(), "_").take(30)
        val file = File(exportDir, "${safeTitle}_${System.currentTimeMillis()}.pdf")
        val outStream = FileOutputStream(file)
        pdfDoc.writeTo(outStream)
        outStream.flush()
        outStream.close()
        pdfDoc.close()

        file
    }

    suspend fun generatePhotosToPdf(
        pages: List<PhotoDocPage>,
        documentTitle: String = "Photo_Document",
        pageSize: DocPageSize = DocPageSize.A4
    ): File = withContext(Dispatchers.IO) {
        val pdfDoc = PdfDocument()
        val width = pageSize.widthPt
        val height = pageSize.heightPt
        val margin = 36f
        val printableW = (width - margin * 2).toInt()
        val printableH = (height - margin * 2).toInt()

        for ((index, pageItem) in pages.withIndex()) {
            val pageInfo = PdfDocument.PageInfo.Builder(width, height, index + 1).create()
            val pdfPage = pdfDoc.startPage(pageInfo)
            val canvas = pdfPage.canvas

            try {
                val inputStream = context.contentResolver.openInputStream(pageItem.imageUri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (originalBitmap != null) {
                    val processedBitmap = applyPhotoFilter(originalBitmap, pageItem)

                    // Scale while maintaining aspect ratio
                    val scale = minOf(
                        printableW.toFloat() / processedBitmap.width.toFloat(),
                        printableH.toFloat() / processedBitmap.height.toFloat()
                    )
                    val scaledW = processedBitmap.width * scale
                    val scaledH = processedBitmap.height * scale
                    val left = margin + (printableW - scaledW) / 2f
                    val top = margin + (printableH - scaledH) / 2f

                    canvas.drawBitmap(
                        processedBitmap,
                        null,
                        RectF(left, top, left + scaledW, top + scaledH),
                        Paint(Paint.FILTER_BITMAP_FLAG)
                    )

                    // Page number footer
                    val footerPaint = Paint().apply {
                        color = Color.GRAY
                        textSize = 8f
                    }
                    canvas.drawText("Page ${index + 1} of ${pages.size} • DocuPera", margin, height - 16f, footerPaint)
                }
            } catch (_: Exception) {
                // Draw fallback placeholder
                val errorPaint = Paint().apply {
                    color = Color.RED
                    textSize = 12f
                }
                canvas.drawText("Could not load image for page ${index + 1}", margin, height / 2f, errorPaint)
            }

            pdfDoc.finishPage(pdfPage)
        }

        val exportDir = File(context.cacheDir, "docupera_exports")
        if (!exportDir.exists()) exportDir.mkdirs()
        val file = File(exportDir, "${documentTitle}_${System.currentTimeMillis()}.pdf")
        val outStream = FileOutputStream(file)
        pdfDoc.writeTo(outStream)
        outStream.flush()
        outStream.close()
        pdfDoc.close()

        file
    }

    fun sharePdf(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, file.name)
            putExtra(Intent.EXTRA_TEXT, "Generated via DocuPera Document Studio")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(shareIntent, "Share Document PDF").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    fun openPdf(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val openIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(openIntent, "Open PDF").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    private fun applyPhotoFilter(bitmap: Bitmap, page: PhotoDocPage): Bitmap {
        var bmp = bitmap
        // Apply rotation if needed
        if (page.rotationDegrees != 0f) {
            val matrix = Matrix().apply { postRotate(page.rotationDegrees) }
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        }

        // Apply filters
        val colorMatrix = ColorMatrix()
        when (page.filter) {
            PhotoFilter.BLACK_WHITE -> {
                colorMatrix.setSaturation(0f)
            }
            PhotoFilter.HIGH_CONTRAST -> {
                colorMatrix.setSaturation(0f)
                val scale = 1.4f
                val translate = (-0.5f * scale + 0.5f) * 255f
                val contrastMatrix = ColorMatrix(
                    floatArrayOf(
                        scale, 0f, 0f, 0f, translate,
                        0f, scale, 0f, 0f, translate,
                        0f, 0f, scale, 0f, translate,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                colorMatrix.postConcat(contrastMatrix)
            }
            PhotoFilter.DOCUMENT_CLEANUP -> {
                // Crisp document cleanup: increase contrast slightly, sharpen
                colorMatrix.setSaturation(0.2f)
                val scale = 1.25f
                val translate = (-0.5f * scale + 0.5f) * 255f
                val docMatrix = ColorMatrix(
                    floatArrayOf(
                        scale, 0f, 0f, 0f, translate,
                        0f, scale, 0f, 0f, translate,
                        0f, 0f, scale, 0f, translate,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
                colorMatrix.postConcat(docMatrix)
            }
            PhotoFilter.ORIGINAL -> {}
        }

        val filteredBitmap = Bitmap.createBitmap(bmp.width, bmp.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(filteredBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }
        canvas.drawBitmap(bmp, 0f, 0f, paint)
        return filteredBitmap
    }

    private fun wrapText(text: String, maxWidth: Float, paint: Paint): List<String> {
        val lines = mutableListOf<String>()
        val words = text.split(" ")
        var currentLine = ""

        for (word in words) {
            val candidate = if (currentLine.isEmpty()) word else "$currentLine $word"
            if (paint.measureText(candidate) <= maxWidth) {
                currentLine = candidate
            } else {
                if (currentLine.isNotEmpty()) lines.add(currentLine)
                currentLine = word
            }
        }
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }
        return lines
    }
}
