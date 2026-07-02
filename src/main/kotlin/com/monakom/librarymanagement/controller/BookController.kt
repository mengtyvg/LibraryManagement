package com.monakom.librarymanagement.controller

import org.springframework.data.domain.Page

import com.monakom.librarymanagement.dto.BookResponseDTO
import com.monakom.librarymanagement.dto.CreateBookRequestDTO
import com.monakom.librarymanagement.entity.BookEntity
import com.monakom.librarymanagement.entity.BookStatus
import com.monakom.librarymanagement.service.BookService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {

    @GetMapping("/all")
    fun getAllBooks(): List<BookEntity> {
        return bookService.getAllBooks()
    }

    @GetMapping("/page")
    fun getAllBooks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
        @RequestParam(required = false) status: BookStatus?,
        @RequestParam(required = false) sort: String?
    ): Page<BookEntity> {
        return bookService.getAllBooksWithPage(page, size, status, sort)
    }




    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): BookEntity? {
        return bookService.findByID(id)
    }

//    @GetMapping
//    fun getBookByStatus(@RequestParam status: BookStatus): List<BookEntity> {
//        return bookService.getBookByStatus(status)
//    }

    @PostMapping("/create")
    fun createBook(@RequestBody createBookRequestDTO: CreateBookRequestDTO): BookEntity {
        return bookService.createBook(createBookRequestDTO)
    }

    @PutMapping("/update/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @RequestBody request: CreateBookRequestDTO
    ): BookResponseDTO {
        return bookService.updateBook(id, request)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteBook(@PathVariable("id") id: Long): String {
        bookService.deleteBook(id)
        return "book successfully deleted"
    }



}