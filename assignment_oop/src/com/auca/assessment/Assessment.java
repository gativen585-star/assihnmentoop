package com.auca.assessment;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;

public class Assessment implements Identifiable, Registrable {
    private String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore;
    private double obtainedScore;
    private LocalDate assessmentDate;

    public Assessment(String assessmentId, Enrollment enrollment, String assessmentType, double maximumScore, double obtainedScore, LocalDate assessmentDate) {
        this.assessmentId = assessmentId;
        this.enrollment = enrollment;
        this.assessmentType = assessmentType;
        this.maximumScore = maximumScore;
        this.obtainedScore = obtainedScore;
        this.assessmentDate = assessmentDate;
    }

    @Override
    public String getId() { return assessmentId; }

    @Override
    public String getDisplayName() { return assessmentType + " (" + enrollment.getCourse().getCourseCode() + ")"; }

    @Override
    public boolean isActive() { return true; }

    @Override
    public void validate() throws ValidationException {
        if (assessmentId == null || assessmentId.trim().length() < MIN_ID_LENGTH || assessmentId.trim().length() > MAX_ID_LENGTH)
            throw new ValidationException("Assessment ID must be 4-30 characters.");
        if (enrollment == null || !enrollment.isActive())
            throw new ValidationException("Active enrollment is required.");
        if (assessmentType == null || (!assessmentType.equalsIgnoreCase("QUIZ") && !assessmentType.equalsIgnoreCase("MIDTERM") && !assessmentType.equalsIgnoreCase("FINAL") && !assessmentType.equalsIgnoreCase("ASSIGNMENT")))
            throw new ValidationException("Assessment type must be QUIZ, MIDTERM, FINAL, or ASSIGNMENT.");
        if (maximumScore <= 0)
            throw new ValidationException("Maximum score must be greater than zero.");
        if (obtainedScore < 0 || obtainedScore > maximumScore)
            throw new ValidationException("Obtained score must be between 0 and " + maximumScore);
        if (assessmentDate == null || assessmentDate.isAfter(LocalDate.now()))
            throw new ValidationException("Assessment date cannot be in the future.");
    }

    @Override
    public void printDetails() {
        System.out.println("=========================================");
        System.out.println("ASSESSMENT: " + assessmentId + " | Type: " + assessmentType);
        System.out.println("Enrollment: " + enrollment.getRegistrationSummary());
        System.out.println("Score: " + obtainedScore + " / " + maximumScore + " | Date: " + assessmentDate);
        System.out.println("=========================================");
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + assessmentId + "] " + enrollment.getStudent().getStudentNumber() + " - " + assessmentType + ": " + obtainedScore + "/" + maximumScore;
    }

    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) { this.enrollment = enrollment; }
    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }
    public double getMaximumScore() { return maximumScore; }
    public void setMaximumScore(double maximumScore) { this.maximumScore = maximumScore; }
    public double getObtainedScore() { return obtainedScore; }
    public void setObtainedScore(double obtainedScore) { this.obtainedScore = obtainedScore; }
    public LocalDate getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(LocalDate assessmentDate) { this.assessmentDate = assessmentDate; }

    @Override
    public String toString() { return getRegistrationSummary(); }
}
