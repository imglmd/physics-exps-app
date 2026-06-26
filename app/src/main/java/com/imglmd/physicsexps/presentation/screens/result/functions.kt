package com.imglmd.physicsexps.presentation.screens.result

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.imglmd.physicsexps.R
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap
import com.imglmd.physicsexps.presentation.core.getStringByKey


fun saveResultAsPdf(
    context: Context,
    nameExp: String,
    nameSection: String,
    data: Map<String, String>
) {
    val pdfDoc = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDoc.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    val logo = ContextCompat.getDrawable(context, R.drawable.app)?.toBitmap(90, 90, Bitmap.Config.ARGB_8888)
    if (logo != null) {
        canvas.drawBitmap(logo, (canvas.width / 2 - 50).toFloat(), 20f, paint)
    }

    //заголовок
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 20f
    paint.isFakeBoldText = false
    paint.apply {
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
    }
    canvas.drawText(context.getString(R.string.exp_rep), (canvas.width/2).toFloat(), 150f, paint)

    //описание, ну тип там название и категория
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 16f
    paint.isFakeBoldText = false
    paint.apply {
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.ITALIC)
    }
    canvas.drawText("$nameSection: $nameExp", (canvas.width/2).toFloat(), 190f, paint)

    // разделитель
    paint.color = Color.rgb(169, 7, 53)
    paint.strokeWidth = 3f
    canvas.drawLine(20f, 220f, canvas.width.toFloat() - 20, 220f, paint)

    //результаты
    paint.textAlign = Paint.Align.LEFT
    paint.color = Color.BLACK
    paint.textSize = 14f
    paint.isFakeBoldText = false
    paint.apply {
        typeface = android.graphics.Typeface.DEFAULT
    }

    var yDist = 250f
    val step = 30f
    for ((name, value) in data) {
        canvas.drawText("$name: $value", 50f, yDist, paint)
        yDist += step
    }

    // разделитель нижний
    paint.color = Color.rgb(169, 7, 53)
    paint.strokeWidth = 3f
    canvas.drawLine(20f, canvas.height - 50f, canvas.width.toFloat() - 20, canvas.height - 50f, paint)

    pdfDoc.finishPage(page)

    val pdfFileName = "physics_exp_${System.currentTimeMillis()}.pdf"
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
    }

    val pdfUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

    if (pdfUri != null) {
        resolver.openOutputStream(pdfUri).use {
            pdfDoc.writeTo(it)
            Toast.makeText(context, "${context.getString(R.string.pdf)} $pdfFileName", Toast.LENGTH_LONG).show()
        }
    }

    pdfDoc.close()
}