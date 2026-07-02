package com.monakom.librarymanagement.service

import com.monakom.librarymanagement.dto.BookResponseDTO
import com.monakom.librarymanagement.dto.CreateBookRequestDTO
import com.monakom.librarymanagement.entity.BookEntity
import com.monakom.librarymanagement.entity.BookStatus
import org.springframework.data.domain.Page

interface BookService {

    fun getAllBooks(): List<BookEntity>

    fun getAllBooksWithPage(page: Int, size: Int, status: BookStatus?, sort: String?): Page<BookEntity>

    fun findByID(id: Long): BookEntity?

    fun createBook(request: CreateBookRequestDTO): BookEntity

    fun updateBook(id: Long, request: CreateBookRequestDTO): BookResponseDTO

    fun deleteBook(id: Long)
}