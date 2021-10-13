import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  BlogComponentsPage,
  /* BlogDeleteDialog, */
  BlogUpdatePage,
} from './blog.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Blog e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let blogComponentsPage: BlogComponentsPage;
  let blogUpdatePage: BlogUpdatePage;
  /* let blogDeleteDialog: BlogDeleteDialog; */
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

  it('should load Blogs', async () => {
    await navBarPage.goToEntity('blog');
    blogComponentsPage = new BlogComponentsPage();
    await browser.wait(ec.visibilityOf(blogComponentsPage.title), 5000);
    expect(await blogComponentsPage.getTitle()).to.eq('dhApp.blog.home.title');
    await browser.wait(ec.or(ec.visibilityOf(blogComponentsPage.entities), ec.visibilityOf(blogComponentsPage.noResult)), 1000);
  });

  it('should load create Blog page', async () => {
    await blogComponentsPage.clickOnCreateButton();
    blogUpdatePage = new BlogUpdatePage();
    expect(await blogUpdatePage.getPageTitle()).to.eq('dhApp.blog.home.createOrEditLabel');
    await blogUpdatePage.cancel();
  });

  /* it('should create and save Blogs', async () => {
        const nbButtonsBeforeCreate = await blogComponentsPage.countDeleteButtons();

        await blogComponentsPage.clickOnCreateButton();

        await promise.all([
            blogUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            blogUpdatePage.setTitleInput('title'),
            blogUpdatePage.setImageInput(absolutePath),
            blogUpdatePage.appuserSelectLastOption(),
            blogUpdatePage.communitySelectLastOption(),
        ]);

        await blogUpdatePage.save();
        expect(await blogUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await blogComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Blog', async () => {
        const nbButtonsBeforeDelete = await blogComponentsPage.countDeleteButtons();
        await blogComponentsPage.clickOnLastDeleteButton();

        blogDeleteDialog = new BlogDeleteDialog();
        expect(await blogDeleteDialog.getDialogTitle())
            .to.eq('dhApp.blog.delete.question');
        await blogDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(blogComponentsPage.title), 5000);

        expect(await blogComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
