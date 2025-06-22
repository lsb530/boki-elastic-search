package boki.elasticsearchdemo.controller

import boki.elasticsearchdemo.dto.CreateProductRequest
import boki.elasticsearchdemo.dto.ProductResponse
import boki.elasticsearchdemo.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/products")
@RestController
class ProductController(
    private val productService: ProductService,
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
        val response = productService.getSuggestions(query)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable productId: Long): ResponseEntity<Unit> {
        productService.deleteProduct(productId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}