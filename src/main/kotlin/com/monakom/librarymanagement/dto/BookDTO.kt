package com.monakom.librarymanagement.dto

import com.monakom.librarymanagement.entity.BookEntity
import com.monakom.librarymanagement.entity.BookStatus

class BookDTO {
}

data class CreateBookRequestDTO(
    val title: String?,
    val author: String?,
    val isbn: String?,
    val status: BookStatus?
)

data class BookResponseDTO(
    val id: Long?,
    val title: String,
    val author: String,
    val isbn: String,
    val status: BookStatus
)



