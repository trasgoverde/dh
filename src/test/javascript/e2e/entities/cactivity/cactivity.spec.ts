import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CactivityComponentsPage, CactivityDeleteDialog, CactivityUpdatePage } from './cactivity.page-object';

const expect = chai.expect;

describe('Cactivity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cactivityComponentsPage: CactivityComponentsPage;
  let cactivityUpdatePage: CactivityUpdatePage;
  let cactivityDeleteDialog: CactivityDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cactivities', async () => {
    await navBarPage.goToEntity('cactivity');
    cactivityComponentsPage = new CactivityComponentsPage();
    await browser.wait(ec.visibilityOf(cactivityComponentsPage.title), 5000);
    expect(await cactivityComponentsPage.getTitle()).to.eq('dhApp.cactivity.home.title');
    await browser.wait(ec.or(ec.visibilityOf(cactivityComponentsPage.entities), ec.visibilityOf(cactivityComponentsPage.noResult)), 1000);
  });

  it('should load create Cactivity page', async () => {
    await cactivityComponentsPage.clickOnCreateButton();
    cactivityUpdatePage = new CactivityUpdatePage();
    expect(await cactivityUpdatePage.getPageTitle()).to.eq('dhApp.cactivity.home.createOrEditLabel');
    await cactivityUpdatePage.cancel();
  });

  it('should create and save Cactivities', async () => {
    const nbButtonsBeforeCreate = await cactivityComponentsPage.countDeleteButtons();

    await cactivityComponentsPage.clickOnCreateButton();

    await promise.all([
      cactivityUpdatePage.setActivityNameInput('activityName'),
      // cactivityUpdatePage.communitySelectLastOption(),
    ]);

    await cactivityUpdatePage.save();
    expect(await cactivityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cactivityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Cactivity', async () => {
    const nbButtonsBeforeDelete = await cactivityComponentsPage.countDeleteButtons();
    await cactivityComponentsPage.clickOnLastDeleteButton();

    cactivityDeleteDialog = new CactivityDeleteDialog();
    expect(await cactivityDeleteDialog.getDialogTitle()).to.eq('dhApp.cactivity.delete.question');
    await cactivityDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cactivityComponentsPage.title), 5000);

    expect(await cactivityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
