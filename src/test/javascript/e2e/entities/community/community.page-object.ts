import { element, by, ElementFinder } from 'protractor';

export class CommunityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-community div table .btn-danger'));
  title = element.all(by.css('jhi-community div h2#page-heading span')).first();
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

export class CommunityUpdatePage {
  pageTitle = element(by.id('jhi-community-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  communityNameInput = element(by.id('field_communityName'));
  communityDescriptionInput = element(by.id('field_communityDescription'));
  imageInput = element(by.id('file_image'));
  isActiveInput = element(by.id('field_isActive'));

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

  async setCommunityNameInput(communityName: string): Promise<void> {
    await this.communityNameInput.sendKeys(communityName);
  }

  async getCommunityNameInput(): Promise<string> {
    return await this.communityNameInput.getAttribute('value');
  }

  async setCommunityDescriptionInput(communityDescription: string): Promise<void> {
    await this.communityDescriptionInput.sendKeys(communityDescription);
  }

  async getCommunityDescriptionInput(): Promise<string> {
    return await this.communityDescriptionInput.getAttribute('value');
  }

  async setImageInput(image: string): Promise<void> {
    await this.imageInput.sendKeys(image);
  }

  async getImageInput(): Promise<string> {
    return await this.imageInput.getAttribute('value');
  }

  getIsActiveInput(): ElementFinder {
    return this.isActiveInput;
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

export class CommunityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-community-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-community'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
