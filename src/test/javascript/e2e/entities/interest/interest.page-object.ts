import { element, by, ElementFinder } from 'protractor';

export class InterestComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-interest div table .btn-danger'));
  title = element.all(by.css('jhi-interest div h2#page-heading span')).first();
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

export class InterestUpdatePage {
  pageTitle = element(by.id('jhi-interest-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  interestNameInput = element(by.id('field_interestName'));

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

  async setInterestNameInput(interestName: string): Promise<void> {
    await this.interestNameInput.sendKeys(interestName);
  }

  async getInterestNameInput(): Promise<string> {
    return await this.interestNameInput.getAttribute('value');
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

export class InterestDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-interest-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-interest'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
