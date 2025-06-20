package boki.elasticsearchdemo.dto

data class CreateUserRequest(
    val id: String,
    val name: String,
    val age: Long,
    val isActive: Boolean,
)

data class UpdateUserRequest(
    val name: String,
    val age: Long,
    val isActive: Boolean,
)