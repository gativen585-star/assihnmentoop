package com.auca.department;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;
import com.auca.exceptions.ValidationException;

public class Department implements Identifiable, Registrable {
    private String departmentId;
    private String departmentName;
    private String departmentCode;
    private String officeLocation;
    private String contactEmail;
    private Teacher headTeacher;

    public Department(String departmentId, String departmentName, String departmentCode, String officeLocation, String contactEmail, Teacher headTeacher) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.officeLocation = officeLocation;
        this.contactEmail = contactEmail;
        this.headTeacher = headTeacher;
    }

    @Override
    public String getId() { return departmentId; }

    @Override
    public String getDisplayName() { return departmentName + " (" + departmentCode + ")"; }

    @Override
    public boolean isActive() { return true; }

    @Override
    public void validate() throws ValidationException {
        if (departmentId == null || departmentId.trim().length() < MIN_ID_LENGTH || departmentId.trim().length() > MAX_ID_LENGTH)
            throw new ValidationException("Department ID must be 4-30 characters.");
        if (departmentName == null || departmentName.trim().length() < MIN_NAME_LENGTH || departmentName.trim().length() > MAX_NAME_LENGTH)
            throw new ValidationException("Department name must be 2-60 characters.");
        if (departmentCode == null || departmentCode.trim().isEmpty())
            throw new ValidationException("Department code is required.");
        if (officeLocation == null || officeLocation.trim().isEmpty())
            throw new ValidationException("Office location is required.");
        if (contactEmail == null || !contactEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$") || contactEmail.length() > MAX_EMAIL_LENGTH)
            throw new ValidationException("Invalid contact email.");
    }

    @Override
    public void printDetails() {
        System.out.println("=========================================");
        System.out.println("DEPARTMENT: " + departmentId + " | " + departmentName + " (" + departmentCode + ")");
        System.out.println("Office: " + officeLocation + " | Email: " + contactEmail);
        System.out.println("Head Teacher: " + (headTeacher != null ? headTeacher.getDisplayName() : "Not Assigned"));
        System.out.println("=========================================");
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + departmentId + "] " + departmentName + " (" + departmentCode + ")";
    }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public Teacher getHeadTeacher() { return headTeacher; }
    public void setHeadTeacher(Teacher headTeacher) { this.headTeacher = headTeacher; }

    @Override
    public String toString() { return getRegistrationSummary(); }
}
