package com.mastertek.cabcaller.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A CallerDetails.
 */
@Entity
@Table(name = "caller_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CallerDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "call_date")
    private LocalDate callDate;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Cabin cabin;

    @ManyToOne
    private Stuff stuff;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCallDate() {
        return callDate;
    }

    public CallerDetails callDate(LocalDate callDate) {
        this.callDate = callDate;
        return this;
    }

    public void setCallDate(LocalDate callDate) {
        this.callDate = callDate;
    }

    public Branch getBranch() {
        return branch;
    }

    public CallerDetails branch(Branch branch) {
        this.branch = branch;
        return this;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Cabin getCabin() {
        return cabin;
    }

    public CallerDetails cabin(Cabin cabin) {
        this.cabin = cabin;
        return this;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
    }

    public Stuff getStuff() {
        return stuff;
    }

    public CallerDetails stuff(Stuff stuff) {
        this.stuff = stuff;
        return this;
    }

    public void setStuff(Stuff stuff) {
        this.stuff = stuff;
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
        CallerDetails callerDetails = (CallerDetails) o;
        if (callerDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), callerDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CallerDetails{" +
            "id=" + getId() +
            ", callDate='" + getCallDate() + "'" +
            "}";
    }
}
