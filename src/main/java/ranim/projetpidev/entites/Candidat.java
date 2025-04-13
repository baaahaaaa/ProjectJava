package ranim.projetpidev.entites;

import java.util.Date;

public class Candidat {
    private int id;
    private int internshipId;       // Colonne "internship_id"
    private int studentId;          // Colonne "student_id"
    private Date dateCandidature;   // Colonne "date_candidature"
    private String email;
    private String phoneNumber;     // Colonne "phone_number"
    private String fullName;        // Colonne "full_name"
    private boolean result;         // Colonne "result" de type boolean
    private String cvFilename;      // Colonne "cv_filename"
    private String coverLetterFilename; // Colonne "cover_letter_filename"
    private String coverLetterText;     // Colonne "cover_letter_text"
    private String applyingMotivation;  // Colonne "applying_motivation"
    private Date interviewDate;         // Colonne "interview_date"

    // Constructeur vide
    public Candidat() { }

    // Constructeur complet avec le paramètre result de type boolean
    public Candidat(int id, int internshipId, int studentId, Date dateCandidature, String email,
                    String phoneNumber, String fullName, boolean result, String cvFilename,
                    String coverLetterFilename, String coverLetterText, String applyingMotivation,
                    Date interviewDate) {
        this.id = id;
        this.internshipId = internshipId;
        this.studentId = studentId;
        this.dateCandidature = dateCandidature;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.result = result;
        this.cvFilename = cvFilename;
        this.coverLetterFilename = coverLetterFilename;
        this.coverLetterText = coverLetterText;
        this.applyingMotivation = applyingMotivation;
        this.interviewDate = interviewDate;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getInternshipId() { return internshipId; }
    public void setInternshipId(int internshipId) { this.internshipId = internshipId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public Date getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(Date dateCandidature) { this.dateCandidature = dateCandidature; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    // Pour le boolean, le getter est généralement nommé isResult
    public boolean isResult() { return result; }
    public void setResult(boolean result) { this.result = result; }

    public String getCvFilename() { return cvFilename; }
    public void setCvFilename(String cvFilename) { this.cvFilename = cvFilename; }

    public String getCoverLetterFilename() { return coverLetterFilename; }
    public void setCoverLetterFilename(String coverLetterFilename) { this.coverLetterFilename = coverLetterFilename; }

    public String getCoverLetterText() { return coverLetterText; }
    public void setCoverLetterText(String coverLetterText) { this.coverLetterText = coverLetterText; }

    public String getApplyingMotivation() { return applyingMotivation; }
    public void setApplyingMotivation(String applyingMotivation) { this.applyingMotivation = applyingMotivation; }

    public Date getInterviewDate() { return interviewDate; }
    public void setInterviewDate(Date interviewDate) { this.interviewDate = interviewDate; }

    @Override
    public String toString() {
        return "Candidat{" +
                "id=" + id +
                ", internshipId=" + internshipId +
                ", studentId=" + studentId +
                ", dateCandidature=" + dateCandidature +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", result=" + result +
                ", cvFilename='" + cvFilename + '\'' +
                ", coverLetterFilename='" + coverLetterFilename + '\'' +
                ", coverLetterText='" + coverLetterText + '\'' +
                ", applyingMotivation='" + applyingMotivation + '\'' +
                ", interviewDate=" + interviewDate +
                '}';
    }
}
