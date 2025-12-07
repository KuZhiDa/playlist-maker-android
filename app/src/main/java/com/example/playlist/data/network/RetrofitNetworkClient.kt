package com.example.playlist.data.network

import com.example.playlist.data.dto.TracksSearchRequest
import com.example.playlist.domain.NetworkClient
import retrofit2.HttpException
import java.io.IOException
import com.example.playlist.data.dto.BaseResponse
import com.example.playlist.data.dto.ITunesInterface


class RetrofitNetworkClient(
    private val apiService: ITunesInterface
) : NetworkClient {

    override suspend fun doRequest(dto: Any): BaseResponse {
        return when (dto) {
            is TracksSearchRequest -> {
                try {
                    apiService.searchTracks(term = dto.expression.trim()).apply { resultCode = 200 }
                } catch (httpException: HttpException) {
                    throw IOException(httpException)
                }
            }
            else -> throw IllegalArgumentException("Unsupported request type: ${dto::class.java}")
        }
    }
}