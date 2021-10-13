import { element, by, ElementFinder } from 'protractor';

export class CcelebComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-cceleb div table .btn-danger'));
  title = element.all(by.css('jhi-cceleb div h2#page-heading span')).first();
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

export class CcelebUpdatePage {
  pageTitle = element(by.id('jhi-cceleb-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  celebNameInput = element(by.id('field_celebName'));

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

  async setCelebNameInput(celebName: string): Promise<void> {
    await this.celebNameInput.sendKeys(celebName);
  }

  async getCelebNameInput(): Promise<string> {
    return await this.celebNameInput.getAttribute('value');
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

export class CcelebDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-cceleb-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-cceleb'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
