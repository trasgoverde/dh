import { element, by, ElementFinder } from 'protractor';

export class UrllinkComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-urllink div table .btn-danger'));
  title = element.all(by.css('jhi-urllink div h2#page-heading span')).first();
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

export class UrllinkUpdatePage {
  pageTitle = element(by.id('jhi-urllink-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  linkTextInput = element(by.id('field_linkText'));
  linkURLInput = element(by.id('field_linkURL'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setLinkTextInput(linkText: string): Promise<void> {
    await this.linkTextInput.sendKeys(linkText);
  }

  async getLinkTextInput(): Promise<string> {
    return await this.linkTextInput.getAttribute('value');
  }

  async setLinkURLInput(linkURL: string): Promise<void> {
    await this.linkURLInput.sendKeys(linkURL);
  }

  async getLinkURLInput(): Promise<string> {
    return await this.linkURLInput.getAttribute('value');
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

export class UrllinkDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-urllink-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-urllink'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
