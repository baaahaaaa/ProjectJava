package ranim.projetpidev.entites;

import java.util.Date;

public class Feedback {
    private int id;
    private int candidatId;
    private int internshipId;
    private int rating;
    private String comment;
    private Date dateFeedback;

    // Constructeur vide
    public Feedback() { }

    // Constructeur complet
    public Feedback(int id, int candidatId, int internshipId, int rating, String comment, Date dateFeedback) {
        this.id = id;
        this.candidatId = candidatId;
        this.internshipId = internshipId;
        this.rating = rating;
        this.comment = comment;
        this.dateFeedback = dateFeedback;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCandidatId() { return candidatId; }
    public void setCandidatId(int candidatId) { this.candidatId = candidatId; }

    public int getInternshipId() { return internshipId; }
    public void setInternshipId(int internshipId) { this.internshipId = internshipId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getDateFeedback() { return dateFeedback; }
    public void setDateFeedback(Date dateFeedback) { this.dateFeedback = dateFeedback; }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", candidatId=" + candidatId +
                ", internshipId=" + internshipId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", dateFeedback=" + dateFeedback +
                '}';
    }
}
