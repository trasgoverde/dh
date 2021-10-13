import { element, by, ElementFinder } from 'protractor';

export class MessageComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-message div table .btn-danger'));
  title = element.all(by.css('jhi-message div h2#page-heading span')).first();
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

export class MessageUpdatePage {
  pageTitle = element(by.id('jhi-message-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  messageTextInput = element(by.id('field_messageText'));
  isDeliveredInput = element(by.id('field_isDelivered'));

  senderSelect = element(by.id('field_sender'));
  receiverSelect = element(by.id('field_receiver'));

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

  async senderSelectLastOption(): Promise<void> {
    await this.senderSelect.all(by.tagName('option')).last().click();
  }

  async senderSelectOption(option: string): Promise<void> {
    await this.senderSelect.sendKeys(option);
  }

  getSenderSelect(): ElementFinder {
    return this.senderSelect;
  }

  async getSenderSelectedOption(): Promise<string> {
    return await this.senderSelect.element(by.css('option:checked')).getText();
  }

  async receiverSelectLastOption(): Promise<void> {
    await this.receiverSelect.all(by.tagName('option')).last().click();
  }

  async receiverSelectOption(option: string): Promise<void> {
    await this.receiverSelect.sendKeys(option);
  }

  getReceiverSelect(): ElementFinder {
    return this.receiverSelect;
  }

  async getReceiverSelectedOption(): Promise<string> {
    return await this.receiverSelect.element(by.css('option:checked')).getText();
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

export class MessageDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-message-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-message'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
