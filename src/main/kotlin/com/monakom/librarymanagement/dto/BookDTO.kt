package com.monakom.librarymanagement.dto

import com.monakom.librarymanagement.entity.BookStatus
import jakarta.validation.constraints.NotBlank


//class BookDTO {
//}

data class CreateBookRequestDTO(
    @field:NotBlank(message = "Title cannot be empty")
    val title: String? = null,

    @field:NotBlank(message = "Author cannot be empty")
    val author: String? = null,

    @field:NotBlank(message = "ISBN cannot be empty")
    val isbn: String? = null,
    val bookStatus: BookStatus? = null
)

data class BookResponseDTO(
    val id: Long?,
    val title: String?,
    val author: String?,
    val isbn: String?,
    val bookStatus: BookStatus?,
    val updatedDate: String?
)



