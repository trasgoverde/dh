import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { InterestComponentsPage, InterestDeleteDialog, InterestUpdatePage } from './interest.page-object';

const expect = chai.expect;

describe('Interest e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let interestComponentsPage: InterestComponentsPage;
  let interestUpdatePage: InterestUpdatePage;
  let interestDeleteDialog: InterestDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Interests', async () => {
    await navBarPage.goToEntity('interest');
    interestComponentsPage = new InterestComponentsPage();
    await browser.wait(ec.visibilityOf(interestComponentsPage.title), 5000);
    expect(await interestComponentsPage.getTitle()).to.eq('dhApp.interest.home.title');
    await browser.wait(ec.or(ec.visibilityOf(interestComponentsPage.entities), ec.visibilityOf(interestComponentsPage.noResult)), 1000);
  });

  it('should load create Interest page', async () => {
    await interestComponentsPage.clickOnCreateButton();
    interestUpdatePage = new InterestUpdatePage();
    expect(await interestUpdatePage.getPageTitle()).to.eq('dhApp.interest.home.createOrEditLabel');
    await interestUpdatePage.cancel();
  });

  it('should create and save Interests', async () => {
    const nbButtonsBeforeCreate = await interestComponentsPage.countDeleteButtons();

    await interestComponentsPage.clickOnCreateButton();

    await promise.all([
      interestUpdatePage.setInterestNameInput('interestName'),
      // interestUpdatePage.appuserSelectLastOption(),
    ]);

    await interestUpdatePage.save();
    expect(await interestUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await interestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Interest', async () => {
    const nbButtonsBeforeDelete = await interestComponentsPage.countDeleteButtons();
    await interestComponentsPage.clickOnLastDeleteButton();

    interestDeleteDialog = new InterestDeleteDialog();
    expect(await interestDeleteDialog.getDialogTitle()).to.eq('dhApp.interest.delete.question');
    await interestDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(interestComponentsPage.title), 5000);

    expect(await interestComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
