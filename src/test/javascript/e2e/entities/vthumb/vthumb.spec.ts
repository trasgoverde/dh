import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  VthumbComponentsPage,
  /* VthumbDeleteDialog, */
  VthumbUpdatePage,
} from './vthumb.page-object';

const expect = chai.expect;

describe('Vthumb e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let vthumbComponentsPage: VthumbComponentsPage;
  let vthumbUpdatePage: VthumbUpdatePage;
  /* let vthumbDeleteDialog: VthumbDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Vthumbs', async () => {
    await navBarPage.goToEntity('vthumb');
    vthumbComponentsPage = new VthumbComponentsPage();
    await browser.wait(ec.visibilityOf(vthumbComponentsPage.title), 5000);
    expect(await vthumbComponentsPage.getTitle()).to.eq('dhApp.vthumb.home.title');
    await browser.wait(ec.or(ec.visibilityOf(vthumbComponentsPage.entities), ec.visibilityOf(vthumbComponentsPage.noResult)), 1000);
  });

  it('should load create Vthumb page', async () => {
    await vthumbComponentsPage.clickOnCreateButton();
    vthumbUpdatePage = new VthumbUpdatePage();
    expect(await vthumbUpdatePage.getPageTitle()).to.eq('dhApp.vthumb.home.createOrEditLabel');
    await vthumbUpdatePage.cancel();
  });

  /* it('should create and save Vthumbs', async () => {
        const nbButtonsBeforeCreate = await vthumbComponentsPage.countDeleteButtons();

        await vthumbComponentsPage.clickOnCreateButton();

        await promise.all([
            vthumbUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            vthumbUpdatePage.getVthumbUpInput().click(),
            vthumbUpdatePage.getVthumbDownInput().click(),
            vthumbUpdatePage.appuserSelectLastOption(),
            vthumbUpdatePage.vquestionSelectLastOption(),
            vthumbUpdatePage.vanswerSelectLastOption(),
        ]);

        await vthumbUpdatePage.save();
        expect(await vthumbUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await vthumbComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Vthumb', async () => {
        const nbButtonsBeforeDelete = await vthumbComponentsPage.countDeleteButtons();
        await vthumbComponentsPage.clickOnLastDeleteButton();

        vthumbDeleteDialog = new VthumbDeleteDialog();
        expect(await vthumbDeleteDialog.getDialogTitle())
            .to.eq('dhApp.vthumb.delete.question');
        await vthumbDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(vthumbComponentsPage.title), 5000);

        expect(await vthumbComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
