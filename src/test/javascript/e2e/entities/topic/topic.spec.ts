import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TopicComponentsPage, TopicDeleteDialog, TopicUpdatePage } from './topic.page-object';

const expect = chai.expect;

describe('Topic e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let topicComponentsPage: TopicComponentsPage;
  let topicUpdatePage: TopicUpdatePage;
  let topicDeleteDialog: TopicDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Topics', async () => {
    await navBarPage.goToEntity('topic');
    topicComponentsPage = new TopicComponentsPage();
    await browser.wait(ec.visibilityOf(topicComponentsPage.title), 5000);
    expect(await topicComponentsPage.getTitle()).to.eq('dhApp.topic.home.title');
    await browser.wait(ec.or(ec.visibilityOf(topicComponentsPage.entities), ec.visibilityOf(topicComponentsPage.noResult)), 1000);
  });

  it('should load create Topic page', async () => {
    await topicComponentsPage.clickOnCreateButton();
    topicUpdatePage = new TopicUpdatePage();
    expect(await topicUpdatePage.getPageTitle()).to.eq('dhApp.topic.home.createOrEditLabel');
    await topicUpdatePage.cancel();
  });

  it('should create and save Topics', async () => {
    const nbButtonsBeforeCreate = await topicComponentsPage.countDeleteButtons();

    await topicComponentsPage.clickOnCreateButton();

    await promise.all([
      topicUpdatePage.setTopicNameInput('topicName'),
      // topicUpdatePage.postSelectLastOption(),
    ]);

    await topicUpdatePage.save();
    expect(await topicUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await topicComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Topic', async () => {
    const nbButtonsBeforeDelete = await topicComponentsPage.countDeleteButtons();
    await topicComponentsPage.clickOnLastDeleteButton();

    topicDeleteDialog = new TopicDeleteDialog();
    expect(await topicDeleteDialog.getDialogTitle()).to.eq('dhApp.topic.delete.question');
    await topicDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(topicComponentsPage.title), 5000);

    expect(await topicComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
