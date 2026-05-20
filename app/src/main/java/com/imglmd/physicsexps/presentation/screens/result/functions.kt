package com.imglmd.physicsexps.presentation.screens.result

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.imglmd.physicsexps.R
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap


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
    canvas.drawText("Отчёт по эксперименту", (canvas.width/2).toFloat(), 150f, paint)

    //описание, ну тип там название и категория
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 16f
    paint.isFakeBoldText = false
    paint.apply {
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.ITALIC)
    }
    canvas.drawText("$nameSection: $nameExp", (canvas.width/2).toFloat(), 190f, paint)

    // разделитель
    paint.color = android.graphics.Color.rgb(169, 7, 53)
    paint.strokeWidth = 3f
    canvas.drawLine(20f, 220f, canvas.width.toFloat() - 20, 220f, paint)

    //результаты
    paint.textAlign = Paint.Align.LEFT
    paint.color = android.graphics.Color.BLACK
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
    paint.color = android.graphics.Color.rgb(169, 7, 53)
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
            Toast.makeText(context, "PDF сохранён как $pdfFileName", Toast.LENGTH_LONG).show()
        }
    }

    pdfDoc.close()
}

fun getExperimentName(expId: String): String = when(expId) {
    "pendulum" -> "Математический маятник"
    "coulombs_law" -> "Закон Кулона"
    "doppler_effect" -> "Эффект Доплера"
    "free_fall" -> "Свободное падение тел"
    "harmonic_vibrations" -> "Гармонические колебания"
    "joule_lenz" -> "Закон Джоуля-Ленца"
    "physical_pendulum" -> "Физический маятник"
    "projectile_motion" -> "Движение тела, брошенного под углом к горизонту"
    "radioactive_decay" -> "Радиоактивный распад"
    "spring_pendulum" -> "Пружинный маятник"
    else -> ""
}

fun getCategoryName(expId: String): String = when(expId) {
    "pendulum", "harmonic_vibrations", "physical_pendulum", "spring_pendulum" -> "Механика"
    "coulombs_law", "joule_lenz" -> "Электричество"
    "doppler_effect" -> "Акустика"
    "free_fall", "projectile_motion" -> "Кинематика"
    "radioactive_decay" -> "Ядерная физика"
    else -> ""
}