package boki.elasticsearchdemo.controller

import boki.elasticsearchdemo.dto.CreateProductRequest
import boki.elasticsearchdemo.service.ProductQueryService
import boki.elasticsearchdemo.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/products")
@RestController
class ProductController(
    private val productService: ProductService,
    private val productQueryService: ProductQueryService,
) {
    @PostMapping
    fun createProduct(
        @RequestBody reqeust: CreateProductRequest,
    ): ResponseEntity<Any> {
        val response = productService.createProduct(reqeust)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping
    fun getProducts(
        @PageableDefault(page = 1, size = 10) pageable: Pageable
    ): ResponseEntity<Any> {
        val page = pageable.pageNumber
        val size = pageable.pageSize
        val response = productService.getProducts(page, size)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/suggestions")
    fun getProductsSuggestions(
        @RequestParam(value = "query") query: String,
    ): ResponseEntity<Any> {
        val response = productQueryService.getSuggestions(query)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/search")
    fun searchProducts(
        @RequestParam(value = "query") query: String,
        @RequestParam(value = "category", required = false) category: String?,
        @RequestParam(value = "minPrice", defaultValue = "0") minPrice: Double,
        @RequestParam(value = "maxPrice", defaultValue = "100000000") maxPrice: Double,
        @PageableDefault(page = 1, size = 5) pageable: Pageable
    ): ResponseEntity<Any> {
        val response = productQueryService.searchProducts(
            query,
            category,
            minPrice,
            maxPrice,
            pageable
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable productId: Long): ResponseEntity<Unit> {
        productService.deleteProduct(productId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}