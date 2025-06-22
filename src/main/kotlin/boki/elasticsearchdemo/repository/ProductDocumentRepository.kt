package boki.elasticsearchdemo.repository

import boki.elasticsearchdemo.document.ProductDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductDocumentRepository : ElasticsearchRepository<ProductDocument, String>