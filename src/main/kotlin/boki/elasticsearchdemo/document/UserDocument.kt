package boki.elasticsearchdemo.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "users") // users 인덱스의 Document 명시
class UserDocument(
    @Field(type = FieldType.Keyword)  // 매핑 정의 : Keyword 타입
    var name: String,

    @Field(type = FieldType.Long)    // 매핑 정의 : Long 타입
    var age: Long,

    @Field(type = FieldType.Boolean) // 매핑 정의 : Boolean 타입
    var isActive: Boolean,

    @Id
    val id: String? = null           // Elasticsearch에서는 Document ID를 문자열(String)으로 다룸
) {

    fun updateActivate() {
        this.isActive = true
    }

    fun updateDeactivate() {
        this.isActive = false
    }

}