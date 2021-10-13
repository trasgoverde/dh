import { element, by, ElementFinder } from 'protractor';

export class PostComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-post div table .btn-danger'));
  title = element.all(by.css('jhi-post div h2#page-heading span')).first();
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

export class PostUpdatePage {
  pageTitle = element(by.id('jhi-post-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  creationDateInput = element(by.id('field_creationDate'));
  publicationDateInput = element(by.id('field_publicationDate'));
  headlineInput = element(by.id('field_headline'));
  leadtextInput = element(by.id('field_leadtext'));
  bodytextInput = element(by.id('field_bodytext'));
  quoteInput = element(by.id('field_quote'));
  conclusionInput = element(by.id('field_conclusion'));
  linkTextInput = element(by.id('field_linkText'));
  linkURLInput = element(by.id('field_linkURL'));
  imageInput = element(by.id('file_image'));

  appuserSelect = element(by.id('field_appuser'));
  blogSelect = element(by.id('field_blog'));

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

  async setPublicationDateInput(publicationDate: string): Promise<void> {
    await this.publicationDateInput.sendKeys(publicationDate);
  }

  async getPublicationDateInput(): Promise<string> {
    return await this.publicationDateInput.getAttribute('value');
  }

  async setHeadlineInput(headline: string): Promise<void> {
    await this.headlineInput.sendKeys(headline);
  }

  async getHeadlineInput(): Promise<string> {
    return await this.headlineInput.getAttribute('value');
  }

  async setLeadtextInput(leadtext: string): Promise<void> {
    await this.leadtextInput.sendKeys(leadtext);
  }

  async getLeadtextInput(): Promise<string> {
    return await this.leadtextInput.getAttribute('value');
  }

  async setBodytextInput(bodytext: string): Promise<void> {
    await this.bodytextInput.sendKeys(bodytext);
  }

  async getBodytextInput(): Promise<string> {
    return await this.bodytextInput.getAttribute('value');
  }

  async setQuoteInput(quote: string): Promise<void> {
    await this.quoteInput.sendKeys(quote);
  }

  async getQuoteInput(): Promise<string> {
    return await this.quoteInput.getAttribute('value');
  }

  async setConclusionInput(conclusion: string): Promise<void> {
    await this.conclusionInput.sendKeys(conclusion);
  }

  async getConclusionInput(): Promise<string> {
    return await this.conclusionInput.getAttribute('value');
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

  async setImageInput(image: string): Promise<void> {
    await this.imageInput.sendKeys(image);
  }

  async getImageInput(): Promise<string> {
    return await this.imageInput.getAttribute('value');
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

  async blogSelectLastOption(): Promise<void> {
    await this.blogSelect.all(by.tagName('option')).last().click();
  }

  async blogSelectOption(option: string): Promise<void> {
    await this.blogSelect.sendKeys(option);
  }

  getBlogSelect(): ElementFinder {
    return this.blogSelect;
  }

  async getBlogSelectedOption(): Promise<string> {
    return await this.blogSelect.element(by.css('option:checked')).getText();
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

export class PostDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-post-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-post'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
