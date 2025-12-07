package com.example.playlist.domain

import com.example.playlist.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}