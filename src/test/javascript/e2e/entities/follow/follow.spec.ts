import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FollowComponentsPage, FollowDeleteDialog, FollowUpdatePage } from './follow.page-object';

const expect = chai.expect;

describe('Follow e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let followComponentsPage: FollowComponentsPage;
  let followUpdatePage: FollowUpdatePage;
  let followDeleteDialog: FollowDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Follows', async () => {
    await navBarPage.goToEntity('follow');
    followComponentsPage = new FollowComponentsPage();
    await browser.wait(ec.visibilityOf(followComponentsPage.title), 5000);
    expect(await followComponentsPage.getTitle()).to.eq('dhApp.follow.home.title');
    await browser.wait(ec.or(ec.visibilityOf(followComponentsPage.entities), ec.visibilityOf(followComponentsPage.noResult)), 1000);
  });

  it('should load create Follow page', async () => {
    await followComponentsPage.clickOnCreateButton();
    followUpdatePage = new FollowUpdatePage();
    expect(await followUpdatePage.getPageTitle()).to.eq('dhApp.follow.home.createOrEditLabel');
    await followUpdatePage.cancel();
  });

  it('should create and save Follows', async () => {
    const nbButtonsBeforeCreate = await followComponentsPage.countDeleteButtons();

    await followComponentsPage.clickOnCreateButton();

    await promise.all([
      followUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      followUpdatePage.followedSelectLastOption(),
      followUpdatePage.followingSelectLastOption(),
      followUpdatePage.cfollowedSelectLastOption(),
      followUpdatePage.cfollowingSelectLastOption(),
    ]);

    await followUpdatePage.save();
    expect(await followUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await followComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Follow', async () => {
    const nbButtonsBeforeDelete = await followComponentsPage.countDeleteButtons();
    await followComponentsPage.clickOnLastDeleteButton();

    followDeleteDialog = new FollowDeleteDialog();
    expect(await followDeleteDialog.getDialogTitle()).to.eq('dhApp.follow.delete.question');
    await followDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(followComponentsPage.title), 5000);

    expect(await followComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
