package com.blocknitive.com.service.dto;

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
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.blocknitive.com.domain.Appprofile} entity.
 */
public class AppprofileDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    private Gender gender;

    @Size(max = 20)
    private String phone;

    @Size(max = 7500)
    private String bio;

    @Size(max = 50)
    private String facebook;

    @Size(max = 50)
    private String twitter;

    @Size(max = 50)
    private String linkedin;

    @Size(max = 50)
    private String instagram;

    @Size(max = 50)
    private String googlePlus;

    private Instant birthdate;

    private CivilStatus civilStatus;

    private Gender lookingFor;

    private Purpose purpose;

    private Physical physical;

    private Religion religion;

    private EthnicGroup ethnicGroup;

    private Studies studies;

    @Min(value = -1)
    @Max(value = 20)
    private Integer sibblings;

    private Eyes eyes;

    private Smoker smoker;

    private Children children;

    private FutureChildren futureChildren;

    private Boolean pet;

    private AppuserDTO appuser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public Instant getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Instant birthdate) {
        this.birthdate = birthdate;
    }

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public Gender getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(Gender lookingFor) {
        this.lookingFor = lookingFor;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Physical getPhysical() {
        return physical;
    }

    public void setPhysical(Physical physical) {
        this.physical = physical;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public EthnicGroup getEthnicGroup() {
        return ethnicGroup;
    }

    public void setEthnicGroup(EthnicGroup ethnicGroup) {
        this.ethnicGroup = ethnicGroup;
    }

    public Studies getStudies() {
        return studies;
    }

    public void setStudies(Studies studies) {
        this.studies = studies;
    }

    public Integer getSibblings() {
        return sibblings;
    }

    public void setSibblings(Integer sibblings) {
        this.sibblings = sibblings;
    }

    public Eyes getEyes() {
        return eyes;
    }

    public void setEyes(Eyes eyes) {
        this.eyes = eyes;
    }

    public Smoker getSmoker() {
        return smoker;
    }

    public void setSmoker(Smoker smoker) {
        this.smoker = smoker;
    }

    public Children getChildren() {
        return children;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    public FutureChildren getFutureChildren() {
        return futureChildren;
    }

    public void setFutureChildren(FutureChildren futureChildren) {
        this.futureChildren = futureChildren;
    }

    public Boolean getPet() {
        return pet;
    }

    public void setPet(Boolean pet) {
        this.pet = pet;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppprofileDTO)) {
            return false;
        }

        AppprofileDTO appprofileDTO = (AppprofileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appprofileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppprofileDTO{" +
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
            ", appuser=" + getAppuser() +
            "}";
    }
}
