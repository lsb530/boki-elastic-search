package boki.elasticsearchdemo.service

import boki.elasticsearchdemo.dto.CreateProductRequest
import boki.elasticsearchdemo.dto.ProductResponse
import boki.elasticsearchdemo.entity.ProductEntity
import boki.elasticsearchdemo.repository.ProductEntityRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productEntityRepository: ProductEntityRepository
) {
    @Transactional(readOnly = true)
    fun getProducts(page: Int, size: Int): PagedModel<ProductResponse> {
        val pageable = PageRequest.of(page - 1, size)
        val pageQueryResponse = productEntityRepository.findAll(pageable).map<ProductResponse?> { productEntity ->
            ProductResponse.of(productEntity)
        }
        return PagedModel(pageQueryResponse)
    }

    @Transactional
    fun createProduct(request: CreateProductRequest): ProductResponse {
        val product = ProductEntity(
            name = request.name,
            description = request.description,
            price = request.price,
            rating = request.rating,
            category = request.category,
        )
        return productEntityRepository.save(product).let(ProductResponse::of)
    }

    @Transactional
    fun deleteProduct(id: Long) {
        val existProduct = productEntityRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Product with ID $id not found")
        productEntityRepository.delete(existProduct)
    }
}