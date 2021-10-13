import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppprofile } from '../appprofile.model';

@Component({
  selector: 'jhi-appprofile-detail',
  templateUrl: './appprofile-detail.component.html',
})
export class AppprofileDetailComponent implements OnInit {
  appprofile: IAppprofile | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appprofile }) => {
      this.appprofile = appprofile;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
