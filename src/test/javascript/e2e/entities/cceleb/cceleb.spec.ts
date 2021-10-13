import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CcelebComponentsPage, CcelebDeleteDialog, CcelebUpdatePage } from './cceleb.page-object';

const expect = chai.expect;

describe('Cceleb e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ccelebComponentsPage: CcelebComponentsPage;
  let ccelebUpdatePage: CcelebUpdatePage;
  let ccelebDeleteDialog: CcelebDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ccelebs', async () => {
    await navBarPage.goToEntity('cceleb');
    ccelebComponentsPage = new CcelebComponentsPage();
    await browser.wait(ec.visibilityOf(ccelebComponentsPage.title), 5000);
    expect(await ccelebComponentsPage.getTitle()).to.eq('dhApp.cceleb.home.title');
    await browser.wait(ec.or(ec.visibilityOf(ccelebComponentsPage.entities), ec.visibilityOf(ccelebComponentsPage.noResult)), 1000);
  });

  it('should load create Cceleb page', async () => {
    await ccelebComponentsPage.clickOnCreateButton();
    ccelebUpdatePage = new CcelebUpdatePage();
    expect(await ccelebUpdatePage.getPageTitle()).to.eq('dhApp.cceleb.home.createOrEditLabel');
    await ccelebUpdatePage.cancel();
  });

  it('should create and save Ccelebs', async () => {
    const nbButtonsBeforeCreate = await ccelebComponentsPage.countDeleteButtons();

    await ccelebComponentsPage.clickOnCreateButton();

    await promise.all([
      ccelebUpdatePage.setCelebNameInput('celebName'),
      // ccelebUpdatePage.communitySelectLastOption(),
    ]);

    await ccelebUpdatePage.save();
    expect(await ccelebUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ccelebComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cceleb', async () => {
    const nbButtonsBeforeDelete = await ccelebComponentsPage.countDeleteButtons();
    await ccelebComponentsPage.clickOnLastDeleteButton();

    ccelebDeleteDialog = new CcelebDeleteDialog();
    expect(await ccelebDeleteDialog.getDialogTitle()).to.eq('dhApp.cceleb.delete.question');
    await ccelebDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(ccelebComponentsPage.title), 5000);

    expect(await ccelebComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
