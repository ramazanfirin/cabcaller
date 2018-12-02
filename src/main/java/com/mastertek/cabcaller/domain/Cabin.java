package com.mastertek.cabcaller.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Cabin.
 */
@Entity
@Table(name = "cabin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cabin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "cab_no", nullable = false)
    private String cabNo;

    @NotNull
    @Column(name = "button_id", nullable = false)
    private String buttonId;

    @ManyToOne
    private CabinGroup cabinGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCabNo() {
        return cabNo;
    }

    public Cabin cabNo(String cabNo) {
        this.cabNo = cabNo;
        return this;
    }

    public void setCabNo(String cabNo) {
        this.cabNo = cabNo;
    }

    public String getButtonId() {
        return buttonId;
    }

    public Cabin buttonId(String buttonId) {
        this.buttonId = buttonId;
        return this;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public CabinGroup getCabinGroup() {
        return cabinGroup;
    }

    public Cabin cabinGroup(CabinGroup cabinGroup) {
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
        Cabin cabin = (Cabin) o;
        if (cabin.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cabin.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cabin{" +
            "id=" + getId() +
            ", cabNo='" + getCabNo() + "'" +
            ", buttonId='" + getButtonId() + "'" +
            "}";
    }
}
