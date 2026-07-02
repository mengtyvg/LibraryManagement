package com.monakom.librarymanagement.service

import com.monakom.librarymanagement.dto.BookResponseDTO
import com.monakom.librarymanagement.dto.CreateBookRequestDTO
import com.monakom.librarymanagement.entity.BookEntity
import com.monakom.librarymanagement.entity.BookStatus
import com.monakom.librarymanagement.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class BookServiceImpl(
    val bookRepository: BookRepository
) : BookService {

    override fun getAllBooks(): List<BookEntity> {
        return bookRepository.findAll()
    }

    override fun getAllBooksWithPage(page: Int, size: Int, status: BookStatus?, sort: String?): Page<BookEntity> {

        val direction = if (sort?.lowercase() == "asc") {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }

        val pagable = PageRequest.of(
            page,
            size,
            Sort.by(direction, "createdAt")
        )

        return if (status != null) {
            bookRepository.findByStatus(status, pagable)
        } else {
            bookRepository.findAll(pagable)
        }
    }

    override fun findByID(id: Long): BookEntity? {
        return bookRepository.findById(id).orElse(null)
    }

    override fun createBook(request: CreateBookRequestDTO): BookEntity {
        val title = request.title?.trim()
        val author = request.author?.trim()
        val isbn = request.isbn?.trim()

        if (bookRepository.existsByIsbn(isbn)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Book with this ISBN already exists")
        }

        val book = BookEntity(
            title = title,
            author = author,
            isbn = isbn,


        )

        return bookRepository.save(book)
    }

    override fun updateBook(id: Long, request: CreateBookRequestDTO): BookResponseDTO {
        val book = bookRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
            }

        val title = request.title?.trim()
        val author = request.author?.trim()
        val isbn = request.isbn?.trim()


        if (bookRepository.existsByIsbnAndIdNot(isbn, id)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Book with this ISBN already exists"
            )
        }

        book.title = title
        book.author = author
        book.isbn = isbn

        val updatedBook = bookRepository.save(book)

        val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm a", Locale.ENGLISH)


        return BookResponseDTO(
            id = updatedBook.id,
            title = updatedBook.title,
            author = updatedBook.author,
            isbn = updatedBook.isbn,
            bookStatus = BookStatus.valueOf(updatedBook.bookStatus ?: BookStatus.NEW.name),
            updatedDate = formatter.format(updatedBook.createdAt)
        )
    }

    override fun deleteBook(id: Long) {
        if (!bookRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        }

        return bookRepository.deleteById(id)
    }
}