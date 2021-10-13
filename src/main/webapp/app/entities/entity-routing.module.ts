import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'blog',
        data: { pageTitle: 'dhApp.blog.home.title' },
        loadChildren: () => import('./blog/blog.module').then(m => m.BlogModule),
      },
      {
        path: 'post',
        data: { pageTitle: 'dhApp.post.home.title' },
        loadChildren: () => import('./post/post.module').then(m => m.PostModule),
      },
      {
        path: 'topic',
        data: { pageTitle: 'dhApp.topic.home.title' },
        loadChildren: () => import('./topic/topic.module').then(m => m.TopicModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'dhApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'dhApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'cmessage',
        data: { pageTitle: 'dhApp.cmessage.home.title' },
        loadChildren: () => import('./cmessage/cmessage.module').then(m => m.CmessageModule),
      },
      {
        path: 'message',
        data: { pageTitle: 'dhApp.message.home.title' },
        loadChildren: () => import('./message/message.module').then(m => m.MessageModule),
      },
      {
        path: 'notification',
        data: { pageTitle: 'dhApp.notification.home.title' },
        loadChildren: () => import('./notification/notification.module').then(m => m.NotificationModule),
      },
      {
        path: 'appphoto',
        data: { pageTitle: 'dhApp.appphoto.home.title' },
        loadChildren: () => import('./appphoto/appphoto.module').then(m => m.AppphotoModule),
      },
      {
        path: 'appprofile',
        data: { pageTitle: 'dhApp.appprofile.home.title' },
        loadChildren: () => import('./appprofile/appprofile.module').then(m => m.AppprofileModule),
      },
      {
        path: 'community',
        data: { pageTitle: 'dhApp.community.home.title' },
        loadChildren: () => import('./community/community.module').then(m => m.CommunityModule),
      },
      {
        path: 'follow',
        data: { pageTitle: 'dhApp.follow.home.title' },
        loadChildren: () => import('./follow/follow.module').then(m => m.FollowModule),
      },
      {
        path: 'blockuser',
        data: { pageTitle: 'dhApp.blockuser.home.title' },
        loadChildren: () => import('./blockuser/blockuser.module').then(m => m.BlockuserModule),
      },
      {
        path: 'album',
        data: { pageTitle: 'dhApp.album.home.title' },
        loadChildren: () => import('./album/album.module').then(m => m.AlbumModule),
      },
      {
        path: 'calbum',
        data: { pageTitle: 'dhApp.calbum.home.title' },
        loadChildren: () => import('./calbum/calbum.module').then(m => m.CalbumModule),
      },
      {
        path: 'photo',
        data: { pageTitle: 'dhApp.photo.home.title' },
        loadChildren: () => import('./photo/photo.module').then(m => m.PhotoModule),
      },
      {
        path: 'interest',
        data: { pageTitle: 'dhApp.interest.home.title' },
        loadChildren: () => import('./interest/interest.module').then(m => m.InterestModule),
      },
      {
        path: 'activity',
        data: { pageTitle: 'dhApp.activity.home.title' },
        loadChildren: () => import('./activity/activity.module').then(m => m.ActivityModule),
      },
      {
        path: 'celeb',
        data: { pageTitle: 'dhApp.celeb.home.title' },
        loadChildren: () => import('./celeb/celeb.module').then(m => m.CelebModule),
      },
      {
        path: 'cinterest',
        data: { pageTitle: 'dhApp.cinterest.home.title' },
        loadChildren: () => import('./cinterest/cinterest.module').then(m => m.CinterestModule),
      },
      {
        path: 'cactivity',
        data: { pageTitle: 'dhApp.cactivity.home.title' },
        loadChildren: () => import('./cactivity/cactivity.module').then(m => m.CactivityModule),
      },
      {
        path: 'cceleb',
        data: { pageTitle: 'dhApp.cceleb.home.title' },
        loadChildren: () => import('./cceleb/cceleb.module').then(m => m.CcelebModule),
      },
      {
        path: 'urllink',
        data: { pageTitle: 'dhApp.urllink.home.title' },
        loadChildren: () => import('./urllink/urllink.module').then(m => m.UrllinkModule),
      },
      {
        path: 'frontpageconfig',
        data: { pageTitle: 'dhApp.frontpageconfig.home.title' },
        loadChildren: () => import('./frontpageconfig/frontpageconfig.module').then(m => m.FrontpageconfigModule),
      },
      {
        path: 'vtopic',
        data: { pageTitle: 'dhApp.vtopic.home.title' },
        loadChildren: () => import('./vtopic/vtopic.module').then(m => m.VtopicModule),
      },
      {
        path: 'vquestion',
        data: { pageTitle: 'dhApp.vquestion.home.title' },
        loadChildren: () => import('./vquestion/vquestion.module').then(m => m.VquestionModule),
      },
      {
        path: 'vanswer',
        data: { pageTitle: 'dhApp.vanswer.home.title' },
        loadChildren: () => import('./vanswer/vanswer.module').then(m => m.VanswerModule),
      },
      {
        path: 'vthumb',
        data: { pageTitle: 'dhApp.vthumb.home.title' },
        loadChildren: () => import('./vthumb/vthumb.module').then(m => m.VthumbModule),
      },
      {
        path: 'newsletter',
        data: { pageTitle: 'dhApp.newsletter.home.title' },
        loadChildren: () => import('./newsletter/newsletter.module').then(m => m.NewsletterModule),
      },
      {
        path: 'feedback',
        data: { pageTitle: 'dhApp.feedback.home.title' },
        loadChildren: () => import('./feedback/feedback.module').then(m => m.FeedbackModule),
      },
      {
        path: 'config-variables',
        data: { pageTitle: 'dhApp.configVariables.home.title' },
        loadChildren: () => import('./config-variables/config-variables.module').then(m => m.ConfigVariablesModule),
      },
      {
        path: 'proposal',
        data: { pageTitle: 'dhApp.proposal.home.title' },
        loadChildren: () => import('./proposal/proposal.module').then(m => m.ProposalModule),
      },
      {
        path: 'proposal-vote',
        data: { pageTitle: 'dhApp.proposalVote.home.title' },
        loadChildren: () => import('./proposal-vote/proposal-vote.module').then(m => m.ProposalVoteModule),
      },
      {
        path: 'appuser',
        data: { pageTitle: 'dhApp.appuser.home.title' },
        loadChildren: () => import('./appuser/appuser.module').then(m => m.AppuserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
