import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CmessageComponentsPage, CmessageDeleteDialog, CmessageUpdatePage } from './cmessage.page-object';

const expect = chai.expect;

describe('Cmessage e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cmessageComponentsPage: CmessageComponentsPage;
  let cmessageUpdatePage: CmessageUpdatePage;
  let cmessageDeleteDialog: CmessageDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cmessages', async () => {
    await navBarPage.goToEntity('cmessage');
    cmessageComponentsPage = new CmessageComponentsPage();
    await browser.wait(ec.visibilityOf(cmessageComponentsPage.title), 5000);
    expect(await cmessageComponentsPage.getTitle()).to.eq('dhApp.cmessage.home.title');
    await browser.wait(ec.or(ec.visibilityOf(cmessageComponentsPage.entities), ec.visibilityOf(cmessageComponentsPage.noResult)), 1000);
  });

  it('should load create Cmessage page', async () => {
    await cmessageComponentsPage.clickOnCreateButton();
    cmessageUpdatePage = new CmessageUpdatePage();
    expect(await cmessageUpdatePage.getPageTitle()).to.eq('dhApp.cmessage.home.createOrEditLabel');
    await cmessageUpdatePage.cancel();
  });

  it('should create and save Cmessages', async () => {
    const nbButtonsBeforeCreate = await cmessageComponentsPage.countDeleteButtons();

    await cmessageComponentsPage.clickOnCreateButton();

    await promise.all([
      cmessageUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      cmessageUpdatePage.setMessageTextInput('messageText'),
      cmessageUpdatePage.getIsDeliveredInput().click(),
      cmessageUpdatePage.csenderSelectLastOption(),
      cmessageUpdatePage.creceiverSelectLastOption(),
    ]);

    await cmessageUpdatePage.save();
    expect(await cmessageUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cmessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cmessage', async () => {
    const nbButtonsBeforeDelete = await cmessageComponentsPage.countDeleteButtons();
    await cmessageComponentsPage.clickOnLastDeleteButton();

    cmessageDeleteDialog = new CmessageDeleteDialog();
    expect(await cmessageDeleteDialog.getDialogTitle()).to.eq('dhApp.cmessage.delete.question');
    await cmessageDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cmessageComponentsPage.title), 5000);

    expect(await cmessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
