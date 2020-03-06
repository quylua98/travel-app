package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.model.AuditModel;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "plan")

public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    @Size(min = 4, max = 200, message = "Please use 4 to 200 letters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Content is required")
    @Column(name = "content")
    private String content;

    @Column(name = "start_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDay;

    @Column(name = "end_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDay;

    @NotBlank(message = "Status is required")
    @Column(name = "status")
    private String status;

    @Column(name = "created_day")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDay;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    @JoinColumn(name = "schedule_id", nullable = false)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "plan")
    private List<PlanInteractor> planInteractors;

    public Plan() {
    }

    public Plan(@NotBlank(message = "Name is required") @Size(min = 4, max = 200, message = "Please use 4 to 200 letters") String name,
                @NotBlank(message = "Content is required") String content, Date startDay, Date endDay,
                @NotBlank(message = "Status is required") String status,
                Date createdDay, String image,
                User user) {
        this.name = name;
        this.content = content;
        this.startDay = startDay;
        this.endDay = endDay;
        this.status = status;
        this.createdDay = createdDay;
        this.image = image;
        this.user = user;
    }

    public List<PlanInteractor> getPlanInteractors() {
        return planInteractors;
    }

    public void setPlanInteractors(List<PlanInteractor> planInteractors) {
        this.planInteractors = planInteractors;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDay() {
        return createdDay;
    }

    public void setCreatedDay(Date createdDay) {
        this.createdDay = createdDay;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", status='" + status + '\'' +
                ", createdDay=" + createdDay +
                ", image='" + image + '\'' +
                ", user=" + user +
                '}';
    }
}
