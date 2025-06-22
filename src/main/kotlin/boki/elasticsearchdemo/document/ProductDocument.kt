package boki.elasticsearchdemo.document

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.*

@Document(indexName = "products")
@Setting(settingPath = "/elasticsearch/settings/products.json")
class ProductDocument(
    @Id
    val id: String,

    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "products_name_analyzer"),
        otherFields = [
            InnerField(
                suffix = "auto_complete",
                type = FieldType.Search_As_You_Type,
                analyzer = "nori"
            )
        ]
    )
    val name: String,

    @Field(type = FieldType.Text, analyzer = "products_description_analyzer")
    val description: String,

    @Field(type = FieldType.Integer)
    val price: Int,

    @Field(type = FieldType.Double)
    val rating: Double,

    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "products_category_analyzer"),
        otherFields = [
            InnerField(
                suffix = "raw",
                type = FieldType.Keyword,
            )
        ]
    )
    val category: String,
)