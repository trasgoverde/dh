import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICmessage } from '../cmessage.model';

@Component({
  selector: 'jhi-cmessage-detail',
  templateUrl: './cmessage-detail.component.html',
})
export class CmessageDetailComponent implements OnInit {
  cmessage: ICmessage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cmessage }) => {
      this.cmessage = cmessage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
