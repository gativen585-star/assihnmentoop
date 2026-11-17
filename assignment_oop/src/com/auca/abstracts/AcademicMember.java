package com.auca.abstracts;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;

public abstract class AcademicMember extends Person {
    protected String memberNumber;
    protected Department department;
    protected LocalDate joinDate;
    protected String academicStatus;
    protected String institutionalEmail;
    protected String advisorName;

    public AcademicMember(String personId, String firstName, String lastName, String email, String phoneNumber, LocalDate dateOfBirth,
                          String memberNumber, Department department, LocalDate joinDate, String academicStatus, String institutionalEmail, String advisorName) {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.memberNumber = memberNumber;
        this.department = department;
        this.joinDate = joinDate;
        this.academicStatus = academicStatus;
        this.institutionalEmail = institutionalEmail;
        this.advisorName = advisorName;
    }

    public abstract String getAcademicCategory();

    @Override
    public boolean isActive() { return "ACTIVE".equalsIgnoreCase(academicStatus); }

    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    public String getAcademicStatus() { return academicStatus; }
    public void setAcademicStatus(String academicStatus) { this.academicStatus = academicStatus; }
    public String getInstitutionalEmail() { return institutionalEmail; }
    public void setInstitutionalEmail(String institutionalEmail) { this.institutionalEmail = institutionalEmail; }
    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    protected void validateAcademicMember() throws ValidationException {
        validatePerson();
        if (memberNumber == null || memberNumber.trim().length() < MIN_ID_LENGTH || memberNumber.trim().length() > MAX_ID_LENGTH)
            throw new ValidationException("Member number must be 4-30 characters.");
        if (department == null)
            throw new ValidationException("Department is required.");
        if (joinDate == null || joinDate.isAfter(LocalDate.now()))
            throw new ValidationException("Join date cannot be in the future.");
        if (academicStatus == null || (!academicStatus.equalsIgnoreCase("ACTIVE") && !academicStatus.equalsIgnoreCase("INACTIVE")))
            throw new ValidationException("Academic status must be ACTIVE or INACTIVE.");
        if (institutionalEmail == null || !institutionalEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$") || institutionalEmail.length() > MAX_EMAIL_LENGTH)
            throw new ValidationException("Invalid institutional email.");
        if (advisorName == null || advisorName.trim().length() < 2 || advisorName.trim().length() > 120)
            throw new ValidationException("Advisor name must be 2-120 characters.");
    }
}
