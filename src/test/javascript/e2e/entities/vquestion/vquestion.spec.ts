import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  VquestionComponentsPage,
  /* VquestionDeleteDialog, */
  VquestionUpdatePage,
} from './vquestion.page-object';

const expect = chai.expect;

describe('Vquestion e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let vquestionComponentsPage: VquestionComponentsPage;
  let vquestionUpdatePage: VquestionUpdatePage;
  /* let vquestionDeleteDialog: VquestionDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Vquestions', async () => {
    await navBarPage.goToEntity('vquestion');
    vquestionComponentsPage = new VquestionComponentsPage();
    await browser.wait(ec.visibilityOf(vquestionComponentsPage.title), 5000);
    expect(await vquestionComponentsPage.getTitle()).to.eq('dhApp.vquestion.home.title');
    await browser.wait(ec.or(ec.visibilityOf(vquestionComponentsPage.entities), ec.visibilityOf(vquestionComponentsPage.noResult)), 1000);
  });

  it('should load create Vquestion page', async () => {
    await vquestionComponentsPage.clickOnCreateButton();
    vquestionUpdatePage = new VquestionUpdatePage();
    expect(await vquestionUpdatePage.getPageTitle()).to.eq('dhApp.vquestion.home.createOrEditLabel');
    await vquestionUpdatePage.cancel();
  });

  /* it('should create and save Vquestions', async () => {
        const nbButtonsBeforeCreate = await vquestionComponentsPage.countDeleteButtons();

        await vquestionComponentsPage.clickOnCreateButton();

        await promise.all([
            vquestionUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            vquestionUpdatePage.setVquestionInput('vquestion'),
            vquestionUpdatePage.setVquestionDescriptionInput('vquestionDescription'),
            vquestionUpdatePage.appuserSelectLastOption(),
            vquestionUpdatePage.vtopicSelectLastOption(),
        ]);

        await vquestionUpdatePage.save();
        expect(await vquestionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await vquestionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Vquestion', async () => {
        const nbButtonsBeforeDelete = await vquestionComponentsPage.countDeleteButtons();
        await vquestionComponentsPage.clickOnLastDeleteButton();

        vquestionDeleteDialog = new VquestionDeleteDialog();
        expect(await vquestionDeleteDialog.getDialogTitle())
            .to.eq('dhApp.vquestion.delete.question');
        await vquestionDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(vquestionComponentsPage.title), 5000);

        expect(await vquestionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
