package com.imglmd.physicsexps.domain.usecase.media

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.IOException


class UploadMediaUseCase(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(context: Context, uri: Uri, runId: String): Result<Media> = withContext(Dispatchers.IO) {
        runCatching {
            val contentResolver = context.contentResolver
            val mime = contentResolver.getType(uri) ?: "application/octet-stream"
            var filename = "file_${System.currentTimeMillis()}"
            if (MimeTypeMap.getSingleton().getExtensionFromMimeType(mime) != null) {
                filename = filename + ".${MimeTypeMap.getSingleton().getExtensionFromMimeType(mime)}"
            }

            val body =  object : RequestBody() {

                override fun contentType() = mime.toMediaTypeOrNull()

                override fun writeTo(sink: BufferedSink) {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        sink.writeAll(inputStream.source())
                    } ?: throw IOException("Ошибка загрузки медиа")

                }
            }

            val multipart = MultipartBody.Part.createFormData("file", filename, body)
            mediaRepository.uploadMedia(runId, multipart, filename)

        }
    }
}