package boki.elasticsearchdemo.dto

import boki.elasticsearchdemo.entity.ProductEntity
import org.springframework.data.jpa.domain.AbstractPersistable_.id

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val category: String,
) {
    companion object {
        fun of(productEntity: ProductEntity): ProductResponse {
            return ProductResponse(
                id = productEntity.id!!,
                name = productEntity.name,
                description = productEntity.description,
                price = productEntity.price,
                rating = productEntity.rating,
                category = productEntity.category
            )
        }
    }
}