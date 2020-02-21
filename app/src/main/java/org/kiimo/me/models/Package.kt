package org.kiimo.me.models

data class Package(
    val packageId: String?,
    val title: Any?,
    val images: List<String?>?,
    val videos: Any?,
    val description: String?,
    val deliveryId: String?,
    val packageSizeId: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val packageSize: PackageSize?
)