import { element, by, ElementFinder } from 'protractor';

export class NotificationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-notification div table .btn-danger'));
  title = element.all(by.css('jhi-notification div h2#page-heading span')).first();
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

export class NotificationUpdatePage {
  pageTitle = element(by.id('jhi-notification-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  notificationDateInput = element(by.id('field_notificationDate'));
  notificationReasonSelect = element(by.id('field_notificationReason'));
  notificationTextInput = element(by.id('field_notificationText'));
  isDeliveredInput = element(by.id('field_isDelivered'));

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

  async setNotificationDateInput(notificationDate: string): Promise<void> {
    await this.notificationDateInput.sendKeys(notificationDate);
  }

  async getNotificationDateInput(): Promise<string> {
    return await this.notificationDateInput.getAttribute('value');
  }

  async setNotificationReasonSelect(notificationReason: string): Promise<void> {
    await this.notificationReasonSelect.sendKeys(notificationReason);
  }

  async getNotificationReasonSelect(): Promise<string> {
    return await this.notificationReasonSelect.element(by.css('option:checked')).getText();
  }

  async notificationReasonSelectLastOption(): Promise<void> {
    await this.notificationReasonSelect.all(by.tagName('option')).last().click();
  }

  async setNotificationTextInput(notificationText: string): Promise<void> {
    await this.notificationTextInput.sendKeys(notificationText);
  }

  async getNotificationTextInput(): Promise<string> {
    return await this.notificationTextInput.getAttribute('value');
  }

  getIsDeliveredInput(): ElementFinder {
    return this.isDeliveredInput;
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

export class NotificationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-notification-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-notification'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
