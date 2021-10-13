import { element, by, ElementFinder } from 'protractor';

export class ConfigVariablesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-config-variables div table .btn-danger'));
  title = element.all(by.css('jhi-config-variables div h2#page-heading span')).first();
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

export class ConfigVariablesUpdatePage {
  pageTitle = element(by.id('jhi-config-variables-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  configVarLong1Input = element(by.id('field_configVarLong1'));
  configVarLong2Input = element(by.id('field_configVarLong2'));
  configVarLong3Input = element(by.id('field_configVarLong3'));
  configVarLong4Input = element(by.id('field_configVarLong4'));
  configVarLong5Input = element(by.id('field_configVarLong5'));
  configVarLong6Input = element(by.id('field_configVarLong6'));
  configVarLong7Input = element(by.id('field_configVarLong7'));
  configVarLong8Input = element(by.id('field_configVarLong8'));
  configVarLong9Input = element(by.id('field_configVarLong9'));
  configVarLong10Input = element(by.id('field_configVarLong10'));
  configVarLong11Input = element(by.id('field_configVarLong11'));
  configVarLong12Input = element(by.id('field_configVarLong12'));
  configVarLong13Input = element(by.id('field_configVarLong13'));
  configVarLong14Input = element(by.id('field_configVarLong14'));
  configVarLong15Input = element(by.id('field_configVarLong15'));
  configVarBoolean16Input = element(by.id('field_configVarBoolean16'));
  configVarBoolean17Input = element(by.id('field_configVarBoolean17'));
  configVarBoolean18Input = element(by.id('field_configVarBoolean18'));
  configVarString19Input = element(by.id('field_configVarString19'));
  configVarString20Input = element(by.id('field_configVarString20'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setConfigVarLong1Input(configVarLong1: string): Promise<void> {
    await this.configVarLong1Input.sendKeys(configVarLong1);
  }

  async getConfigVarLong1Input(): Promise<string> {
    return await this.configVarLong1Input.getAttribute('value');
  }

  async setConfigVarLong2Input(configVarLong2: string): Promise<void> {
    await this.configVarLong2Input.sendKeys(configVarLong2);
  }

  async getConfigVarLong2Input(): Promise<string> {
    return await this.configVarLong2Input.getAttribute('value');
  }

  async setConfigVarLong3Input(configVarLong3: string): Promise<void> {
    await this.configVarLong3Input.sendKeys(configVarLong3);
  }

  async getConfigVarLong3Input(): Promise<string> {
    return await this.configVarLong3Input.getAttribute('value');
  }

  async setConfigVarLong4Input(configVarLong4: string): Promise<void> {
    await this.configVarLong4Input.sendKeys(configVarLong4);
  }

  async getConfigVarLong4Input(): Promise<string> {
    return await this.configVarLong4Input.getAttribute('value');
  }

  async setConfigVarLong5Input(configVarLong5: string): Promise<void> {
    await this.configVarLong5Input.sendKeys(configVarLong5);
  }

  async getConfigVarLong5Input(): Promise<string> {
    return await this.configVarLong5Input.getAttribute('value');
  }

  async setConfigVarLong6Input(configVarLong6: string): Promise<void> {
    await this.configVarLong6Input.sendKeys(configVarLong6);
  }

  async getConfigVarLong6Input(): Promise<string> {
    return await this.configVarLong6Input.getAttribute('value');
  }

  async setConfigVarLong7Input(configVarLong7: string): Promise<void> {
    await this.configVarLong7Input.sendKeys(configVarLong7);
  }

  async getConfigVarLong7Input(): Promise<string> {
    return await this.configVarLong7Input.getAttribute('value');
  }

  async setConfigVarLong8Input(configVarLong8: string): Promise<void> {
    await this.configVarLong8Input.sendKeys(configVarLong8);
  }

  async getConfigVarLong8Input(): Promise<string> {
    return await this.configVarLong8Input.getAttribute('value');
  }

  async setConfigVarLong9Input(configVarLong9: string): Promise<void> {
    await this.configVarLong9Input.sendKeys(configVarLong9);
  }

  async getConfigVarLong9Input(): Promise<string> {
    return await this.configVarLong9Input.getAttribute('value');
  }

  async setConfigVarLong10Input(configVarLong10: string): Promise<void> {
    await this.configVarLong10Input.sendKeys(configVarLong10);
  }

  async getConfigVarLong10Input(): Promise<string> {
    return await this.configVarLong10Input.getAttribute('value');
  }

  async setConfigVarLong11Input(configVarLong11: string): Promise<void> {
    await this.configVarLong11Input.sendKeys(configVarLong11);
  }

  async getConfigVarLong11Input(): Promise<string> {
    return await this.configVarLong11Input.getAttribute('value');
  }

  async setConfigVarLong12Input(configVarLong12: string): Promise<void> {
    await this.configVarLong12Input.sendKeys(configVarLong12);
  }

  async getConfigVarLong12Input(): Promise<string> {
    return await this.configVarLong12Input.getAttribute('value');
  }

  async setConfigVarLong13Input(configVarLong13: string): Promise<void> {
    await this.configVarLong13Input.sendKeys(configVarLong13);
  }

  async getConfigVarLong13Input(): Promise<string> {
    return await this.configVarLong13Input.getAttribute('value');
  }

  async setConfigVarLong14Input(configVarLong14: string): Promise<void> {
    await this.configVarLong14Input.sendKeys(configVarLong14);
  }

  async getConfigVarLong14Input(): Promise<string> {
    return await this.configVarLong14Input.getAttribute('value');
  }

  async setConfigVarLong15Input(configVarLong15: string): Promise<void> {
    await this.configVarLong15Input.sendKeys(configVarLong15);
  }

  async getConfigVarLong15Input(): Promise<string> {
    return await this.configVarLong15Input.getAttribute('value');
  }

  getConfigVarBoolean16Input(): ElementFinder {
    return this.configVarBoolean16Input;
  }

  getConfigVarBoolean17Input(): ElementFinder {
    return this.configVarBoolean17Input;
  }

  getConfigVarBoolean18Input(): ElementFinder {
    return this.configVarBoolean18Input;
  }

  async setConfigVarString19Input(configVarString19: string): Promise<void> {
    await this.configVarString19Input.sendKeys(configVarString19);
  }

  async getConfigVarString19Input(): Promise<string> {
    return await this.configVarString19Input.getAttribute('value');
  }

  async setConfigVarString20Input(configVarString20: string): Promise<void> {
    await this.configVarString20Input.sendKeys(configVarString20);
  }

  async getConfigVarString20Input(): Promise<string> {
    return await this.configVarString20Input.getAttribute('value');
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

export class ConfigVariablesDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-configVariables-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-configVariables'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
