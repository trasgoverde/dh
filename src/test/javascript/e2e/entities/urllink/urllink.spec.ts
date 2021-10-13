import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { UrllinkComponentsPage, UrllinkDeleteDialog, UrllinkUpdatePage } from './urllink.page-object';

const expect = chai.expect;

describe('Urllink e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let urllinkComponentsPage: UrllinkComponentsPage;
  let urllinkUpdatePage: UrllinkUpdatePage;
  let urllinkDeleteDialog: UrllinkDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Urllinks', async () => {
    await navBarPage.goToEntity('urllink');
    urllinkComponentsPage = new UrllinkComponentsPage();
    await browser.wait(ec.visibilityOf(urllinkComponentsPage.title), 5000);
    expect(await urllinkComponentsPage.getTitle()).to.eq('dhApp.urllink.home.title');
    await browser.wait(ec.or(ec.visibilityOf(urllinkComponentsPage.entities), ec.visibilityOf(urllinkComponentsPage.noResult)), 1000);
  });

  it('should load create Urllink page', async () => {
    await urllinkComponentsPage.clickOnCreateButton();
    urllinkUpdatePage = new UrllinkUpdatePage();
    expect(await urllinkUpdatePage.getPageTitle()).to.eq('dhApp.urllink.home.createOrEditLabel');
    await urllinkUpdatePage.cancel();
  });

  it('should create and save Urllinks', async () => {
    const nbButtonsBeforeCreate = await urllinkComponentsPage.countDeleteButtons();

    await urllinkComponentsPage.clickOnCreateButton();

    await promise.all([urllinkUpdatePage.setLinkTextInput('linkText'), urllinkUpdatePage.setLinkURLInput('linkURL')]);

    await urllinkUpdatePage.save();
    expect(await urllinkUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await urllinkComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Urllink', async () => {
    const nbButtonsBeforeDelete = await urllinkComponentsPage.countDeleteButtons();
    await urllinkComponentsPage.clickOnLastDeleteButton();

    urllinkDeleteDialog = new UrllinkDeleteDialog();
    expect(await urllinkDeleteDialog.getDialogTitle()).to.eq('dhApp.urllink.delete.question');
    await urllinkDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(urllinkComponentsPage.title), 5000);

    expect(await urllinkComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
