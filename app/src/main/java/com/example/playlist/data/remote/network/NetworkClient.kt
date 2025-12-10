package com.example.playlist.data.remote.network

import com.example.playlist.data.remote.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}