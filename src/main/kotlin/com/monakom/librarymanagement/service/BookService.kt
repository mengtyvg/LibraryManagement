package com.monakom.librarymanagement.service

import com.monakom.librarymanagement.dto.BookResponseDTO
import com.monakom.librarymanagement.dto.CreateBookRequestDTO
import com.monakom.librarymanagement.entity.BookEntity
import com.monakom.librarymanagement.entity.BookStatus
import com.monakom.librarymanagement.repository.BookRepository
import org.hibernate.engine.jdbc.Size
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort


@Service
class BookService(
    val bookRepository: BookRepository) {

    fun getAllBooks(): List<BookEntity> {
        return bookRepository.findAll()
    }

    fun getAllBooksWithPage(page: Int, size: Int, status: BookStatus?, sort: String?): Page<BookEntity> {

        val direction = if (sort?.lowercase() == "asc") {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }

        val pagable = PageRequest.of(
            page,
            size,
            Sort.by(direction, "createdAt"))

        if (status != null) {
            return bookRepository.findByStatus(status, pagable)
        }else{
            return bookRepository.findAll(pagable)
        }


    }

//    fun getBookByStatus(status: BookStatus): List<BookEntity> {
//        return bookRepository.findByStatus(status)
//    }

    fun findByID(id: Long): BookEntity? {
        return bookRepository.findById(id).orElse(null)
    }



    fun createBook(request: CreateBookRequestDTO): BookEntity {
        val title = request.title?.trim()
        val author = request.author?.trim()
        val isbn = request.isbn?.trim()
        val status = request.status

        if(title.isNullOrBlank()){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be empty")
        }

        if(author.isNullOrBlank()){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Author cannot be empty")
        }

        if(isbn.isNullOrBlank()){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Issue cannot be empty")
        }

        if(status == null){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Status cannot be empty")
        }

        if(bookRepository.existsByIsbn(isbn)){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Book with this ISBN already exists")
        }

        val book = BookEntity(
            title = title,
            author = author,
            isbn = isbn,
            status = status,
        )

        return bookRepository.save(book)
    }

    fun updateBook(id: Long, request: CreateBookRequestDTO): BookResponseDTO {
        val book = bookRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
            }

        val title = request.title?.trim()
        val author = request.author?.trim()
        val isbn = request.isbn?.trim()
        val status = request.status

        if (title.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be empty")
        }

        if (author.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Author cannot be empty")
        }

        if (isbn.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN cannot be empty")
        }

        if (status == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Status cannot be empty")
        }

        book.title = title
        book.author = author
        book.isbn = isbn
        book.status = status

        val updatedBook = bookRepository.save(book)

        return BookResponseDTO(
            id = updatedBook.id,
            title = updatedBook.title,
            author = updatedBook.author,
            isbn = updatedBook.isbn,
            status = updatedBook.status
        )
    }


    fun deleteBook(id: Long) {
        if(!bookRepository.existsById(id)){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        }

        return bookRepository.deleteById(id)

    }



}
