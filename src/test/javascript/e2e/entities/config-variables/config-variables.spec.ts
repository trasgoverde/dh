import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ConfigVariablesComponentsPage, ConfigVariablesDeleteDialog, ConfigVariablesUpdatePage } from './config-variables.page-object';

const expect = chai.expect;

describe('ConfigVariables e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let configVariablesComponentsPage: ConfigVariablesComponentsPage;
  let configVariablesUpdatePage: ConfigVariablesUpdatePage;
  let configVariablesDeleteDialog: ConfigVariablesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ConfigVariables', async () => {
    await navBarPage.goToEntity('config-variables');
    configVariablesComponentsPage = new ConfigVariablesComponentsPage();
    await browser.wait(ec.visibilityOf(configVariablesComponentsPage.title), 5000);
    expect(await configVariablesComponentsPage.getTitle()).to.eq('dhApp.configVariables.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(configVariablesComponentsPage.entities), ec.visibilityOf(configVariablesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ConfigVariables page', async () => {
    await configVariablesComponentsPage.clickOnCreateButton();
    configVariablesUpdatePage = new ConfigVariablesUpdatePage();
    expect(await configVariablesUpdatePage.getPageTitle()).to.eq('dhApp.configVariables.home.createOrEditLabel');
    await configVariablesUpdatePage.cancel();
  });

  it('should create and save ConfigVariables', async () => {
    const nbButtonsBeforeCreate = await configVariablesComponentsPage.countDeleteButtons();

    await configVariablesComponentsPage.clickOnCreateButton();

    await promise.all([
      configVariablesUpdatePage.setConfigVarLong1Input('5'),
      configVariablesUpdatePage.setConfigVarLong2Input('5'),
      configVariablesUpdatePage.setConfigVarLong3Input('5'),
      configVariablesUpdatePage.setConfigVarLong4Input('5'),
      configVariablesUpdatePage.setConfigVarLong5Input('5'),
      configVariablesUpdatePage.setConfigVarLong6Input('5'),
      configVariablesUpdatePage.setConfigVarLong7Input('5'),
      configVariablesUpdatePage.setConfigVarLong8Input('5'),
      configVariablesUpdatePage.setConfigVarLong9Input('5'),
      configVariablesUpdatePage.setConfigVarLong10Input('5'),
      configVariablesUpdatePage.setConfigVarLong11Input('5'),
      configVariablesUpdatePage.setConfigVarLong12Input('5'),
      configVariablesUpdatePage.setConfigVarLong13Input('5'),
      configVariablesUpdatePage.setConfigVarLong14Input('5'),
      configVariablesUpdatePage.setConfigVarLong15Input('5'),
      configVariablesUpdatePage.getConfigVarBoolean16Input().click(),
      configVariablesUpdatePage.getConfigVarBoolean17Input().click(),
      configVariablesUpdatePage.getConfigVarBoolean18Input().click(),
      configVariablesUpdatePage.setConfigVarString19Input('configVarString19'),
      configVariablesUpdatePage.setConfigVarString20Input('configVarString20'),
    ]);

    await configVariablesUpdatePage.save();
    expect(await configVariablesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await configVariablesComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ConfigVariables', async () => {
    const nbButtonsBeforeDelete = await configVariablesComponentsPage.countDeleteButtons();
    await configVariablesComponentsPage.clickOnLastDeleteButton();

    configVariablesDeleteDialog = new ConfigVariablesDeleteDialog();
    expect(await configVariablesDeleteDialog.getDialogTitle()).to.eq('dhApp.configVariables.delete.question');
    await configVariablesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(configVariablesComponentsPage.title), 5000);

    expect(await configVariablesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
