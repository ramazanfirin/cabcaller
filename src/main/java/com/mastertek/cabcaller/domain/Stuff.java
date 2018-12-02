package com.mastertek.cabcaller.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Stuff.
 */
@Entity
@Table(name = "stuff")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stuff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "user_code")
    private Long userCode;

    @NotNull
    @Column(name = "jhi_password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "device_token", nullable = false)
    private String deviceToken;

    @NotNull
    @Column(name = "statu", nullable = false)
    private String statu;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Company company;

    @ManyToOne
    private CabinGroup cabinGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Stuff name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Stuff surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public Stuff email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserCode() {
        return userCode;
    }

    public Stuff userCode(Long userCode) {
        this.userCode = userCode;
        return this;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public Stuff password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public Stuff deviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        return this;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getStatu() {
        return statu;
    }

    public Stuff statu(String statu) {
        this.statu = statu;
        return this;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public Branch getBranch() {
        return branch;
    }

    public Stuff branch(Branch branch) {
        this.branch = branch;
        return this;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Company getCompany() {
        return company;
    }

    public Stuff company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CabinGroup getCabinGroup() {
        return cabinGroup;
    }

    public Stuff cabinGroup(CabinGroup cabinGroup) {
        this.cabinGroup = cabinGroup;
        return this;
    }

    public void setCabinGroup(CabinGroup cabinGroup) {
        this.cabinGroup = cabinGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stuff stuff = (Stuff) o;
        if (stuff.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stuff.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stuff{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", userCode=" + getUserCode() +
            ", password='" + getPassword() + "'" +
            ", deviceToken='" + getDeviceToken() + "'" +
            ", statu='" + getStatu() + "'" +
            "}";
    }
}
