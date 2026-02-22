package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

fun addBook(service: LibraryService) {
    println("--- Add New Book ---")
    print("ISBN: ")
    val isbn = readln().trim()
    print("Title: ")
    val title = readln().trim()
    print("Author: ")
    val author = readln().trim()
    print("Year: ")
    val year = readln().trim().toIntOrNull() ?: 0

    val book = Book(isbn, title, author, year)
    if (service.addBook(book)) {
        println("Book added successfully!")
    } else {
        println("Error: Book with this ISBN already exists.")
    }
}

fun registerPatron(repository: LibraryRepository) {
    println("--- Register Patron ---")
    print("Patron ID: ")
    val id = readln().trim()
    print("Name: ")
    val name = readln().trim()

    val patron = Patron(id, name)
    if (repository.addPatron(patron)) {
        println("Patron registered successfully!")
    } else {
        println("Error: Patron with this ID already exists.")
    }
}

fun borrowBook(service: LibraryService) {
    println("--- Borrow Book ---")
    print("Patron ID: ")
    val patronId = readln().trim()
    print("Book ISBN: ")
    val isbn = readln().trim()

    val result = service.borrowBook(patronId, isbn)
    when (result) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully!")
        BorrowResult.BOOK_NOT_FOUND -> println("Error: Book not found.")
        BorrowResult.PATRON_NOT_FOUND -> println("Error: Patron not found.")
        BorrowResult.NOT_AVAILABLE -> println("Error: Book is not available.")
        BorrowResult.LIMIT_REACHED -> println("Error: Patron has reached the borrow limit (3 books).")
    }
}

fun returnBook(service: LibraryService) {
    println("--- Return Book ---")
    print("Patron ID: ")
    val patronId = readln().trim()
    print("Book ISBN: ")
    val isbn = readln().trim()

    if (service.returnBook(patronId, isbn)) {
        println("Book returned successfully!")
    } else {
        println("Error: Return failed. Check IDs or if the book was actually borrowed by this patron.")
    }
}

fun search(service: LibraryService) {
    print("Enter search query (title or author): ")
    val query = readln().trim()
    val results = service.search(query)

    if (results.isEmpty()) {
        println("No books found matching '$query'.")
    } else {
        println("--- Search Results ---")
        results.forEach { println("${it.isbn}: ${it.title} by ${it.author} (${if (it.isAvailable) "Available" else "Borrowed"})") }
    }
}

fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()
    if (books.isEmpty()) {
        println("The library is empty.")
    } else {
        println("--- Library Catalog ---")
        books.forEach { println("${it.isbn}: ${it.title} by ${it.author} [${if (it.isAvailable) "Available" else "Borrowed"}]") }
    }
}
