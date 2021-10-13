import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FrontpageconfigComponentsPage, FrontpageconfigDeleteDialog, FrontpageconfigUpdatePage } from './frontpageconfig.page-object';

const expect = chai.expect;

describe('Frontpageconfig e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let frontpageconfigComponentsPage: FrontpageconfigComponentsPage;
  let frontpageconfigUpdatePage: FrontpageconfigUpdatePage;
  let frontpageconfigDeleteDialog: FrontpageconfigDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Frontpageconfigs', async () => {
    await navBarPage.goToEntity('frontpageconfig');
    frontpageconfigComponentsPage = new FrontpageconfigComponentsPage();
    await browser.wait(ec.visibilityOf(frontpageconfigComponentsPage.title), 5000);
    expect(await frontpageconfigComponentsPage.getTitle()).to.eq('dhApp.frontpageconfig.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(frontpageconfigComponentsPage.entities), ec.visibilityOf(frontpageconfigComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Frontpageconfig page', async () => {
    await frontpageconfigComponentsPage.clickOnCreateButton();
    frontpageconfigUpdatePage = new FrontpageconfigUpdatePage();
    expect(await frontpageconfigUpdatePage.getPageTitle()).to.eq('dhApp.frontpageconfig.home.createOrEditLabel');
    await frontpageconfigUpdatePage.cancel();
  });

  it('should create and save Frontpageconfigs', async () => {
    const nbButtonsBeforeCreate = await frontpageconfigComponentsPage.countDeleteButtons();

    await frontpageconfigComponentsPage.clickOnCreateButton();

    await promise.all([
      frontpageconfigUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      frontpageconfigUpdatePage.setTopNews1Input('5'),
      frontpageconfigUpdatePage.setTopNews2Input('5'),
      frontpageconfigUpdatePage.setTopNews3Input('5'),
      frontpageconfigUpdatePage.setTopNews4Input('5'),
      frontpageconfigUpdatePage.setTopNews5Input('5'),
      frontpageconfigUpdatePage.setLatestNews1Input('5'),
      frontpageconfigUpdatePage.setLatestNews2Input('5'),
      frontpageconfigUpdatePage.setLatestNews3Input('5'),
      frontpageconfigUpdatePage.setLatestNews4Input('5'),
      frontpageconfigUpdatePage.setLatestNews5Input('5'),
      frontpageconfigUpdatePage.setBreakingNews1Input('5'),
      frontpageconfigUpdatePage.setRecentPosts1Input('5'),
      frontpageconfigUpdatePage.setRecentPosts2Input('5'),
      frontpageconfigUpdatePage.setRecentPosts3Input('5'),
      frontpageconfigUpdatePage.setRecentPosts4Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles1Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles2Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles3Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles4Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles5Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles6Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles7Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles8Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles9Input('5'),
      frontpageconfigUpdatePage.setFeaturedArticles10Input('5'),
      frontpageconfigUpdatePage.setPopularNews1Input('5'),
      frontpageconfigUpdatePage.setPopularNews2Input('5'),
      frontpageconfigUpdatePage.setPopularNews3Input('5'),
      frontpageconfigUpdatePage.setPopularNews4Input('5'),
      frontpageconfigUpdatePage.setPopularNews5Input('5'),
      frontpageconfigUpdatePage.setPopularNews6Input('5'),
      frontpageconfigUpdatePage.setPopularNews7Input('5'),
      frontpageconfigUpdatePage.setPopularNews8Input('5'),
      frontpageconfigUpdatePage.setWeeklyNews1Input('5'),
      frontpageconfigUpdatePage.setWeeklyNews2Input('5'),
      frontpageconfigUpdatePage.setWeeklyNews3Input('5'),
      frontpageconfigUpdatePage.setWeeklyNews4Input('5'),
      frontpageconfigUpdatePage.setNewsFeeds1Input('5'),
      frontpageconfigUpdatePage.setNewsFeeds2Input('5'),
      frontpageconfigUpdatePage.setNewsFeeds3Input('5'),
      frontpageconfigUpdatePage.setNewsFeeds4Input('5'),
      frontpageconfigUpdatePage.setNewsFeeds5Input('5'),
      frontpageconfigUpdatePage.setNewsFeeds6Input('5'),
      frontpageconfigUpdatePage.setUsefulLinks1Input('5'),
      frontpageconfigUpdatePage.setUsefulLinks2Input('5'),
      frontpageconfigUpdatePage.setUsefulLinks3Input('5'),
      frontpageconfigUpdatePage.setUsefulLinks4Input('5'),
      frontpageconfigUpdatePage.setUsefulLinks5Input('5'),
      frontpageconfigUpdatePage.setUsefulLinks6Input('5'),
      frontpageconfigUpdatePage.setRecentVideos1Input('5'),
      frontpageconfigUpdatePage.setRecentVideos2Input('5'),
      frontpageconfigUpdatePage.setRecentVideos3Input('5'),
      frontpageconfigUpdatePage.setRecentVideos4Input('5'),
      frontpageconfigUpdatePage.setRecentVideos5Input('5'),
      frontpageconfigUpdatePage.setRecentVideos6Input('5'),
    ]);

    await frontpageconfigUpdatePage.save();
    expect(await frontpageconfigUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await frontpageconfigComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last Frontpageconfig', async () => {
    const nbButtonsBeforeDelete = await frontpageconfigComponentsPage.countDeleteButtons();
    await frontpageconfigComponentsPage.clickOnLastDeleteButton();

    frontpageconfigDeleteDialog = new FrontpageconfigDeleteDialog();
    expect(await frontpageconfigDeleteDialog.getDialogTitle()).to.eq('dhApp.frontpageconfig.delete.question');
    await frontpageconfigDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(frontpageconfigComponentsPage.title), 5000);

    expect(await frontpageconfigComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
