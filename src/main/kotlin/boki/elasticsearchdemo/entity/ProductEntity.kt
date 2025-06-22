package boki.elasticsearchdemo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "products")
@Entity
class ProductEntity(
    val name: String,

    @Column(columnDefinition = "TEXT")
    val description: String,

    val price: Int,

    val rating: Double,

    val category: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)