package boki.elasticsearchdemo.service

import boki.elasticsearchdemo.document.ProductDocument
import boki.elasticsearchdemo.dto.CreateProductRequest
import boki.elasticsearchdemo.dto.ProductResponse
import boki.elasticsearchdemo.entity.ProductEntity
import boki.elasticsearchdemo.repository.ProductDocumentRepository
import boki.elasticsearchdemo.repository.ProductEntityRepository
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productEntityRepository: ProductEntityRepository,
    private val productDocumentRepository: ProductDocumentRepository,
    private val elasticsearchOperations: ElasticsearchOperations,
) {
    @Transactional(readOnly = true)
    fun getProducts(page: Int, size: Int): PagedModel<ProductResponse> {
        val pageable = PageRequest.of(page - 1, size)
        val pageQueryResponse = productEntityRepository.findAll(pageable).map { productEntity ->
            ProductResponse.of(productEntity)
        }
        return PagedModel(pageQueryResponse)
    }

    fun getSuggestions(query: String): List<String> {
        val multiMatchQuery = MultiMatchQuery.of { m ->
            m.query(query)
                .type(TextQueryType.BoolPrefix)
                .fields(
                    "name.auto_complete",
                    "name.auto_complete._2gram",
                    "name.auto_complete._3gram",
                )
        }._toQuery()

        val nativeQuery = NativeQuery.builder()
            .withQuery(multiMatchQuery)
            .withPageable(PageRequest.of(0, 5))
            .build()

        val searchQuery = elasticsearchOperations.search(
            nativeQuery,
            ProductDocument::class.java
        )

        return searchQuery.searchHits
            .map { it.content.name }
            .toList()
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
        val savedProduct = productEntityRepository.save(product).let(ProductResponse::of)

        val productDocument = ProductDocument(
            id = savedProduct.id.toString(),
            name = savedProduct.name,
            description = savedProduct.description,
            price = savedProduct.price,
            rating = savedProduct.rating,
            category = savedProduct.category
        )
        productDocumentRepository.save(productDocument)

        return savedProduct
    }

    @Transactional
    fun deleteProduct(id: Long) {
        val existProduct = productEntityRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Product with ID $id not found")
        productEntityRepository.delete(existProduct)

        productDocumentRepository.deleteById(existProduct.id.toString())
    }
}