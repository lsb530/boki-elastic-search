package boki.elasticsearchdemo.controller

import boki.elasticsearchdemo.document.UserDocument
import boki.elasticsearchdemo.dto.CreateUserRequest
import boki.elasticsearchdemo.dto.UpdateUserRequest
import boki.elasticsearchdemo.repository.UserDocumentRepository
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserController(
    private val userDocumentRepository: UserDocumentRepository
) {

    @PostMapping
    fun createUser(
        @RequestBody request: CreateUserRequest,
    ): ResponseEntity<Any> {
        val newUser = UserDocument(
            id = request.id,
            name = request.name,
            age = request.age,
            isActive = true,
        )
        val savedUser = userDocumentRepository.save(newUser)
        return ResponseEntity.ok().body(savedUser)
    }

    @GetMapping
    fun findUsers(): ResponseEntity<Any> {
        val findUsers = userDocumentRepository.findAll(
            PageRequest.of(0, 10)
        )
        return ResponseEntity.ok().body(findUsers)
    }

    @GetMapping("/{id}")
    fun findUser(@PathVariable id: String): ResponseEntity<Any> {
        val findUser = userDocumentRepository.findById(id)
            .orElseThrow { RuntimeException("존재하지 않는 사용자입니다 : $id") }
        return ResponseEntity.ok().body(findUser)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: String,
        @RequestBody request: UpdateUserRequest,
    ): ResponseEntity<Any> {
        val findUser = userDocumentRepository.findById(id)
            .orElseThrow { RuntimeException("존재하지 않는 사용자입니다 : $id") }
        findUser.age = request.age
        findUser.name = request.name
        findUser.isActive = request.isActive
        return ResponseEntity.ok().body(userDocumentRepository.save(findUser))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: String,
    ): ResponseEntity<Any> {
        val findUser = userDocumentRepository.findById(id)
            .orElseThrow { RuntimeException("존재하지 않는 사용자입니다 : $id") }
        userDocumentRepository.delete(findUser)
        return ResponseEntity.noContent().build()
    }

}