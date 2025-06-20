package boki.elasticsearchdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ElasticSearchDemoApplication

fun main(args: Array<String>) {
    runApplication<ElasticSearchDemoApplication>(*args)
}
