package com.mastertek.cabcaller.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CabinGroupUserHistory.
 */
@Entity
@Table(name = "cabin_group_user_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CabinGroupUserHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "group_id", nullable = false)
    private Long groupID;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "action_date")
    private LocalDate actionDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "cabin_group_user_history_groupid",
               joinColumns = @JoinColumn(name="cabin_group_user_histories_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="groupids_id", referencedColumnName="id"))
    private Set<CabinGroup> groupIDS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "cabin_group_user_history_userid",
               joinColumns = @JoinColumn(name="cabin_group_user_histories_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="userids_id", referencedColumnName="id"))
    private Set<Stuff> userIDS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupID() {
        return groupID;
    }

    public CabinGroupUserHistory groupID(Long groupID) {
        this.groupID = groupID;
        return this;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Long getUserID() {
        return userID;
    }

    public CabinGroupUserHistory userID(Long userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public CabinGroupUserHistory actionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
        return this;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    public Set<CabinGroup> getGroupIDS() {
        return groupIDS;
    }

    public CabinGroupUserHistory groupIDS(Set<CabinGroup> cabinGroups) {
        this.groupIDS = cabinGroups;
        return this;
    }

    public CabinGroupUserHistory addGroupID(CabinGroup cabinGroup) {
        this.groupIDS.add(cabinGroup);
        return this;
    }

    public CabinGroupUserHistory removeGroupID(CabinGroup cabinGroup) {
        this.groupIDS.remove(cabinGroup);
        return this;
    }

    public void setGroupIDS(Set<CabinGroup> cabinGroups) {
        this.groupIDS = cabinGroups;
    }

    public Set<Stuff> getUserIDS() {
        return userIDS;
    }

    public CabinGroupUserHistory userIDS(Set<Stuff> stuffs) {
        this.userIDS = stuffs;
        return this;
    }

    public CabinGroupUserHistory addUserID(Stuff stuff) {
        this.userIDS.add(stuff);
        return this;
    }

    public CabinGroupUserHistory removeUserID(Stuff stuff) {
        this.userIDS.remove(stuff);
        return this;
    }

    public void setUserIDS(Set<Stuff> stuffs) {
        this.userIDS = stuffs;
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
        CabinGroupUserHistory cabinGroupUserHistory = (CabinGroupUserHistory) o;
        if (cabinGroupUserHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cabinGroupUserHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CabinGroupUserHistory{" +
            "id=" + getId() +
            ", groupID=" + getGroupID() +
            ", userID=" + getUserID() +
            ", actionDate='" + getActionDate() + "'" +
            "}";
    }
}
