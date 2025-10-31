package com.example.playlist.domain

import com.example.playlist.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}