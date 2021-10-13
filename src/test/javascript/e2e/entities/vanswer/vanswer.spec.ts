import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  VanswerComponentsPage,
  /* VanswerDeleteDialog, */
  VanswerUpdatePage,
} from './vanswer.page-object';

const expect = chai.expect;

describe('Vanswer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let vanswerComponentsPage: VanswerComponentsPage;
  let vanswerUpdatePage: VanswerUpdatePage;
  /* let vanswerDeleteDialog: VanswerDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Vanswers', async () => {
    await navBarPage.goToEntity('vanswer');
    vanswerComponentsPage = new VanswerComponentsPage();
    await browser.wait(ec.visibilityOf(vanswerComponentsPage.title), 5000);
    expect(await vanswerComponentsPage.getTitle()).to.eq('dhApp.vanswer.home.title');
    await browser.wait(ec.or(ec.visibilityOf(vanswerComponentsPage.entities), ec.visibilityOf(vanswerComponentsPage.noResult)), 1000);
  });

  it('should load create Vanswer page', async () => {
    await vanswerComponentsPage.clickOnCreateButton();
    vanswerUpdatePage = new VanswerUpdatePage();
    expect(await vanswerUpdatePage.getPageTitle()).to.eq('dhApp.vanswer.home.createOrEditLabel');
    await vanswerUpdatePage.cancel();
  });

  /* it('should create and save Vanswers', async () => {
        const nbButtonsBeforeCreate = await vanswerComponentsPage.countDeleteButtons();

        await vanswerComponentsPage.clickOnCreateButton();

        await promise.all([
            vanswerUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            vanswerUpdatePage.setUrlVanswerInput('urlVanswer'),
            vanswerUpdatePage.getAcceptedInput().click(),
            vanswerUpdatePage.appuserSelectLastOption(),
            vanswerUpdatePage.vquestionSelectLastOption(),
        ]);

        await vanswerUpdatePage.save();
        expect(await vanswerUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await vanswerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Vanswer', async () => {
        const nbButtonsBeforeDelete = await vanswerComponentsPage.countDeleteButtons();
        await vanswerComponentsPage.clickOnLastDeleteButton();

        vanswerDeleteDialog = new VanswerDeleteDialog();
        expect(await vanswerDeleteDialog.getDialogTitle())
            .to.eq('dhApp.vanswer.delete.question');
        await vanswerDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(vanswerComponentsPage.title), 5000);

        expect(await vanswerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
