import { element, by, ElementFinder } from 'protractor';

export class CmessageComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-cmessage div table .btn-danger'));
  title = element.all(by.css('jhi-cmessage div h2#page-heading span')).first();
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

export class CmessageUpdatePage {
  pageTitle = element(by.id('jhi-cmessage-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  messageTextInput = element(by.id('field_messageText'));
  isDeliveredInput = element(by.id('field_isDelivered'));

  csenderSelect = element(by.id('field_csender'));
  creceiverSelect = element(by.id('field_creceiver'));

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

  async setMessageTextInput(messageText: string): Promise<void> {
    await this.messageTextInput.sendKeys(messageText);
  }

  async getMessageTextInput(): Promise<string> {
    return await this.messageTextInput.getAttribute('value');
  }

  getIsDeliveredInput(): ElementFinder {
    return this.isDeliveredInput;
  }

  async csenderSelectLastOption(): Promise<void> {
    await this.csenderSelect.all(by.tagName('option')).last().click();
  }

  async csenderSelectOption(option: string): Promise<void> {
    await this.csenderSelect.sendKeys(option);
  }

  getCsenderSelect(): ElementFinder {
    return this.csenderSelect;
  }

  async getCsenderSelectedOption(): Promise<string> {
    return await this.csenderSelect.element(by.css('option:checked')).getText();
  }

  async creceiverSelectLastOption(): Promise<void> {
    await this.creceiverSelect.all(by.tagName('option')).last().click();
  }

  async creceiverSelectOption(option: string): Promise<void> {
    await this.creceiverSelect.sendKeys(option);
  }

  getCreceiverSelect(): ElementFinder {
    return this.creceiverSelect;
  }

  async getCreceiverSelectedOption(): Promise<string> {
    return await this.creceiverSelect.element(by.css('option:checked')).getText();
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

export class CmessageDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cmessage-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cmessage'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
