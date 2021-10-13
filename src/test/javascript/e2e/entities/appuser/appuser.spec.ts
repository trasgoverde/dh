import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  AppuserComponentsPage,
  /* AppuserDeleteDialog, */
  AppuserUpdatePage,
} from './appuser.page-object';

const expect = chai.expect;

describe('Appuser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let appuserComponentsPage: AppuserComponentsPage;
  let appuserUpdatePage: AppuserUpdatePage;
  /* let appuserDeleteDialog: AppuserDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Appusers', async () => {
    await navBarPage.goToEntity('appuser');
    appuserComponentsPage = new AppuserComponentsPage();
    await browser.wait(ec.visibilityOf(appuserComponentsPage.title), 5000);
    expect(await appuserComponentsPage.getTitle()).to.eq('dhApp.appuser.home.title');
    await browser.wait(ec.or(ec.visibilityOf(appuserComponentsPage.entities), ec.visibilityOf(appuserComponentsPage.noResult)), 1000);
  });

  it('should load create Appuser page', async () => {
    await appuserComponentsPage.clickOnCreateButton();
    appuserUpdatePage = new AppuserUpdatePage();
    expect(await appuserUpdatePage.getPageTitle()).to.eq('dhApp.appuser.home.createOrEditLabel');
    await appuserUpdatePage.cancel();
  });

  /* it('should create and save Appusers', async () => {
        const nbButtonsBeforeCreate = await appuserComponentsPage.countDeleteButtons();

        await appuserComponentsPage.clickOnCreateButton();

        await promise.all([
            appuserUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            appuserUpdatePage.setAssignedVotesPointsInput('5'),
            appuserUpdatePage.userSelectLastOption(),
        ]);

        await appuserUpdatePage.save();
        expect(await appuserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await appuserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Appuser', async () => {
        const nbButtonsBeforeDelete = await appuserComponentsPage.countDeleteButtons();
        await appuserComponentsPage.clickOnLastDeleteButton();

        appuserDeleteDialog = new AppuserDeleteDialog();
        expect(await appuserDeleteDialog.getDialogTitle())
            .to.eq('dhApp.appuser.delete.question');
        await appuserDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(appuserComponentsPage.title), 5000);

        expect(await appuserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
