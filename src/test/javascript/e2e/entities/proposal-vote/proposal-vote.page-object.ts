import { element, by, ElementFinder } from 'protractor';

export class ProposalVoteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-proposal-vote div table .btn-danger'));
  title = element.all(by.css('jhi-proposal-vote div h2#page-heading span')).first();
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

export class ProposalVoteUpdatePage {
  pageTitle = element(by.id('jhi-proposal-vote-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  votePointsInput = element(by.id('field_votePoints'));

  appuserSelect = element(by.id('field_appuser'));
  proposalSelect = element(by.id('field_proposal'));

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

  async setVotePointsInput(votePoints: string): Promise<void> {
    await this.votePointsInput.sendKeys(votePoints);
  }

  async getVotePointsInput(): Promise<string> {
    return await this.votePointsInput.getAttribute('value');
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

  async proposalSelectLastOption(): Promise<void> {
    await this.proposalSelect.all(by.tagName('option')).last().click();
  }

  async proposalSelectOption(option: string): Promise<void> {
    await this.proposalSelect.sendKeys(option);
  }

  getProposalSelect(): ElementFinder {
    return this.proposalSelect;
  }

  async getProposalSelectedOption(): Promise<string> {
    return await this.proposalSelect.element(by.css('option:checked')).getText();
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

export class ProposalVoteDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-proposalVote-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-proposalVote'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
