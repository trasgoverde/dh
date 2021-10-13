import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  VtopicComponentsPage,
  /* VtopicDeleteDialog, */
  VtopicUpdatePage,
} from './vtopic.page-object';

const expect = chai.expect;

describe('Vtopic e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let vtopicComponentsPage: VtopicComponentsPage;
  let vtopicUpdatePage: VtopicUpdatePage;
  /* let vtopicDeleteDialog: VtopicDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Vtopics', async () => {
    await navBarPage.goToEntity('vtopic');
    vtopicComponentsPage = new VtopicComponentsPage();
    await browser.wait(ec.visibilityOf(vtopicComponentsPage.title), 5000);
    expect(await vtopicComponentsPage.getTitle()).to.eq('dhApp.vtopic.home.title');
    await browser.wait(ec.or(ec.visibilityOf(vtopicComponentsPage.entities), ec.visibilityOf(vtopicComponentsPage.noResult)), 1000);
  });

  it('should load create Vtopic page', async () => {
    await vtopicComponentsPage.clickOnCreateButton();
    vtopicUpdatePage = new VtopicUpdatePage();
    expect(await vtopicUpdatePage.getPageTitle()).to.eq('dhApp.vtopic.home.createOrEditLabel');
    await vtopicUpdatePage.cancel();
  });

  /* it('should create and save Vtopics', async () => {
        const nbButtonsBeforeCreate = await vtopicComponentsPage.countDeleteButtons();

        await vtopicComponentsPage.clickOnCreateButton();

        await promise.all([
            vtopicUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            vtopicUpdatePage.setVtopicTitleInput('vtopicTitle'),
            vtopicUpdatePage.setVtopicDescriptionInput('vtopicDescription'),
            vtopicUpdatePage.appuserSelectLastOption(),
        ]);

        await vtopicUpdatePage.save();
        expect(await vtopicUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await vtopicComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Vtopic', async () => {
        const nbButtonsBeforeDelete = await vtopicComponentsPage.countDeleteButtons();
        await vtopicComponentsPage.clickOnLastDeleteButton();

        vtopicDeleteDialog = new VtopicDeleteDialog();
        expect(await vtopicDeleteDialog.getDialogTitle())
            .to.eq('dhApp.vtopic.delete.question');
        await vtopicDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(vtopicComponentsPage.title), 5000);

        expect(await vtopicComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
