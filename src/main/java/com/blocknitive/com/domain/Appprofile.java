package com.blocknitive.com.domain;

import com.blocknitive.com.domain.enumeration.Children;
import com.blocknitive.com.domain.enumeration.CivilStatus;
import com.blocknitive.com.domain.enumeration.EthnicGroup;
import com.blocknitive.com.domain.enumeration.Eyes;
import com.blocknitive.com.domain.enumeration.FutureChildren;
import com.blocknitive.com.domain.enumeration.Gender;
import com.blocknitive.com.domain.enumeration.Physical;
import com.blocknitive.com.domain.enumeration.Purpose;
import com.blocknitive.com.domain.enumeration.Religion;
import com.blocknitive.com.domain.enumeration.Smoker;
import com.blocknitive.com.domain.enumeration.Studies;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Appprofile.
 */
@Entity
@Table(name = "appprofile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appprofile")
public class Appprofile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 7500)
    @Column(name = "bio", length = 7500)
    private String bio;

    @Size(max = 50)
    @Column(name = "facebook", length = 50)
    private String facebook;

    @Size(max = 50)
    @Column(name = "twitter", length = 50)
    private String twitter;

    @Size(max = 50)
    @Column(name = "linkedin", length = 50)
    private String linkedin;

    @Size(max = 50)
    @Column(name = "instagram", length = 50)
    private String instagram;

    @Size(max = 50)
    @Column(name = "google_plus", length = 50)
    private String googlePlus;

    @Column(name = "birthdate")
    private Instant birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "civil_status")
    private CivilStatus civilStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "looking_for")
    private Gender lookingFor;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose")
    private Purpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "physical")
    private Physical physical;

    @Enumerated(EnumType.STRING)
    @Column(name = "religion")
    private Religion religion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ethnic_group")
    private EthnicGroup ethnicGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "studies")
    private Studies studies;

    @Min(value = -1)
    @Max(value = 20)
    @Column(name = "sibblings")
    private Integer sibblings;

    @Enumerated(EnumType.STRING)
    @Column(name = "eyes")
    private Eyes eyes;

    @Enumerated(EnumType.STRING)
    @Column(name = "smoker")
    private Smoker smoker;

    @Enumerated(EnumType.STRING)
    @Column(name = "children")
    private Children children;

    @Enumerated(EnumType.STRING)
    @Column(name = "future_children")
    private FutureChildren futureChildren;

    @Column(name = "pet")
    private Boolean pet;

    @JsonIgnoreProperties(
        value = {
            "user",
            "appprofile",
            "appphoto",
            "communities",
            "blogs",
            "notifications",
            "albums",
            "comments",
            "posts",
            "senders",
            "receivers",
            "followeds",
            "followings",
            "blockedusers",
            "blockingusers",
            "vtopics",
            "vquestions",
            "vanswers",
            "vthumbs",
            "proposals",
            "proposalVotes",
            "interests",
            "activities",
            "celebs",
        },
        allowSetters = true
    )
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Appuser appuser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Appprofile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Appprofile creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Appprofile gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public Appprofile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return this.bio;
    }

    public Appprofile bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public Appprofile facebook(String facebook) {
        this.setFacebook(facebook);
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public Appprofile twitter(String twitter) {
        this.setTwitter(twitter);
        return this;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return this.linkedin;
    }

    public Appprofile linkedin(String linkedin) {
        this.setLinkedin(linkedin);
        return this;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return this.instagram;
    }

    public Appprofile instagram(String instagram) {
        this.setInstagram(instagram);
        return this;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getGooglePlus() {
        return this.googlePlus;
    }

    public Appprofile googlePlus(String googlePlus) {
        this.setGooglePlus(googlePlus);
        return this;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public Instant getBirthdate() {
        return this.birthdate;
    }

    public Appprofile birthdate(Instant birthdate) {
        this.setBirthdate(birthdate);
        return this;
    }

    public void setBirthdate(Instant birthdate) {
        this.birthdate = birthdate;
    }

    public CivilStatus getCivilStatus() {
        return this.civilStatus;
    }

    public Appprofile civilStatus(CivilStatus civilStatus) {
        this.setCivilStatus(civilStatus);
        return this;
    }

    public void setCivilStatus(CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public Gender getLookingFor() {
        return this.lookingFor;
    }

    public Appprofile lookingFor(Gender lookingFor) {
        this.setLookingFor(lookingFor);
        return this;
    }

    public void setLookingFor(Gender lookingFor) {
        this.lookingFor = lookingFor;
    }

    public Purpose getPurpose() {
        return this.purpose;
    }

    public Appprofile purpose(Purpose purpose) {
        this.setPurpose(purpose);
        return this;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Physical getPhysical() {
        return this.physical;
    }

    public Appprofile physical(Physical physical) {
        this.setPhysical(physical);
        return this;
    }

    public void setPhysical(Physical physical) {
        this.physical = physical;
    }

    public Religion getReligion() {
        return this.religion;
    }

    public Appprofile religion(Religion religion) {
        this.setReligion(religion);
        return this;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public EthnicGroup getEthnicGroup() {
        return this.ethnicGroup;
    }

    public Appprofile ethnicGroup(EthnicGroup ethnicGroup) {
        this.setEthnicGroup(ethnicGroup);
        return this;
    }

    public void setEthnicGroup(EthnicGroup ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }

    public Studies getStudies() {
        return this.studies;
    }

    public Appprofile studies(Studies studies) {
        this.setStudies(studies);
        return this;
    }

    public void setStudies(Studies studies) {
        this.studies = studies;
    }

    public Integer getSibblings() {
        return this.sibblings;
    }

    public Appprofile sibblings(Integer sibblings) {
        this.setSibblings(sibblings);
        return this;
    }

    public void setSibblings(Integer sibblings) {
        this.sibblings = sibblings;
    }

    public Eyes getEyes() {
        return this.eyes;
    }

    public Appprofile eyes(Eyes eyes) {
        this.setEyes(eyes);
        return this;
    }

    public void setEyes(Eyes eyes) {
        this.eyes = eyes;
    }

    public Smoker getSmoker() {
        return this.smoker;
    }

    public Appprofile smoker(Smoker smoker) {
        this.setSmoker(smoker);
        return this;
    }

    public void setSmoker(Smoker smoker) {
        this.smoker = smoker;
    }

    public Children getChildren() {
        return this.children;
    }

    public Appprofile children(Children children) {
        this.setChildren(children);
        return this;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    public FutureChildren getFutureChildren() {
        return this.futureChildren;
    }

    public Appprofile futureChildren(FutureChildren futureChildren) {
        this.setFutureChildren(futureChildren);
        return this;
    }

    public void setFutureChildren(FutureChildren futureChildren) {
        this.futureChildren = futureChildren;
    }

    public Boolean getPet() {
        return this.pet;
    }

    public Appprofile pet(Boolean pet) {
        this.setPet(pet);
        return this;
    }

    public void setPet(Boolean pet) {
        this.pet = pet;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Appprofile appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appprofile)) {
            return false;
        }
        return id != null && id.equals(((Appprofile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appprofile{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bio='" + getBio() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", twitter='" + getTwitter() + "'" +
            ", linkedin='" + getLinkedin() + "'" +
            ", instagram='" + getInstagram() + "'" +
            ", googlePlus='" + getGooglePlus() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", civilStatus='" + getCivilStatus() + "'" +
            ", lookingFor='" + getLookingFor() + "'" +
            ", purpose='" + getPurpose() + "'" +
            ", physical='" + getPhysical() + "'" +
            ", religion='" + getReligion() + "'" +
            ", ethnicGroup='" + getEthnicGroup() + "'" +
            ", studies='" + getStudies() + "'" +
            ", sibblings=" + getSibblings() +
            ", eyes='" + getEyes() + "'" +
            ", smoker='" + getSmoker() + "'" +
            ", children='" + getChildren() + "'" +
            ", futureChildren='" + getFutureChildren() + "'" +
            ", pet='" + getPet() + "'" +
            "}";
    }
}
