import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  AppphotoComponentsPage,
  /* AppphotoDeleteDialog, */
  AppphotoUpdatePage,
} from './appphoto.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Appphoto e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let appphotoComponentsPage: AppphotoComponentsPage;
  let appphotoUpdatePage: AppphotoUpdatePage;
  /* let appphotoDeleteDialog: AppphotoDeleteDialog; */
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Appphotos', async () => {
    await navBarPage.goToEntity('appphoto');
    appphotoComponentsPage = new AppphotoComponentsPage();
    await browser.wait(ec.visibilityOf(appphotoComponentsPage.title), 5000);
    expect(await appphotoComponentsPage.getTitle()).to.eq('dhApp.appphoto.home.title');
    await browser.wait(ec.or(ec.visibilityOf(appphotoComponentsPage.entities), ec.visibilityOf(appphotoComponentsPage.noResult)), 1000);
  });

  it('should load create Appphoto page', async () => {
    await appphotoComponentsPage.clickOnCreateButton();
    appphotoUpdatePage = new AppphotoUpdatePage();
    expect(await appphotoUpdatePage.getPageTitle()).to.eq('dhApp.appphoto.home.createOrEditLabel');
    await appphotoUpdatePage.cancel();
  });

  /* it('should create and save Appphotos', async () => {
        const nbButtonsBeforeCreate = await appphotoComponentsPage.countDeleteButtons();

        await appphotoComponentsPage.clickOnCreateButton();

        await promise.all([
            appphotoUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            appphotoUpdatePage.setImageInput(absolutePath),
            appphotoUpdatePage.appuserSelectLastOption(),
        ]);

        await appphotoUpdatePage.save();
        expect(await appphotoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await appphotoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Appphoto', async () => {
        const nbButtonsBeforeDelete = await appphotoComponentsPage.countDeleteButtons();
        await appphotoComponentsPage.clickOnLastDeleteButton();

        appphotoDeleteDialog = new AppphotoDeleteDialog();
        expect(await appphotoDeleteDialog.getDialogTitle())
            .to.eq('dhApp.appphoto.delete.question');
        await appphotoDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(appphotoComponentsPage.title), 5000);

        expect(await appphotoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
