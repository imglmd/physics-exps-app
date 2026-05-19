package com.imglmd.physicsexps.data.mapper

import com.imglmd.physicsexps.data.remote.MediaDto
import com.imglmd.physicsexps.domain.model.Media

fun Media.toDto(): MediaDto {
    return MediaDto(id = id, filename = filename, url = url, size = size, created_at = created_at)
}

fun MediaDto.toUi(): Media {
    return Media(id = id, filename = filename, url = url, size = size, created_at = created_at)
}