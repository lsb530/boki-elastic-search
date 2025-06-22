package boki.elasticsearchdemo.dto

data class CreateProductRequest(
    val name: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val category: String,
)