import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  CommunityComponentsPage,
  /* CommunityDeleteDialog, */
  CommunityUpdatePage,
} from './community.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Community e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let communityComponentsPage: CommunityComponentsPage;
  let communityUpdatePage: CommunityUpdatePage;
  /* let communityDeleteDialog: CommunityDeleteDialog; */
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

  it('should load Communities', async () => {
    await navBarPage.goToEntity('community');
    communityComponentsPage = new CommunityComponentsPage();
    await browser.wait(ec.visibilityOf(communityComponentsPage.title), 5000);
    expect(await communityComponentsPage.getTitle()).to.eq('dhApp.community.home.title');
    await browser.wait(ec.or(ec.visibilityOf(communityComponentsPage.entities), ec.visibilityOf(communityComponentsPage.noResult)), 1000);
  });

  it('should load create Community page', async () => {
    await communityComponentsPage.clickOnCreateButton();
    communityUpdatePage = new CommunityUpdatePage();
    expect(await communityUpdatePage.getPageTitle()).to.eq('dhApp.community.home.createOrEditLabel');
    await communityUpdatePage.cancel();
  });

  /* it('should create and save Communities', async () => {
        const nbButtonsBeforeCreate = await communityComponentsPage.countDeleteButtons();

        await communityComponentsPage.clickOnCreateButton();

        await promise.all([
            communityUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            communityUpdatePage.setCommunityNameInput('communityName'),
            communityUpdatePage.setCommunityDescriptionInput('communityDescription'),
            communityUpdatePage.setImageInput(absolutePath),
            communityUpdatePage.getIsActiveInput().click(),
            communityUpdatePage.appuserSelectLastOption(),
        ]);

        await communityUpdatePage.save();
        expect(await communityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await communityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Community', async () => {
        const nbButtonsBeforeDelete = await communityComponentsPage.countDeleteButtons();
        await communityComponentsPage.clickOnLastDeleteButton();

        communityDeleteDialog = new CommunityDeleteDialog();
        expect(await communityDeleteDialog.getDialogTitle())
            .to.eq('dhApp.community.delete.question');
        await communityDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(communityComponentsPage.title), 5000);

        expect(await communityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
