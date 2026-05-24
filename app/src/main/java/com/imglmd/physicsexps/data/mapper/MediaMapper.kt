package com.imglmd.physicsexps.data.mapper

import com.imglmd.physicsexps.data.remote.MediaDto
import com.imglmd.physicsexps.data.remote.MediaListDto
import com.imglmd.physicsexps.data.remote.RemoteConfig
import com.imglmd.physicsexps.domain.model.Media
import com.imglmd.physicsexps.domain.model.MediaList

fun Media.toDto(): MediaDto {
    return MediaDto(mediaId = mediaId, filename = filename, url = url, size = size, createdAt = createdAt,
        experimentId = experimentId)
}

fun MediaDto.toUi(remoteConfig: RemoteConfig): Media {
    return Media(
        mediaId = mediaId,
        filename = filename,
        url = remoteConfig.resolveUrl(url),
        size = size,
        createdAt = createdAt,
        experimentId = experimentId
    )
}

fun MediaListDto.toUi(remoteConfig: RemoteConfig): MediaList {
    return MediaList(runId = runId, media = media.map { it.toUi(remoteConfig) })
}

fun MediaList.toDto(): MediaListDto {
    return MediaListDto(runId = runId, media = media.map { it.toDto() } )
}