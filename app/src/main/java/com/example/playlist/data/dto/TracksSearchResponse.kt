package com.example.playlist.data.dto

import com.example.playlist.domain.TrackDto

class TracksSearchResponse(val results: List<TrackDto>) : BaseResponse()