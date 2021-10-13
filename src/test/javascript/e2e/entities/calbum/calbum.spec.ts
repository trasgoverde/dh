import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  CalbumComponentsPage,
  /* CalbumDeleteDialog, */
  CalbumUpdatePage,
} from './calbum.page-object';

const expect = chai.expect;

describe('Calbum e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let calbumComponentsPage: CalbumComponentsPage;
  let calbumUpdatePage: CalbumUpdatePage;
  /* let calbumDeleteDialog: CalbumDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Calbums', async () => {
    await navBarPage.goToEntity('calbum');
    calbumComponentsPage = new CalbumComponentsPage();
    await browser.wait(ec.visibilityOf(calbumComponentsPage.title), 5000);
    expect(await calbumComponentsPage.getTitle()).to.eq('dhApp.calbum.home.title');
    await browser.wait(ec.or(ec.visibilityOf(calbumComponentsPage.entities), ec.visibilityOf(calbumComponentsPage.noResult)), 1000);
  });

  it('should load create Calbum page', async () => {
    await calbumComponentsPage.clickOnCreateButton();
    calbumUpdatePage = new CalbumUpdatePage();
    expect(await calbumUpdatePage.getPageTitle()).to.eq('dhApp.calbum.home.createOrEditLabel');
    await calbumUpdatePage.cancel();
  });

  /* it('should create and save Calbums', async () => {
        const nbButtonsBeforeCreate = await calbumComponentsPage.countDeleteButtons();

        await calbumComponentsPage.clickOnCreateButton();

        await promise.all([
            calbumUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            calbumUpdatePage.setTitleInput('title'),
            calbumUpdatePage.communitySelectLastOption(),
        ]);

        await calbumUpdatePage.save();
        expect(await calbumUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await calbumComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Calbum', async () => {
        const nbButtonsBeforeDelete = await calbumComponentsPage.countDeleteButtons();
        await calbumComponentsPage.clickOnLastDeleteButton();

        calbumDeleteDialog = new CalbumDeleteDialog();
        expect(await calbumDeleteDialog.getDialogTitle())
            .to.eq('dhApp.calbum.delete.question');
        await calbumDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(calbumComponentsPage.title), 5000);

        expect(await calbumComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
