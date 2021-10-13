import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NewsletterComponentsPage, NewsletterDeleteDialog, NewsletterUpdatePage } from './newsletter.page-object';

const expect = chai.expect;

describe('Newsletter e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let newsletterComponentsPage: NewsletterComponentsPage;
  let newsletterUpdatePage: NewsletterUpdatePage;
  let newsletterDeleteDialog: NewsletterDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Newsletters', async () => {
    await navBarPage.goToEntity('newsletter');
    newsletterComponentsPage = new NewsletterComponentsPage();
    await browser.wait(ec.visibilityOf(newsletterComponentsPage.title), 5000);
    expect(await newsletterComponentsPage.getTitle()).to.eq('dhApp.newsletter.home.title');
    await browser.wait(ec.or(ec.visibilityOf(newsletterComponentsPage.entities), ec.visibilityOf(newsletterComponentsPage.noResult)), 1000);
  });

  it('should load create Newsletter page', async () => {
    await newsletterComponentsPage.clickOnCreateButton();
    newsletterUpdatePage = new NewsletterUpdatePage();
    expect(await newsletterUpdatePage.getPageTitle()).to.eq('dhApp.newsletter.home.createOrEditLabel');
    await newsletterUpdatePage.cancel();
  });

  it('should create and save Newsletters', async () => {
    const nbButtonsBeforeCreate = await newsletterComponentsPage.countDeleteButtons();

    await newsletterComponentsPage.clickOnCreateButton();

    await promise.all([
      newsletterUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      newsletterUpdatePage.setEmailInput('email'),
    ]);

    await newsletterUpdatePage.save();
    expect(await newsletterUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await newsletterComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Newsletter', async () => {
    const nbButtonsBeforeDelete = await newsletterComponentsPage.countDeleteButtons();
    await newsletterComponentsPage.clickOnLastDeleteButton();

    newsletterDeleteDialog = new NewsletterDeleteDialog();
    expect(await newsletterDeleteDialog.getDialogTitle()).to.eq('dhApp.newsletter.delete.question');
    await newsletterDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(newsletterComponentsPage.title), 5000);

    expect(await newsletterComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
