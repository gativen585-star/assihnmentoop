package com.auca.teacher;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Teacher extends AcademicMember {
    private String employeeNumber;
    private String specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber;
    private String employmentType;

    public Teacher(String personId, String firstName, String lastName, String email, String phoneNumber, LocalDate dateOfBirth,
                   String memberNumber, Department department, LocalDate joinDate, String academicStatus, String institutionalEmail, String advisorName,
                   String employeeNumber, String specialization, LocalDate hireDate, BigDecimal salary, String officeNumber, String employmentType) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth, memberNumber, department, joinDate, academicStatus, institutionalEmail, advisorName);
        this.employeeNumber = employeeNumber;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.salary = salary;
        this.officeNumber = officeNumber;
        this.employmentType = employmentType;
    }

    @Override
    public String getRoleDescription() { return "Faculty / Teacher (" + specialization + ")"; }

    @Override
    public String getAcademicCategory() { return "Academic Faculty (" + employmentType + ")"; }

    @Override
    public String getId() { return employeeNumber; }

    @Override
    public void validate() throws ValidationException {
        validateAcademicMember();
        if (employeeNumber == null || employeeNumber.trim().length() < MIN_ID_LENGTH || employeeNumber.trim().length() > MAX_ID_LENGTH)
            throw new ValidationException("Employee number must be 4-30 characters.");
        if (specialization == null || specialization.trim().isEmpty())
            throw new ValidationException("Specialization cannot be blank.");
        if (hireDate == null || hireDate.isAfter(LocalDate.now()))
            throw new ValidationException("Hire date cannot be in the future.");
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Salary must be strictly positive.");
        if (officeNumber == null || officeNumber.trim().isEmpty())
            throw new ValidationException("Office number cannot be blank.");
        if (employmentType == null || (!employmentType.equalsIgnoreCase("FULL_TIME") && !employmentType.equalsIgnoreCase("PART_TIME") && !employmentType.equalsIgnoreCase("VISITING")))
            throw new ValidationException("Employment type must be FULL_TIME, PART_TIME, or VISITING.");
    }

    @Override
    public void printDetails() {
        System.out.println("=========================================");
        System.out.println("TEACHER RECORD: " + employeeNumber);
        System.out.println("Person ID: " + personId + " | Name: " + getDisplayName());
        System.out.println("Dept: " + department.getDepartmentName() + " | Specialization: " + specialization);
        System.out.println("Hire Date: " + hireDate + " | Salary: " + salary + " | Office: " + officeNumber + " | Type: " + employmentType);
        System.out.println("=========================================");
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + employeeNumber + "] " + getDisplayName() + " - " + specialization + " (" + department.getDepartmentCode() + ")";
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber = officeNumber; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    @Override
    public String toString() { return getRegistrationSummary(); }
}
