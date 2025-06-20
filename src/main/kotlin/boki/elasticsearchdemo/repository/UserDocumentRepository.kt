package boki.elasticsearchdemo.repository

import boki.elasticsearchdemo.document.UserDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDocumentRepository: ElasticsearchRepository<UserDocument, String>