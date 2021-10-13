import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICalbum } from '../calbum.model';

@Component({
  selector: 'jhi-calbum-detail',
  templateUrl: './calbum-detail.component.html',
})
export class CalbumDetailComponent implements OnInit {
  calbum: ICalbum | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calbum }) => {
      this.calbum = calbum;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
