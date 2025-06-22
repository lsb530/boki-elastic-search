package boki.elasticsearchdemo.service

import boki.elasticsearchdemo.document.ProductDocument
import boki.elasticsearchdemo.dto.ProductResponse
import co.elastic.clients.elasticsearch._types.query_dsl.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.HighlightQuery
import org.springframework.data.elasticsearch.core.query.highlight.Highlight
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val elasticsearchOperations: ElasticsearchOperations
) {
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

    fun searchProducts(
        query: String,
        category: String?,
        minPrice: Double,
        maxPrice: Double,
        pageable: Pageable
    ): List<ProductResponse> {
        val multiMatchQuery = MultiMatchQuery.of { m ->
            m.query(query)
                .fields(
                    "name^3",
                    "category^2",
                    "description^1",
                )
                .fuzziness("AUTO")
        }._toQuery()

        val filters = mutableListOf<Query>()
        category?.let {
            val categoryFilter = TermQuery.of { t ->
                t.field("category.raw")
                    .value(category)
            }._toQuery()

            filters.add(categoryFilter)
        }

        val priceRangeFilter = NumberRangeQuery.of { r ->
            r.field("price")
                .gte(minPrice)
                .lte(maxPrice)
        }._toRangeQuery()._toQuery()
        filters.add(priceRangeFilter)

        val ratingShould = NumberRangeQuery.of { r ->
            r.field("rating")
                .gt(4.0)
        }._toRangeQuery()._toQuery()

        val boolQuery = BoolQuery.of { b ->
            b.must(multiMatchQuery)
                .filter(filters)
                .should(ratingShould)
        }._toQuery()

        val highlightParams = HighlightParameters.builder()
            .withPreTags("<b>")
            .withPostTags("</b>")
            .build()

        val highlight = Highlight(highlightParams, listOf(
            HighlightField("name")
        ))

        val highlightQuery = HighlightQuery(highlight, ProductDocument::class.java)

        val pageRequest = PageRequest.of(pageable.pageNumber - 1, pageable.pageSize)

        val nativeQuery = NativeQuery.builder()
            .withQuery(boolQuery)
            .withHighlightQuery(highlightQuery)
            .withPageable(pageRequest)
            .build()

        val searchQuery = elasticsearchOperations.search(
            nativeQuery,
            ProductDocument::class.java
        )

        return searchQuery.searchHits.map { hit ->
            val base = ProductResponse.ofDocument(hit.content)

            val highlightedName = hit.getHighlightField("name").firstOrNull()
            highlightedName?.let {
                ProductResponse.withHighlight(
                    hit.content,
                    ProductResponse.HighlightField.NAME,
                    highlightedName
                )
            } ?: base
        }
    }
}