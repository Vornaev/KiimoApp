package org.kiimo.me.models.delivery

import java.io.File

data class UploadSignatureRequest(var media: File,var type: String) {
}