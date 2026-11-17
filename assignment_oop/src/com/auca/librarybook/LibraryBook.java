package com.auca.librarybook;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;

public class LibraryBook implements Identifiable, Registrable {
    private String bookId;
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public LibraryBook(String bookId, String isbn, String title, String author, int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    @Override
    public String getId() { return bookId; }

    @Override
    public String getDisplayName() { return title + " by " + author; }

    @Override
    public boolean isActive() { return availableCopies > 0; }

    @Override
    public void validate() throws ValidationException {
        if (bookId == null || bookId.trim().length() < MIN_ID_LENGTH || bookId.trim().length() > MAX_ID_LENGTH)
            throw new ValidationException("Book ID must be 4-30 characters.");
        if (isbn == null || isbn.trim().isEmpty())
            throw new ValidationException("ISBN is required.");
        if (title == null || title.trim().isEmpty())
            throw new ValidationException("Title is required.");
        if (author == null || author.trim().isEmpty())
            throw new ValidationException("Author is required.");
        if (totalCopies < 0 || availableCopies < 0 || availableCopies > totalCopies)
            throw new ValidationException("Invalid copy counts: 0 <= availableCopies <= totalCopies.");
    }

    @Override
    public void printDetails() {
        System.out.println("=========================================");
        System.out.println("LIBRARY BOOK: " + bookId + " | ISBN: " + isbn);
        System.out.println("Title: " + title + " | Author: " + author);
        System.out.println("Stock: " + availableCopies + " available / " + totalCopies + " total");
        System.out.println("=========================================");
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + bookId + "] "" + title + "" by " + author + " (" + availableCopies + "/" + totalCopies + " avail)";
    }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    @Override
    public String toString() { return getRegistrationSummary(); }
}
