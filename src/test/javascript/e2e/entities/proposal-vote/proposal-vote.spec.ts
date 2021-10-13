import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProposalVoteComponentsPage, ProposalVoteDeleteDialog, ProposalVoteUpdatePage } from './proposal-vote.page-object';

const expect = chai.expect;

describe('ProposalVote e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let proposalVoteComponentsPage: ProposalVoteComponentsPage;
  let proposalVoteUpdatePage: ProposalVoteUpdatePage;
  let proposalVoteDeleteDialog: ProposalVoteDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProposalVotes', async () => {
    await navBarPage.goToEntity('proposal-vote');
    proposalVoteComponentsPage = new ProposalVoteComponentsPage();
    await browser.wait(ec.visibilityOf(proposalVoteComponentsPage.title), 5000);
    expect(await proposalVoteComponentsPage.getTitle()).to.eq('dhApp.proposalVote.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(proposalVoteComponentsPage.entities), ec.visibilityOf(proposalVoteComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ProposalVote page', async () => {
    await proposalVoteComponentsPage.clickOnCreateButton();
    proposalVoteUpdatePage = new ProposalVoteUpdatePage();
    expect(await proposalVoteUpdatePage.getPageTitle()).to.eq('dhApp.proposalVote.home.createOrEditLabel');
    await proposalVoteUpdatePage.cancel();
  });

  it('should create and save ProposalVotes', async () => {
    const nbButtonsBeforeCreate = await proposalVoteComponentsPage.countDeleteButtons();

    await proposalVoteComponentsPage.clickOnCreateButton();

    await promise.all([
      proposalVoteUpdatePage.setCreationDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      proposalVoteUpdatePage.setVotePointsInput('5'),
      proposalVoteUpdatePage.appuserSelectLastOption(),
      proposalVoteUpdatePage.proposalSelectLastOption(),
    ]);

    await proposalVoteUpdatePage.save();
    expect(await proposalVoteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await proposalVoteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ProposalVote', async () => {
    const nbButtonsBeforeDelete = await proposalVoteComponentsPage.countDeleteButtons();
    await proposalVoteComponentsPage.clickOnLastDeleteButton();

    proposalVoteDeleteDialog = new ProposalVoteDeleteDialog();
    expect(await proposalVoteDeleteDialog.getDialogTitle()).to.eq('dhApp.proposalVote.delete.question');
    await proposalVoteDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(proposalVoteComponentsPage.title), 5000);

    expect(await proposalVoteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
