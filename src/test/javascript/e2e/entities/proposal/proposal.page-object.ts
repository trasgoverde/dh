import { element, by, ElementFinder } from 'protractor';

export class ProposalComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-proposal div table .btn-danger'));
  title = element.all(by.css('jhi-proposal div h2#page-heading span')).first();
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

export class ProposalUpdatePage {
  pageTitle = element(by.id('jhi-proposal-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  proposalNameInput = element(by.id('field_proposalName'));
  proposalTypeSelect = element(by.id('field_proposalType'));
  proposalRoleSelect = element(by.id('field_proposalRole'));
  releaseDateInput = element(by.id('field_releaseDate'));
  isOpenInput = element(by.id('field_isOpen'));
  isAcceptedInput = element(by.id('field_isAccepted'));
  isPaidInput = element(by.id('field_isPaid'));

  appuserSelect = element(by.id('field_appuser'));
  postSelect = element(by.id('field_post'));

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

  async setProposalNameInput(proposalName: string): Promise<void> {
    await this.proposalNameInput.sendKeys(proposalName);
  }

  async getProposalNameInput(): Promise<string> {
    return await this.proposalNameInput.getAttribute('value');
  }

  async setProposalTypeSelect(proposalType: string): Promise<void> {
    await this.proposalTypeSelect.sendKeys(proposalType);
  }

  async getProposalTypeSelect(): Promise<string> {
    return await this.proposalTypeSelect.element(by.css('option:checked')).getText();
  }

  async proposalTypeSelectLastOption(): Promise<void> {
    await this.proposalTypeSelect.all(by.tagName('option')).last().click();
  }

  async setProposalRoleSelect(proposalRole: string): Promise<void> {
    await this.proposalRoleSelect.sendKeys(proposalRole);
  }

  async getProposalRoleSelect(): Promise<string> {
    return await this.proposalRoleSelect.element(by.css('option:checked')).getText();
  }

  async proposalRoleSelectLastOption(): Promise<void> {
    await this.proposalRoleSelect.all(by.tagName('option')).last().click();
  }

  async setReleaseDateInput(releaseDate: string): Promise<void> {
    await this.releaseDateInput.sendKeys(releaseDate);
  }

  async getReleaseDateInput(): Promise<string> {
    return await this.releaseDateInput.getAttribute('value');
  }

  getIsOpenInput(): ElementFinder {
    return this.isOpenInput;
  }

  getIsAcceptedInput(): ElementFinder {
    return this.isAcceptedInput;
  }

  getIsPaidInput(): ElementFinder {
    return this.isPaidInput;
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

  async postSelectLastOption(): Promise<void> {
    await this.postSelect.all(by.tagName('option')).last().click();
  }

  async postSelectOption(option: string): Promise<void> {
    await this.postSelect.sendKeys(option);
  }

  getPostSelect(): ElementFinder {
    return this.postSelect;
  }

  async getPostSelectedOption(): Promise<string> {
    return await this.postSelect.element(by.css('option:checked')).getText();
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

export class ProposalDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-proposal-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-proposal'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
