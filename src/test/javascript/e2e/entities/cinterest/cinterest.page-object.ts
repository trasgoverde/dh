import { element, by, ElementFinder } from 'protractor';

export class CinterestComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-cinterest div table .btn-danger'));
  title = element.all(by.css('jhi-cinterest div h2#page-heading span')).first();
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

export class CinterestUpdatePage {
  pageTitle = element(by.id('jhi-cinterest-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  interestNameInput = element(by.id('field_interestName'));

  communitySelect = element(by.id('field_community'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setInterestNameInput(interestName: string): Promise<void> {
    await this.interestNameInput.sendKeys(interestName);
  }

  async getInterestNameInput(): Promise<string> {
    return await this.interestNameInput.getAttribute('value');
  }

  async communitySelectLastOption(): Promise<void> {
    await this.communitySelect.all(by.tagName('option')).last().click();
  }

  async communitySelectOption(option: string): Promise<void> {
    await this.communitySelect.sendKeys(option);
  }

  getCommunitySelect(): ElementFinder {
    return this.communitySelect;
  }

  async getCommunitySelectedOption(): Promise<string> {
    return await this.communitySelect.element(by.css('option:checked')).getText();
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

export class CinterestDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cinterest-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cinterest'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
