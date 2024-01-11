package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.jvm.optionals.toList

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@RestController
class MessageController(val service: MessageService) {

	@GetMapping("/index")
	fun indexExample(@RequestParam("name") name: String) = "Hello, $name"

	@GetMapping("/messagesExample")
	fun messagesExample() = listOf(
		Message("1", "Hello!"),
		Message("2", "Bonjour!"),
		Message("3", "Privet!"),
	)

	@GetMapping("/")
	fun index(): List<Message> = service.findMessages()

	@GetMapping("/{id}")
	fun index(@PathVariable id: String): List<Message> = service.findMessageById(id)

	@PostMapping("/")
	fun post(@RequestBody message: Message) {
		service.save(message)
	}

}

@Service
class MessageService(val db: MessageRepository) {

	fun findMessages(): List<Message> = db.findAll().toList()

	fun findMessageById(id: String): List<Message> = db.findById(id).toList()

	fun save(message: Message) {
		db.save(message)
	}

	fun <T: Any> Optional<out T>.toList(): List<T> =
		if (isPresent) listOf(get()) else emptyList()

}

@Table("MESSAGES")
data class Message(@Id var id: String?, val text: String)

interface MessageRepository : CrudRepository<Message, String>


