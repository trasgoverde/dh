import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  PostComponentsPage,
  /* PostDeleteDialog, */
  PostUpdatePage,
} from './post.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Post e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let postComponentsPage: PostComponentsPage;
  let postUpdatePage: PostUpdatePage;
  /* let postDeleteDialog: PostDeleteDialog; */
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

  it('should load Posts', async () => {
    await navBarPage.goToEntity('post');
    postComponentsPage = new PostComponentsPage();
    await browser.wait(ec.visibilityOf(postComponentsPage.title), 5000);
    expect(await postComponentsPage.getTitle()).to.eq('dhApp.post.home.title');
    await browser.wait(ec.or(ec.visibilityOf(postComponentsPage.entities), ec.visibilityOf(postComponentsPage.noResult)), 1000);
  });

  it('should load create Post page', async () => {
    await postComponentsPage.clickOnCreateButton();
    postUpdatePage = new PostUpdatePage();
    expect(await postUpdatePage.getPageTitle()).to.eq('dhApp.post.home.createOrEditLabel');
    await postUpdatePage.cancel();
  });

  /* it('should create and save Posts', async () => {
        const nbButtonsBeforeCreate = await postComponentsPage.countDeleteButtons();

        await postComponentsPage.clickOnCreateButton();

        await promise.all([
            postUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            postUpdatePage.setPublicationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            postUpdatePage.setHeadlineInput('headline'),
            postUpdatePage.setLeadtextInput('leadtext'),
            postUpdatePage.setBodytextInput('bodytext'),
            postUpdatePage.setQuoteInput('quote'),
            postUpdatePage.setConclusionInput('conclusion'),
            postUpdatePage.setLinkTextInput('linkText'),
            postUpdatePage.setLinkURLInput('linkURL'),
            postUpdatePage.setImageInput(absolutePath),
            postUpdatePage.appuserSelectLastOption(),
            postUpdatePage.blogSelectLastOption(),
        ]);

        await postUpdatePage.save();
        expect(await postUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await postComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Post', async () => {
        const nbButtonsBeforeDelete = await postComponentsPage.countDeleteButtons();
        await postComponentsPage.clickOnLastDeleteButton();

        postDeleteDialog = new PostDeleteDialog();
        expect(await postDeleteDialog.getDialogTitle())
            .to.eq('dhApp.post.delete.question');
        await postDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(postComponentsPage.title), 5000);

        expect(await postComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
