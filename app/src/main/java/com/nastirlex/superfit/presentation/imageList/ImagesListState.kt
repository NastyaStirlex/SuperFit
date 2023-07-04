package com.nastirlex.superfit.presentation.imageList

import com.nastirlex.superfit.net.dto.PhotoDto

sealed class ImagesListState {
    class FullImagesList(val imagesList: MutableMap<String, MutableList<PhotoDto>>) :
        ImagesListState()

    object EmptyImagesList : ImagesListState()
}