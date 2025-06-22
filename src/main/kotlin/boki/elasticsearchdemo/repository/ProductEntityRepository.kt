package boki.elasticsearchdemo.repository

import boki.elasticsearchdemo.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductEntityRepository: JpaRepository<ProductEntity, Long>