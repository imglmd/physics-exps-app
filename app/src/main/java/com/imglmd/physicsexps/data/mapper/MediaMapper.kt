package com.imglmd.physicsexps.data.mapper

import com.imglmd.physicsexps.data.remote.MediaDto
import com.imglmd.physicsexps.data.remote.MediaListDto
import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.model.MediaList

fun Media.toDto(): MediaDto {
    return MediaDto(mediaId = mediaId, filename = filename, url = url, size = size, createdAt = createdAt)
}

fun MediaDto.toUi(): Media {
    return Media(mediaId = mediaId, filename = filename, url = url, size = size, createdAt = createdAt)
}

fun MediaListDto.toUi(): MediaList {
    return MediaList(runId = runId, media = media.map { it.toUi() } )
}

fun MediaList.toDto(): MediaListDto {
    return MediaListDto(runId = runId, media = media.map { it.toDto() } )
}