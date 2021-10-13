import { element, by, ElementFinder } from 'protractor';

export class AppprofileComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-appprofile div table .btn-danger'));
  title = element.all(by.css('jhi-appprofile div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class AppprofileUpdatePage {
  pageTitle = element(by.id('jhi-appprofile-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  genderSelect = element(by.id('field_gender'));
  phoneInput = element(by.id('field_phone'));
  bioInput = element(by.id('field_bio'));
  facebookInput = element(by.id('field_facebook'));
  twitterInput = element(by.id('field_twitter'));
  linkedinInput = element(by.id('field_linkedin'));
  instagramInput = element(by.id('field_instagram'));
  googlePlusInput = element(by.id('field_googlePlus'));
  birthdateInput = element(by.id('field_birthdate'));
  civilStatusSelect = element(by.id('field_civilStatus'));
  lookingForSelect = element(by.id('field_lookingFor'));
  purposeSelect = element(by.id('field_purpose'));
  physicalSelect = element(by.id('field_physical'));
  religionSelect = element(by.id('field_religion'));
  ethnicGroupSelect = element(by.id('field_ethnicGroup'));
  studiesSelect = element(by.id('field_studies'));
  sibblingsInput = element(by.id('field_sibblings'));
  eyesSelect = element(by.id('field_eyes'));
  smokerSelect = element(by.id('field_smoker'));
  childrenSelect = element(by.id('field_children'));
  futureChildrenSelect = element(by.id('field_futureChildren'));
  petInput = element(by.id('field_pet'));

  appuserSelect = element(by.id('field_appuser'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCreationDateInput(creationDate: string): Promise<void> {
    await this.creationDateInput.sendKeys(creationDate);
  }

  async getCreationDateInput(): Promise<string> {
    return await this.creationDateInput.getAttribute('value');
  }

  async setGenderSelect(gender: string): Promise<void> {
    await this.genderSelect.sendKeys(gender);
  }

  async getGenderSelect(): Promise<string> {
    return await this.genderSelect.element(by.css('option:checked')).getText();
  }

  async genderSelectLastOption(): Promise<void> {
    await this.genderSelect.all(by.tagName('option')).last().click();
  }

  async setPhoneInput(phone: string): Promise<void> {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput(): Promise<string> {
    return await this.phoneInput.getAttribute('value');
  }

  async setBioInput(bio: string): Promise<void> {
    await this.bioInput.sendKeys(bio);
  }

  async getBioInput(): Promise<string> {
    return await this.bioInput.getAttribute('value');
  }

  async setFacebookInput(facebook: string): Promise<void> {
    await this.facebookInput.sendKeys(facebook);
  }

  async getFacebookInput(): Promise<string> {
    return await this.facebookInput.getAttribute('value');
  }

  async setTwitterInput(twitter: string): Promise<void> {
    await this.twitterInput.sendKeys(twitter);
  }

  async getTwitterInput(): Promise<string> {
    return await this.twitterInput.getAttribute('value');
  }

  async setLinkedinInput(linkedin: string): Promise<void> {
    await this.linkedinInput.sendKeys(linkedin);
  }

  async getLinkedinInput(): Promise<string> {
    return await this.linkedinInput.getAttribute('value');
  }

  async setInstagramInput(instagram: string): Promise<void> {
    await this.instagramInput.sendKeys(instagram);
  }

  async getInstagramInput(): Promise<string> {
    return await this.instagramInput.getAttribute('value');
  }

  async setGooglePlusInput(googlePlus: string): Promise<void> {
    await this.googlePlusInput.sendKeys(googlePlus);
  }

  async getGooglePlusInput(): Promise<string> {
    return await this.googlePlusInput.getAttribute('value');
  }

  async setBirthdateInput(birthdate: string): Promise<void> {
    await this.birthdateInput.sendKeys(birthdate);
  }

  async getBirthdateInput(): Promise<string> {
    return await this.birthdateInput.getAttribute('value');
  }

  async setCivilStatusSelect(civilStatus: string): Promise<void> {
    await this.civilStatusSelect.sendKeys(civilStatus);
  }

  async getCivilStatusSelect(): Promise<string> {
    return await this.civilStatusSelect.element(by.css('option:checked')).getText();
  }

  async civilStatusSelectLastOption(): Promise<void> {
    await this.civilStatusSelect.all(by.tagName('option')).last().click();
  }

  async setLookingForSelect(lookingFor: string): Promise<void> {
    await this.lookingForSelect.sendKeys(lookingFor);
  }

  async getLookingForSelect(): Promise<string> {
    return await this.lookingForSelect.element(by.css('option:checked')).getText();
  }

  async lookingForSelectLastOption(): Promise<void> {
    await this.lookingForSelect.all(by.tagName('option')).last().click();
  }

  async setPurposeSelect(purpose: string): Promise<void> {
    await this.purposeSelect.sendKeys(purpose);
  }

  async getPurposeSelect(): Promise<string> {
    return await this.purposeSelect.element(by.css('option:checked')).getText();
  }

  async purposeSelectLastOption(): Promise<void> {
    await this.purposeSelect.all(by.tagName('option')).last().click();
  }

  async setPhysicalSelect(physical: string): Promise<void> {
    await this.physicalSelect.sendKeys(physical);
  }

  async getPhysicalSelect(): Promise<string> {
    return await this.physicalSelect.element(by.css('option:checked')).getText();
  }

  async physicalSelectLastOption(): Promise<void> {
    await this.physicalSelect.all(by.tagName('option')).last().click();
  }

  async setReligionSelect(religion: string): Promise<void> {
    await this.religionSelect.sendKeys(religion);
  }

  async getReligionSelect(): Promise<string> {
    return await this.religionSelect.element(by.css('option:checked')).getText();
  }

  async religionSelectLastOption(): Promise<void> {
    await this.religionSelect.all(by.tagName('option')).last().click();
  }

  async setEthnicGroupSelect(ethnicGroup: string): Promise<void> {
    await this.ethnicGroupSelect.sendKeys(ethnicGroup);
  }

  async getEthnicGroupSelect(): Promise<string> {
    return await this.ethnicGroupSelect.element(by.css('option:checked')).getText();
  }

  async ethnicGroupSelectLastOption(): Promise<void> {
    await this.ethnicGroupSelect.all(by.tagName('option')).last().click();
  }

  async setStudiesSelect(studies: string): Promise<void> {
    await this.studiesSelect.sendKeys(studies);
  }

  async getStudiesSelect(): Promise<string> {
    return await this.studiesSelect.element(by.css('option:checked')).getText();
  }

  async studiesSelectLastOption(): Promise<void> {
    await this.studiesSelect.all(by.tagName('option')).last().click();
  }

  async setSibblingsInput(sibblings: string): Promise<void> {
    await this.sibblingsInput.sendKeys(sibblings);
  }

  async getSibblingsInput(): Promise<string> {
    return await this.sibblingsInput.getAttribute('value');
  }

  async setEyesSelect(eyes: string): Promise<void> {
    await this.eyesSelect.sendKeys(eyes);
  }

  async getEyesSelect(): Promise<string> {
    return await this.eyesSelect.element(by.css('option:checked')).getText();
  }

  async eyesSelectLastOption(): Promise<void> {
    await this.eyesSelect.all(by.tagName('option')).last().click();
  }

  async setSmokerSelect(smoker: string): Promise<void> {
    await this.smokerSelect.sendKeys(smoker);
  }

  async getSmokerSelect(): Promise<string> {
    return await this.smokerSelect.element(by.css('option:checked')).getText();
  }

  async smokerSelectLastOption(): Promise<void> {
    await this.smokerSelect.all(by.tagName('option')).last().click();
  }

  async setChildrenSelect(children: string): Promise<void> {
    await this.childrenSelect.sendKeys(children);
  }

  async getChildrenSelect(): Promise<string> {
    return await this.childrenSelect.element(by.css('option:checked')).getText();
  }

  async childrenSelectLastOption(): Promise<void> {
    await this.childrenSelect.all(by.tagName('option')).last().click();
  }

  async setFutureChildrenSelect(futureChildren: string): Promise<void> {
    await this.futureChildrenSelect.sendKeys(futureChildren);
  }

  async getFutureChildrenSelect(): Promise<string> {
    return await this.futureChildrenSelect.element(by.css('option:checked')).getText();
  }

  async futureChildrenSelectLastOption(): Promise<void> {
    await this.futureChildrenSelect.all(by.tagName('option')).last().click();
  }

  getPetInput(): ElementFinder {
    return this.petInput;
  }

  async appuserSelectLastOption(): Promise<void> {
    await this.appuserSelect.all(by.tagName('option')).last().click();
  }

  async appuserSelectOption(option: string): Promise<void> {
    await this.appuserSelect.sendKeys(option);
  }

  getAppuserSelect(): ElementFinder {
    return this.appuserSelect;
  }

  async getAppuserSelectedOption(): Promise<string> {
    return await this.appuserSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class AppprofileDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-appprofile-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-appprofile'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
