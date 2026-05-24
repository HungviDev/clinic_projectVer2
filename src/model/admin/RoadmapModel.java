package model.admin;

import java.sql.Date;

public class RoadmapModel {

    private int id;
    private String title;
    private String description;
    private Date datecreate;

    // Constructor không tham số
    public RoadmapModel() {
    }

    // Constructor đầy đủ tham số
    public RoadmapModel(int id, String title, String description, Date datecreate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.datecreate = datecreate;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatecreate() {
        return datecreate;
    }

    public void setDatecreate(Date datecreate) {
        this.datecreate = datecreate;
    }
}