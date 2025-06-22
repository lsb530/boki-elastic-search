package boki.elasticsearchdemo.dto

import boki.elasticsearchdemo.document.ProductDocument
import boki.elasticsearchdemo.entity.ProductEntity

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val category: String,
) {

    enum class HighlightField {
        NAME,
        DESCRIPTION,
        CATEGORY
    }

    companion object {
        fun ofEntity(productEntity: ProductEntity): ProductResponse {
            return ProductResponse(
                id = productEntity.id!!,
                name = productEntity.name,
                description = productEntity.description,
                price = productEntity.price,
                rating = productEntity.rating,
                category = productEntity.category
            )
        }

        fun ofDocument(productDocument: ProductDocument): ProductResponse {
            return ProductResponse(
                id = productDocument.id.toLong(),
                name = productDocument.name,
                description = productDocument.description,
                price = productDocument.price,
                rating = productDocument.rating,
                category = productDocument.category
            )
        }

        fun withHighlight(
            productEntity: ProductDocument,
            field: HighlightField,
            highlightValue: String
        ): ProductResponse {
            val base = ofDocument(productEntity)
            return when (field) {
                HighlightField.NAME -> base.copy(name = highlightValue)
                HighlightField.DESCRIPTION -> base.copy(description = highlightValue)
                HighlightField.CATEGORY -> base.copy(category = highlightValue)
            }
        }
    }
}