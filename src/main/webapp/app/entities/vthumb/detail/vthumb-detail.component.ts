import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVthumb } from '../vthumb.model';

@Component({
  selector: 'jhi-vthumb-detail',
  templateUrl: './vthumb-detail.component.html',
})
export class VthumbDetailComponent implements OnInit {
  vthumb: IVthumb | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vthumb }) => {
      this.vthumb = vthumb;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
