package com.monakom.librarymanagement.repository

import com.monakom.librarymanagement.entity.BookEntity
import com.monakom.librarymanagement.entity.BookStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


@Repository
interface BookRepository : JpaRepository<BookEntity, Long> {

    fun existsByIsbn(isbn: String?): Boolean
    //fun existsByIdAndIsbn(isbn: String, id: Long): Boolean
    //fun findByStatus(status: BookStatus): List<BookEntity>
    fun existsByIsbnAndIdNot(isbn: String?, id: Long): Boolean
    fun findByStatus(status: BookStatus, pageable: Pageable): Page<BookEntity>

}
