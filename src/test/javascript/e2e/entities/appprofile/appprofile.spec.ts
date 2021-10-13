import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  AppprofileComponentsPage,
  /* AppprofileDeleteDialog, */
  AppprofileUpdatePage,
} from './appprofile.page-object';

const expect = chai.expect;

describe('Appprofile e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let appprofileComponentsPage: AppprofileComponentsPage;
  let appprofileUpdatePage: AppprofileUpdatePage;
  /* let appprofileDeleteDialog: AppprofileDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Appprofiles', async () => {
    await navBarPage.goToEntity('appprofile');
    appprofileComponentsPage = new AppprofileComponentsPage();
    await browser.wait(ec.visibilityOf(appprofileComponentsPage.title), 5000);
    expect(await appprofileComponentsPage.getTitle()).to.eq('dhApp.appprofile.home.title');
    await browser.wait(ec.or(ec.visibilityOf(appprofileComponentsPage.entities), ec.visibilityOf(appprofileComponentsPage.noResult)), 1000);
  });

  it('should load create Appprofile page', async () => {
    await appprofileComponentsPage.clickOnCreateButton();
    appprofileUpdatePage = new AppprofileUpdatePage();
    expect(await appprofileUpdatePage.getPageTitle()).to.eq('dhApp.appprofile.home.createOrEditLabel');
    await appprofileUpdatePage.cancel();
  });

  /* it('should create and save Appprofiles', async () => {
        const nbButtonsBeforeCreate = await appprofileComponentsPage.countDeleteButtons();

        await appprofileComponentsPage.clickOnCreateButton();

        await promise.all([
            appprofileUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            appprofileUpdatePage.genderSelectLastOption(),
            appprofileUpdatePage.setPhoneInput('phone'),
            appprofileUpdatePage.setBioInput('bio'),
            appprofileUpdatePage.setFacebookInput('facebook'),
            appprofileUpdatePage.setTwitterInput('twitter'),
            appprofileUpdatePage.setLinkedinInput('linkedin'),
            appprofileUpdatePage.setInstagramInput('instagram'),
            appprofileUpdatePage.setGooglePlusInput('googlePlus'),
            appprofileUpdatePage.setBirthdateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            appprofileUpdatePage.civilStatusSelectLastOption(),
            appprofileUpdatePage.lookingForSelectLastOption(),
            appprofileUpdatePage.purposeSelectLastOption(),
            appprofileUpdatePage.physicalSelectLastOption(),
            appprofileUpdatePage.religionSelectLastOption(),
            appprofileUpdatePage.ethnicGroupSelectLastOption(),
            appprofileUpdatePage.studiesSelectLastOption(),
            appprofileUpdatePage.setSibblingsInput('5'),
            appprofileUpdatePage.eyesSelectLastOption(),
            appprofileUpdatePage.smokerSelectLastOption(),
            appprofileUpdatePage.childrenSelectLastOption(),
            appprofileUpdatePage.futureChildrenSelectLastOption(),
            appprofileUpdatePage.getPetInput().click(),
            appprofileUpdatePage.appuserSelectLastOption(),
        ]);

        await appprofileUpdatePage.save();
        expect(await appprofileUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await appprofileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Appprofile', async () => {
        const nbButtonsBeforeDelete = await appprofileComponentsPage.countDeleteButtons();
        await appprofileComponentsPage.clickOnLastDeleteButton();

        appprofileDeleteDialog = new AppprofileDeleteDialog();
        expect(await appprofileDeleteDialog.getDialogTitle())
            .to.eq('dhApp.appprofile.delete.question');
        await appprofileDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(appprofileComponentsPage.title), 5000);

        expect(await appprofileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
