import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CelebComponentsPage, CelebDeleteDialog, CelebUpdatePage } from './celeb.page-object';

const expect = chai.expect;

describe('Celeb e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let celebComponentsPage: CelebComponentsPage;
  let celebUpdatePage: CelebUpdatePage;
  let celebDeleteDialog: CelebDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Celebs', async () => {
    await navBarPage.goToEntity('celeb');
    celebComponentsPage = new CelebComponentsPage();
    await browser.wait(ec.visibilityOf(celebComponentsPage.title), 5000);
    expect(await celebComponentsPage.getTitle()).to.eq('dhApp.celeb.home.title');
    await browser.wait(ec.or(ec.visibilityOf(celebComponentsPage.entities), ec.visibilityOf(celebComponentsPage.noResult)), 1000);
  });

  it('should load create Celeb page', async () => {
    await celebComponentsPage.clickOnCreateButton();
    celebUpdatePage = new CelebUpdatePage();
    expect(await celebUpdatePage.getPageTitle()).to.eq('dhApp.celeb.home.createOrEditLabel');
    await celebUpdatePage.cancel();
  });

  it('should create and save Celebs', async () => {
    const nbButtonsBeforeCreate = await celebComponentsPage.countDeleteButtons();

    await celebComponentsPage.clickOnCreateButton();

    await promise.all([
      celebUpdatePage.setCelebNameInput('celebName'),
      // celebUpdatePage.appuserSelectLastOption(),
    ]);

    await celebUpdatePage.save();
    expect(await celebUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await celebComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Celeb', async () => {
    const nbButtonsBeforeDelete = await celebComponentsPage.countDeleteButtons();
    await celebComponentsPage.clickOnLastDeleteButton();

    celebDeleteDialog = new CelebDeleteDialog();
    expect(await celebDeleteDialog.getDialogTitle()).to.eq('dhApp.celeb.delete.question');
    await celebDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(celebComponentsPage.title), 5000);

    expect(await celebComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
