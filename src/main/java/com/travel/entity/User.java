package com.travel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_Birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dOfB;

    @Column(name = "gender")

    private boolean gender;

    @Column(unique = true)

    private String password;

    @Column(name = "join_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date joinDate;

    @JsonBackReference
    @OneToMany(mappedBy="user")
    private Plan plans;

    @OneToMany(mappedBy="user")
    private PlanInteractor planInteractors;

    public Plan getPlans() {
        return plans;
    }

    public void setPlans(Plan plans) {
        this.plans = plans;
    }

    public PlanInteractor getPlanInteractors() {
        return planInteractors;
    }

    public void setPlanInteractors(PlanInteractor planInteractors) {
        this.planInteractors = planInteractors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getdOB() {
        return dOfB;
    }

    public void setdOB(Date dOB) {
        this.dOfB = dOB;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getdOfB() {
        return dOfB;
    }

    public void setdOfB(Date dOfB) {
        this.dOfB = dOfB;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dOfB=" + dOfB +
                ", gender=" + gender +
                ", password='" + password + '\'' +
                ", joinDate=" + joinDate +
                ", plans=" + plans +
                ", planInteractors=" + planInteractors +
                '}';
    }
}
