package com.auca.main;

import com.auca.interfaces.*;
import com.auca.abstracts.*;
import com.auca.student.Student;
import com.auca.teacher.Teacher;
import com.auca.administrator.Administrator;
import com.auca.department.Department;
import com.auca.course.Course;
import com.auca.classroom.Classroom;
import com.auca.enrollment.Enrollment;
import com.auca.assessment.Assessment;
import com.auca.feepayment.FeePayment;
import com.auca.librarybook.LibraryBook;
import com.auca.bookloan.BookLoan;
import com.auca.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> feePayments = new ArrayList<>();
    private static final List<LibraryBook> libraryBooks = new ArrayList<>();
    private static final List<BookLoan> bookLoans = new ArrayList<>();

    private static class FormCancelledException extends RuntimeException {}
    private static class FormBackException extends RuntimeException {}

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> registerDepartment();
                    case "2" -> registerStudent();
                    case "3" -> registerTeacher();
                    case "4" -> registerAdministrator();
                    case "5" -> registerCourse();
                    case "6" -> registerClassroom();
                    case "7" -> enrollStudent();
                    case "8" -> recordAssessment();
                    case "9" -> recordFeePayment();
                    case "10" -> registerLibraryBook();
                    case "11" -> borrowLibraryBook();
                    case "12" -> returnLibraryBook();
                    case "13" -> viewAllRecords();
                    case "14" -> searchRecordById();
                    case "15" -> updateExistingRecord();
                    case "16" -> showStudentAcademicSummary();
                    case "17" -> showStudentPaymentHistory();
                    case "18" -> showAvailableBooks();
                    case "19" -> demonstratePolymorphism();
                    case "0" -> {
                        System.out.println("Exiting AUCA_ID School Management System. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("! Invalid selection. Please enter a number between 0 and 19.");
                }
            } catch (FormCancelledException e) {
                System.out.println("! Operation cancelled. Returning to main menu without saving.");
            } catch (Exception e) {
                System.out.println("! Error: " + e.getMessage());
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n==============================================");
        System.out.println("       AUCA_ID SCHOOL MANAGEMENT SYSTEM       ");
        System.out.println("==============================================");
        System.out.println("1.  Register Department     11. Borrow Library Book");
        System.out.println("2.  Register Student        12. Return Library Book");
        System.out.println("3.  Register Teacher        13. View All Records");
        System.out.println("4.  Register Administrator  14. Search Record by ID");
        System.out.println("5.  Register Course         15. Update Existing Record");
        System.out.println("6.  Register Classroom      16. Student Academic Summary");
        System.out.println("7.  Enroll Student          17. Student Payment History");
        System.out.println("8.  Record Assessment       18. View Available Books");
        System.out.println("9.  Record Fee Payment      19. Demonstrate Polymorphism");
        System.out.println("10. Register Library Book   0.  Exit");
        System.out.println("==============================================");
        System.out.print("Select an option: ");
    }

    // --- HELPER INPUT METHODS ---
    private static String promptText(String label, boolean allowBack) {
        while (true) {
            System.out.print(label + (allowBack ? " [BACK/CANCEL]: " : " [CANCEL]: "));
            String input = scanner.nextLine().trim();
            if ("CANCEL".equalsIgnoreCase(input)) throw new FormCancelledException();
            if (allowBack && "BACK".equalsIgnoreCase(input)) throw new FormBackException();
            if (input.isEmpty()) {
                System.out.println("! Input cannot be blank.");
                continue;
            }
            return input;
        }
    }

    private static String promptUniqueId(String label, boolean allowBack) {
        while (true) {
            String id = promptText(label, allowBack);
            if (id.length() < 4 || id.length() > 30) {
                System.out.println("! ID must be between 4 and 30 characters.");
                continue;
            }
            if (isIdExists(id)) {
                System.out.println("! ID '" + id + "' already exists. Please enter a unique ID.");
                continue;
            }
            return id;
        }
    }

    private static LocalDate promptDate(String label, boolean allowFuture, boolean allowBack) {
        while (true) {
            String input = promptText(label + " (YYYY-MM-DD)", allowBack);
            try {
                LocalDate date = LocalDate.parse(input);
                if (!allowFuture && date.isAfter(LocalDate.now())) {
                    System.out.println("! Date cannot be in the future.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("! Invalid date format. Use YYYY-MM-DD (e.g., 2004-05-16).");
            }
        }
    }

    private static int promptInt(String label, int min, int max, boolean allowBack) {
        while (true) {
            String input = promptText(label + " (" + min + "-" + max + ")", allowBack);
            try {
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    System.out.println("! Value must be between " + min + " and " + max + ".");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("! Invalid number. Enter an integer.");
            }
        }
    }

    private static double promptDouble(String label, double min, double max, boolean allowBack) {
        while (true) {
            String input = promptText(label, allowBack);
            try {
                double val = Double.parseDouble(input);
                if (val < min || val > max) {
                    System.out.println("! Value must be between " + min + " and " + max + ".");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("! Invalid decimal number.");
            }
        }
    }

    private static BigDecimal promptBigDecimal(String label, boolean allowBack) {
        while (true) {
            String input = promptText(label, allowBack);
            try {
                BigDecimal val = new BigDecimal(input);
                if (val.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("! Amount must be strictly positive.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("! Invalid monetary amount.");
            }
        }
    }

    private static boolean isIdExists(String id) {
        for (Identifiable item : getAllIdentifiables()) {
            if (item.getId().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private static List<Identifiable> getAllIdentifiables() {
        List<Identifiable> all = new ArrayList<>();
        all.addAll(departments);
        all.addAll(students);
        all.addAll(teachers);
        all.addAll(administrators);
        all.addAll(courses);
        all.addAll(classrooms);
        all.addAll(enrollments);
        all.addAll(assessments);
        all.addAll(feePayments);
        all.addAll(libraryBooks);
        all.addAll(bookLoans);
        return all;
    }

    // --- 1. REGISTER DEPARTMENT ---
    private static Department registerDepartment() {
        System.out.println("\n--- REGISTER DEPARTMENT ---");
        String id = promptUniqueId("01 Department ID (4-30 chars)", false);
        String name = promptText("02 Department Name (2-60 chars)", false);
        String code = promptText("03 Department Code", false);
        String office = promptText("04 Office Location", false);
        String email = promptText("05 Contact Email", false);
        Teacher head = null;
        if (!teachers.isEmpty()) {
            System.out.println("Select Head Teacher (0 to skip):");
            for (int i = 0; i < teachers.size(); i++) {
                System.out.println((i + 1) + ". " + teachers.get(i).getRegistrationSummary());
            }
            int sel = promptInt("Choice", 0, teachers.size(), false);
            if (sel > 0) head = teachers.get(sel - 1);
        }
        Department dept = new Department(id, name, code, office, email, head);
        try {
            dept.validate();
            departments.add(dept);
            System.out.println("+ Department " + id + " registered successfully.");
            return dept;
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
            return null;
        }
    }

    // --- 2. REGISTER STUDENT (4-STEP MULTI-STEP SMART FORM) ---
    private static Student registerStudent() {
        if (departments.isEmpty()) {
            System.out.println("! No Department exists. Register a Department before registering a Student.");
            System.out.print("1. Register Department now | 0. Cancel: ");
            if ("1".equals(scanner.nextLine().trim())) {
                if (registerDepartment() == null) return null;
            } else {
                return null;
            }
        }

        String personId = "", firstName = "", lastName = "", email = "", phone = "";
        LocalDate dob = null;
        String memberNo = "", status = "ACTIVE", instEmail = "", advisor = "";
        Department dept = null;
        LocalDate joinDate = null;
        String studentNo = "", program = "", guardianPhone = "";
        LocalDate admissionDate = null;
        int studyYear = 1;
        boolean scholarship = false;

        int step = 1;
        while (step <= 4) {
            try {
                if (step == 1) {
                    System.out.println("\n[STEP 1/4] PERSONAL DETAILS");
                    personId = promptUniqueId("01 Person ID (4-30 chars)", false);
                    firstName = promptText("02 First Name (2-60 chars)", true);
                    lastName = promptText("03 Last Name (2-60 chars)", true);
                    email = promptText("04 Email", true);
                    phone = promptText("05 Phone (10-15 digits)", true);
                    dob = promptDate("06 Date of Birth", false, true);
                    step++;
                } else if (step == 2) {
                    System.out.println("\n[STEP 2/4] ACADEMIC DETAILS");
                    memberNo = promptUniqueId("07 Member Number (4-30 chars)", true);
                    dept = selectDepartment(true);
                    joinDate = promptDate("09 Join Date", false, true);
                    int stChoice = promptInt("10 Academic Status (1 ACTIVE / 2 INACTIVE)", 1, 2, true);
                    status = (stChoice == 1) ? "ACTIVE" : "INACTIVE";
                    instEmail = promptText("11 Institutional Email", true);
                    advisor = promptText("12 Advisor Name", true);
                    step++;
                } else if (step == 3) {
                    System.out.println("\n[STEP 3/4] STUDENT DETAILS");
                    studentNo = promptUniqueId("13 Student Number (4-30 chars)", true);
                    program = promptText("14 Program Name", true);
                    admissionDate = promptDate("15 Admission Date", false, true);
                    studyYear = promptInt("16 Study Year", 1, 6, true);
                    guardianPhone = promptText("17 Guardian Phone (10-15 digits)", true);
                    int sch = promptInt("18 Scholarship (1 YES / 2 NO)", 1, 2, true);
                    scholarship = (sch == 1);
                    step++;
                } else if (step == 4) {
                    Student candidate = new Student(personId, firstName, lastName, email, phone, dob,
                            memberNo, dept, joinDate, status, instEmail, advisor,
                            studentNo, program, admissionDate, studyYear, guardianPhone, scholarship);
                    System.out.println("\n[STEP 4/4] REVIEW STUDENT");
                    candidate.printDetails();
                    System.out.print("[1] Save student  [2] Restart form  [0] Cancel: ");
                    String act = scanner.nextLine().trim();
                    if ("1".equals(act)) {
                        candidate.validate();
                        students.add(candidate);
                        System.out.println("+ Student " + studentNo + " registered successfully!");
                        return candidate;
                    } else if ("2".equals(act)) {
                        step = 1;
                    } else if ("0".equals(act)) {
                        throw new FormCancelledException();
                    }
                }
            } catch (FormBackException e) {
                if (step > 1) step--;
            } catch (ValidationException e) {
                System.out.println("! Validation error: " + e.getMessage());
                step = 1;
            }
        }
        return null;
    }

    private static Department selectDepartment(boolean allowBack) {
        while (true) {
            System.out.println("08 Choose Department:");
            for (int i = 0; i < departments.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + departments.get(i).getRegistrationSummary());
            }
            System.out.println("  " + (departments.size() + 1) + ". [+] Register a new Department");
            int choice = promptInt("Select option", 1, departments.size() + 1, allowBack);
            if (choice <= departments.size()) {
                return departments.get(choice - 1);
            } else {
                Department created = registerDepartment();
                if (created != null) return created;
            }
        }
    }

    // --- 3. REGISTER TEACHER ---
    private static Teacher registerTeacher() {
        if (departments.isEmpty()) {
            System.out.println("! No Department exists. Register a Department first.");
            if (registerDepartment() == null) return null;
        }
        System.out.println("\n--- REGISTER TEACHER ---");
        String personId = promptUniqueId("Person ID", false);
        String firstName = promptText("First Name", false);
        String lastName = promptText("Last Name", false);
        String email = promptText("Email", false);
        String phone = promptText("Phone", false);
        LocalDate dob = promptDate("Date of Birth", false, false);
        String memberNo = promptUniqueId("Member Number", false);
        Department dept = selectDepartment(false);
        LocalDate joinDate = promptDate("Join Date", false, false);
        String instEmail = promptText("Institutional Email", false);
        String advisor = promptText("Supervisor/Advisor Name", false);
        String empNo = promptUniqueId("Employee Number", false);
        String spec = promptText("Specialization", false);
        LocalDate hireDate = promptDate("Hire Date", false, false);
        BigDecimal salary = promptBigDecimal("Salary", false);
        String office = promptText("Office Number", false);
        String empType = promptText("Employment Type (FULL_TIME/PART_TIME/VISITING)", false).toUpperCase();

        Teacher t = new Teacher(personId, firstName, lastName, email, phone, dob,
                memberNo, dept, joinDate, "ACTIVE", instEmail, advisor,
                empNo, spec, hireDate, salary, office, empType);
        try {
            t.validate();
            teachers.add(t);
            System.out.println("+ Teacher " + empNo + " registered successfully.");
            return t;
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
            return null;
        }
    }

    // --- 4. REGISTER ADMINISTRATOR ---
    private static void registerAdministrator() {
        System.out.println("\n--- REGISTER ADMINISTRATOR ---");
        String personId = promptUniqueId("Person ID", false);
        String firstName = promptText("First Name", false);
        String lastName = promptText("Last Name", false);
        String email = promptText("Email", false);
        String phone = promptText("Phone", false);
        LocalDate dob = promptDate("Date of Birth", false, false);
        String staffNo = promptUniqueId("Staff Number", false);
        String title = promptText("Job Title", false);
        String office = promptText("Assigned Office", false);
        LocalDate empDate = promptDate("Employment Date", false, false);
        int access = promptInt("Access Level", 1, 5, false);

        Administrator admin = new Administrator(personId, firstName, lastName, email, phone, dob,
                staffNo, title, office, empDate, access, true);
        try {
            admin.validate();
            administrators.add(admin);
            System.out.println("+ Administrator " + staffNo + " registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    // --- 5. REGISTER COURSE ---
    private static Course registerCourse() {
        if (departments.isEmpty()) {
            System.out.println("! No Department exists. Register Department first.");
            if (registerDepartment() == null) return null;
        }
        if (teachers.isEmpty()) {
            System.out.println("! No Teacher exists. Register Teacher first.");
            if (registerTeacher() == null) return null;
        }
        System.out.println("\n--- REGISTER COURSE ---");
        String id = promptUniqueId("Course ID", false);
        String code = promptText("Course Code", false);
        String title = promptText("Course Title", false);
        int credits = promptInt("Credit Hours", 1, 6, false);
        Department dept = selectDepartment(false);

        System.out.println("Select Assigned Teacher:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i).getRegistrationSummary());
        }
        Teacher teacher = teachers.get(promptInt("Choice", 1, teachers.size(), false) - 1);

        Course c = new Course(id, code, title, credits, dept, teacher);
        try {
            c.validate();
            courses.add(c);
            System.out.println("+ Course " + code + " registered successfully.");
            return c;
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
            return null;
        }
    }

    // --- 6. REGISTER CLASSROOM ---
    private static void registerClassroom() {
        System.out.println("\n--- REGISTER CLASSROOM ---");
        String id = promptUniqueId("Classroom ID", false);
        String building = promptText("Building Name", false);
        String room = promptText("Room Number", false);
        int cap = promptInt("Capacity", 1, 1000, false);
        int floor = promptInt("Floor Number", 0, 50, false);

        Classroom cl = new Classroom(id, building, room, cap, floor, true);
        try {
            cl.validate();
            classrooms.add(cl);
            System.out.println("+ Classroom " + id + " registered successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    // --- 7. ENROLL STUDENT ---
    private static void enrollStudent() {
        if (students.isEmpty()) {
            System.out.println("! No Student exists. Register a Student first.");
            if (registerStudent() == null) return;
        }
        if (courses.isEmpty()) {
            System.out.println("! No Course exists. Register a Course first.");
            if (registerCourse() == null) return;
        }
        System.out.println("\n--- ENROLL STUDENT ---");
        String id = promptUniqueId("Enrollment ID", false);
        System.out.println("Select Student:");
        for (int i = 0; i < students.size(); i++) System.out.println((i + 1) + ". " + students.get(i).getRegistrationSummary());
        Student st = students.get(promptInt("Choice", 1, students.size(), false) - 1);

        System.out.println("Select Course:");
        for (int i = 0; i < courses.size(); i++) System.out.println((i + 1) + ". " + courses.get(i).getRegistrationSummary());
        Course co = courses.get(promptInt("Choice", 1, courses.size(), false) - 1);

        String semester = promptText("Semester (e.g., 2026-SEM1)", false);
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(st) && e.getCourse().equals(co) && e.getSemester().equalsIgnoreCase(semester) && e.isActive()) {
                System.out.println("! Duplicate active enrollment detected for this student, course, and semester.");
                return;
            }
        }
        LocalDate date = promptDate("Enrollment Date", false, false);
        Enrollment enr = new Enrollment(id, st, co, date, semester, "ACTIVE");
        try {
            enr.validate();
            enrollments.add(enr);
            System.out.println("+ Enrollment " + id + " saved successfully.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    // --- 8. RECORD ASSESSMENT ---
    private static void recordAssessment() {
        List<Enrollment> activeEnr = new ArrayList<>();
        for (Enrollment e : enrollments) if (e.isActive()) activeEnr.add(e);
        if (activeEnr.isEmpty()) {
            System.out.println("! No active enrollments found. Enroll a student first.");
            return;
        }
        System.out.println("\n--- RECORD ASSESSMENT ---");
        String id = promptUniqueId("Assessment ID", false);
        for (int i = 0; i < activeEnr.size(); i++) System.out.println((i + 1) + ". " + activeEnr.get(i).getRegistrationSummary());
        Enrollment enr = activeEnr.get(promptInt("Select Active Enrollment", 1, activeEnr.size(), false) - 1);
        String type = promptText("Assessment Type (QUIZ/MIDTERM/FINAL/ASSIGNMENT)", false).toUpperCase();
        double max = promptDouble("Maximum Score", 1, 1000, false);
        double obt = promptDouble("Obtained Score", 0, max, false);
        LocalDate date = promptDate("Assessment Date", false, false);

        Assessment a = new Assessment(id, enr, type, max, obt, date);
        try {
            a.validate();
            assessments.add(a);
            System.out.println("+ Assessment " + id + " recorded.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    // --- 9. RECORD FEE PAYMENT ---
    private static void recordFeePayment() {
        if (students.isEmpty()) {
            System.out.println("! No Student exists. Register a Student first.");
            if (registerStudent() == null) return;
        }
        System.out.println("\n--- RECORD FEE PAYMENT ---");
        String id = promptUniqueId("Payment ID", false);
        for (int i = 0; i < students.size(); i++) System.out.println((i + 1) + ". " + students.get(i).getRegistrationSummary());
        Student st = students.get(promptInt("Select Student", 1, students.size(), false) - 1);
        BigDecimal amt = promptBigDecimal("Amount", false);
        LocalDate date = promptDate("Payment Date", false, false);
        String method = promptText("Payment Method (CASH/BANK_TRANSFER/MOBILE_MONEY/CARD)", false).toUpperCase();
        String receipt = promptText("Receipt Number", false);
        for (FeePayment fp : feePayments) {
            if (fp.getReceiptNumber().equalsIgnoreCase(receipt)) {
                System.out.println("! Duplicate receipt number rejected.");
                return;
            }
        }
        FeePayment fp = new FeePayment(id, st, amt, date, method, receipt);
        try {
            fp.validate();
            feePayments.add(fp);
            System.out.println("+ Fee payment " + id + " recorded.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    // --- 10. REGISTER LIBRARY BOOK ---
    private static void registerLibraryBook() {
        System.out.println("\n--- REGISTER LIBRARY BOOK ---");
        String id = promptUniqueId("Book ID", false);
        String isbn = promptText("ISBN", false);
        String title = promptText("Title", false);
        String author = promptText("Author", false);
        int total = promptInt("Total Copies", 1, 10000, false);

        LibraryBook book = new LibraryBook(id, isbn, title, author, total, total);
        try {
            book.validate();
            libraryBooks.add(book);
            System.out.println("+ Book " + id + " added to library.");
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    // --- 11. BORROW LIBRARY BOOK ---
    private static void borrowLibraryBook() {
        if (students.isEmpty() || libraryBooks.isEmpty()) {
            System.out.println("! Both Students and LibraryBooks must exist before borrowing.");
            return;
        }
        System.out.println("\n--- BORROW LIBRARY BOOK ---");
        String id = promptUniqueId("Loan ID", false);
        for (int i = 0; i < students.size(); i++) System.out.println((i + 1) + ". " + students.get(i).getRegistrationSummary());
        Student st = students.get(promptInt("Select Student", 1, students.size(), false) - 1);

        for (int i = 0; i < libraryBooks.size(); i++) System.out.println((i + 1) + ". " + libraryBooks.get(i).getRegistrationSummary());
        LibraryBook book = libraryBooks.get(promptInt("Select Book", 1, libraryBooks.size(), false) - 1);

        if (book.getAvailableCopies() <= 0) {
            System.out.println("! BookUnavailableException: No copies available for '" + book.getTitle() + "'.");
            return;
        }
        LocalDate borrowDate = promptDate("Borrowed Date", false, false);
        LocalDate dueDate = promptDate("Due Date", true, false);
        BookLoan loan = new BookLoan(id, st, book, borrowDate, dueDate, null);
        try {
            loan.validate();
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookLoans.add(loan);
            System.out.println("+ Book borrowed successfully! Remaining stock: " + book.getAvailableCopies());
        } catch (ValidationException e) {
            System.out.println("! Validation failed: " + e.getMessage());
        }
    }

    // --- 12. RETURN LIBRARY BOOK ---
    private static void returnLibraryBook() {
        List<BookLoan> activeLoans = new ArrayList<>();
        for (BookLoan bl : bookLoans) if (bl.isActive()) activeLoans.add(bl);
        if (activeLoans.isEmpty()) {
            System.out.println("! No active book loans to return.");
            return;
        }
        System.out.println("\n--- RETURN LIBRARY BOOK ---");
        for (int i = 0; i < activeLoans.size(); i++) System.out.println((i + 1) + ". " + activeLoans.get(i).getRegistrationSummary());
        BookLoan loan = activeLoans.get(promptInt("Select Loan to Return", 1, activeLoans.size(), false) - 1);
        LocalDate retDate = promptDate("Return Date", false, false);
        if (retDate.isBefore(loan.getBorrowedDate())) {
            System.out.println("! Return date cannot be before borrowed date (" + loan.getBorrowedDate() + ").");
            return;
        }
        loan.setReturnedDate(retDate);
        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
        System.out.println("+ Book returned! Updated stock: " + loan.getBook().getAvailableCopies());
    }

    // --- 13. VIEW ALL RECORDS ---
    private static void viewAllRecords() {
        System.out.println("\n=== ALL SYSTEM RECORDS ===");
        List<Identifiable> all = getAllIdentifiables();
        if (all.isEmpty()) {
            System.out.println("No records stored yet.");
            return;
        }
        for (Identifiable item : all) {
            if (item instanceof Registrable r) {
                System.out.println(r.getRegistrationSummary());
            }
        }
    }

    // --- 14. SEARCH BY ID ---
    private static void searchRecordById() {
        String id = promptText("Enter ID to search", false);
        for (Identifiable item : getAllIdentifiables()) {
            if (item.getId().equalsIgnoreCase(id)) {
                if (item instanceof Registrable r) r.printDetails();
                return;
            }
        }
        System.out.println("! RecordNotFoundException: No record found with ID '" + id + "'.");
    }

    // --- 15. UPDATE EXISTING RECORD ---
    private static void updateExistingRecord() {
        String id = promptText("Enter ID of record to update", false);
        for (Student s : students) {
            if (s.getId().equalsIgnoreCase(id)) {
                s.printDetails();
                String newProg = promptText("Enter new Program Name", false);
                s.setProgramName(newProg);
                System.out.println("+ Student updated successfully.");
                return;
            }
        }
        for (Department d : departments) {
            if (d.getId().equalsIgnoreCase(id)) {
                d.printDetails();
                String newLoc = promptText("Enter new Office Location", false);
                d.setOfficeLocation(newLoc);
                System.out.println("+ Department updated successfully.");
                return;
            }
        }
        System.out.println("! Record not found or update handler not applicable for this ID.");
    }

    // --- 16. STUDENT ACADEMIC SUMMARY ---
    private static void showStudentAcademicSummary() {
        if (students.isEmpty()) { System.out.println("! No students registered."); return; }
        for (int i = 0; i < students.size(); i++) System.out.println((i + 1) + ". " + students.get(i).getRegistrationSummary());
        Student st = students.get(promptInt("Select Student", 1, students.size(), false) - 1);
        st.printDetails();
        System.out.println("--- ENROLLMENTS & ASSESSMENTS ---");
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(st)) {
                System.out.println("  * " + e.getRegistrationSummary());
                for (Assessment a : assessments) {
                    if (a.getEnrollment().equals(e)) {
                        System.out.println("      -> " + a.getRegistrationSummary());
                    }
                }
            }
        }
    }

    // --- 17. STUDENT PAYMENT HISTORY ---
    private static void showStudentPaymentHistory() {
        if (students.isEmpty()) { System.out.println("! No students registered."); return; }
        for (int i = 0; i < students.size(); i++) System.out.println((i + 1) + ". " + students.get(i).getRegistrationSummary());
        Student st = students.get(promptInt("Select Student", 1, students.size(), false) - 1);
        BigDecimal total = BigDecimal.ZERO;
        System.out.println("--- PAYMENT HISTORY FOR " + st.getDisplayName() + " ---");
        for (FeePayment fp : feePayments) {
            if (fp.getStudent().equals(st)) {
                System.out.println("  * " + fp.getRegistrationSummary());
                total = total.add(fp.getAmount());
            }
        }
        System.out.println("Total Paid: " + total);
    }

    // --- 18. VIEW AVAILABLE BOOKS ---
    private static void showAvailableBooks() {
        System.out.println("\n--- AVAILABLE LIBRARY BOOKS ---");
        boolean found = false;
        for (LibraryBook b : libraryBooks) {
            if (b.getAvailableCopies() > 0) {
                System.out.println("  * " + b.getRegistrationSummary());
                found = true;
            }
        }
        if (!found) System.out.println("No books currently available.");
    }

    // --- 19. DEMONSTRATE POLYMORPHISM ---
    private static void demonstratePolymorphism() {
        System.out.println("\n=== 1. SUBCLASS POLYMORPHISM (List<Person>) ===");
        List<Person> people = new ArrayList<>();
        people.addAll(students);
        people.addAll(teachers);
        people.addAll(administrators);
        if (people.isEmpty()) {
            System.out.println("Register at least one Student, Teacher, or Administrator first.");
        } else {
            for (Person p : people) {
                System.out.println("Person Ref -> ID: " + p.getId() + " | Name: " + p.getDisplayName() + " | Role: " + p.getRoleDescription());
            }
        }

        System.out.println("\n=== 2. INTERFACE POLYMORPHISM (List<Identifiable>) ===");
        List<Identifiable> entities = getAllIdentifiables();
        if (entities.isEmpty()) {
            System.out.println("No Identifiable entities registered yet.");
        } else {
            for (Identifiable idObj : entities) {
                System.out.println("Identifiable Ref -> Class: " + idObj.getClass().getSimpleName() + " | ID: " + idObj.getId() + " | Display: " + idObj.getDisplayName() + " | Active: " + idObj.isActive());
            }
        }
    }
}
