import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INewsletter } from '../newsletter.model';

@Component({
  selector: 'jhi-newsletter-detail',
  templateUrl: './newsletter-detail.component.html',
})
export class NewsletterDetailComponent implements OnInit {
  newsletter: INewsletter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ newsletter }) => {
      this.newsletter = newsletter;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
