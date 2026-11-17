package com.auca.bookloan;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import com.auca.librarybook.LibraryBook;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;

public class BookLoan implements Identifiable, Registrable {
    private String loanId;
    private Student student;
    private LibraryBook book;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;

    public BookLoan(String loanId, Student student, LibraryBook book, LocalDate borrowedDate, LocalDate dueDate, LocalDate returnedDate) {
        this.loanId = loanId;
        this.student = student;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    @Override
    public String getId() { return loanId; }

    @Override
    public String getDisplayName() { return book.getTitle() + " loaned to " + student.getDisplayName(); }

    @Override
    public boolean isActive() { return returnedDate == null; }

    @Override
    public void validate() throws ValidationException {
        if (loanId == null || loanId.trim().length() < MIN_ID_LENGTH || loanId.trim().length() > MAX_ID_LENGTH)
            throw new ValidationException("Loan ID must be 4-30 characters.");
        if (student == null)
            throw new ValidationException("Student is required.");
        if (book == null)
            throw new ValidationException("LibraryBook is required.");
        if (borrowedDate == null || borrowedDate.isAfter(LocalDate.now()))
            throw new ValidationException("Borrowed date cannot be in the future.");
        if (dueDate == null || !dueDate.isAfter(borrowedDate))
            throw new ValidationException("Due date must be after borrowed date.");
        if (returnedDate != null && returnedDate.isBefore(borrowedDate))
            throw new ValidationException("Returned date cannot be before borrowed date.");
    }

    @Override
    public void printDetails() {
        System.out.println("=========================================");
        System.out.println("BOOK LOAN: " + loanId + " | Status: " + (returnedDate == null ? "BORROWED" : "RETURNED on " + returnedDate));
        System.out.println("Student: " + student.getRegistrationSummary());
        System.out.println("Book: " + book.getRegistrationSummary());
        System.out.println("Borrowed: " + borrowedDate + " | Due: " + dueDate);
        System.out.println("=========================================");
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + loanId + "] " + book.getTitle() + " -> " + student.getStudentNumber() + " (Due: " + dueDate + ", " + (returnedDate == null ? "ACTIVE" : "RETURNED") + ")";
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public LibraryBook getBook() { return book; }
    public void setBook(LibraryBook book) { this.book = book; }
    public LocalDate getBorrowedDate() { return borrowedDate; }
    public void setBorrowedDate(LocalDate borrowedDate) { this.borrowedDate = borrowedDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnedDate() { return returnedDate; }
    public void setReturnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; }

    @Override
    public String toString() { return getRegistrationSummary(); }
}
