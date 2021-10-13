import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BlockuserComponentsPage, BlockuserDeleteDialog, BlockuserUpdatePage } from './blockuser.page-object';

const expect = chai.expect;

describe('Blockuser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let blockuserComponentsPage: BlockuserComponentsPage;
  let blockuserUpdatePage: BlockuserUpdatePage;
  let blockuserDeleteDialog: BlockuserDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Blockusers', async () => {
    await navBarPage.goToEntity('blockuser');
    blockuserComponentsPage = new BlockuserComponentsPage();
    await browser.wait(ec.visibilityOf(blockuserComponentsPage.title), 5000);
    expect(await blockuserComponentsPage.getTitle()).to.eq('dhApp.blockuser.home.title');
    await browser.wait(ec.or(ec.visibilityOf(blockuserComponentsPage.entities), ec.visibilityOf(blockuserComponentsPage.noResult)), 1000);
  });

  it('should load create Blockuser page', async () => {
    await blockuserComponentsPage.clickOnCreateButton();
    blockuserUpdatePage = new BlockuserUpdatePage();
    expect(await blockuserUpdatePage.getPageTitle()).to.eq('dhApp.blockuser.home.createOrEditLabel');
    await blockuserUpdatePage.cancel();
  });

  it('should create and save Blockusers', async () => {
    const nbButtonsBeforeCreate = await blockuserComponentsPage.countDeleteButtons();

    await blockuserComponentsPage.clickOnCreateButton();

    await promise.all([
      blockuserUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      blockuserUpdatePage.blockeduserSelectLastOption(),
      blockuserUpdatePage.blockinguserSelectLastOption(),
      blockuserUpdatePage.cblockeduserSelectLastOption(),
      blockuserUpdatePage.cblockinguserSelectLastOption(),
    ]);

    await blockuserUpdatePage.save();
    expect(await blockuserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await blockuserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Blockuser', async () => {
    const nbButtonsBeforeDelete = await blockuserComponentsPage.countDeleteButtons();
    await blockuserComponentsPage.clickOnLastDeleteButton();

    blockuserDeleteDialog = new BlockuserDeleteDialog();
    expect(await blockuserDeleteDialog.getDialogTitle()).to.eq('dhApp.blockuser.delete.question');
    await blockuserDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(blockuserComponentsPage.title), 5000);

    expect(await blockuserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
