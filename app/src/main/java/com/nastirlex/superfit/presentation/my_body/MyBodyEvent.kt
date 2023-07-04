package com.nastirlex.superfit.presentation.my_body

import android.graphics.Bitmap
import android.net.Uri

interface MyBodyEvent

class UpdateParameters(val weight: Int, val height: Int) : MyBodyEvent

class LoadPhoto(val bitmap: Bitmap) : MyBodyEvent

class LoadPhotoOnServer(val bitmap: Bitmap) : MyBodyEvent

class LoadGalleryPhotoOnServer(val image: Uri) : MyBodyEvent