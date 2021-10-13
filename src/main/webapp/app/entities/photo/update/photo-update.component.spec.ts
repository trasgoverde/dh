jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PhotoService } from '../service/photo.service';
import { IPhoto, Photo } from '../photo.model';
import { IAlbum } from 'app/entities/album/album.model';
import { AlbumService } from 'app/entities/album/service/album.service';
import { ICalbum } from 'app/entities/calbum/calbum.model';
import { CalbumService } from 'app/entities/calbum/service/calbum.service';

import { PhotoUpdateComponent } from './photo-update.component';

describe('Component Tests', () => {
  describe('Photo Management Update Component', () => {
    let comp: PhotoUpdateComponent;
    let fixture: ComponentFixture<PhotoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let photoService: PhotoService;
    let albumService: AlbumService;
    let calbumService: CalbumService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PhotoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PhotoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      photoService = TestBed.inject(PhotoService);
      albumService = TestBed.inject(AlbumService);
      calbumService = TestBed.inject(CalbumService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Album query and add missing value', () => {
        const photo: IPhoto = { id: 456 };
        const album: IAlbum = { id: 63869 };
        photo.album = album;

        const albumCollection: IAlbum[] = [{ id: 79194 }];
        jest.spyOn(albumService, 'query').mockReturnValue(of(new HttpResponse({ body: albumCollection })));
        const additionalAlbums = [album];
        const expectedCollection: IAlbum[] = [...additionalAlbums, ...albumCollection];
        jest.spyOn(albumService, 'addAlbumToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        expect(albumService.query).toHaveBeenCalled();
        expect(albumService.addAlbumToCollectionIfMissing).toHaveBeenCalledWith(albumCollection, ...additionalAlbums);
        expect(comp.albumsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Calbum query and add missing value', () => {
        const photo: IPhoto = { id: 456 };
        const calbum: ICalbum = { id: 20924 };
        photo.calbum = calbum;

        const calbumCollection: ICalbum[] = [{ id: 1668 }];
        jest.spyOn(calbumService, 'query').mockReturnValue(of(new HttpResponse({ body: calbumCollection })));
        const additionalCalbums = [calbum];
        const expectedCollection: ICalbum[] = [...additionalCalbums, ...calbumCollection];
        jest.spyOn(calbumService, 'addCalbumToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        expect(calbumService.query).toHaveBeenCalled();
        expect(calbumService.addCalbumToCollectionIfMissing).toHaveBeenCalledWith(calbumCollection, ...additionalCalbums);
        expect(comp.calbumsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const photo: IPhoto = { id: 456 };
        const album: IAlbum = { id: 38370 };
        photo.album = album;
        const calbum: ICalbum = { id: 30333 };
        photo.calbum = calbum;

        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(photo));
        expect(comp.albumsSharedCollection).toContain(album);
        expect(comp.calbumsSharedCollection).toContain(calbum);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Photo>>();
        const photo = { id: 123 };
        jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: photo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(photoService.update).toHaveBeenCalledWith(photo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Photo>>();
        const photo = new Photo();
        jest.spyOn(photoService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: photo }));
        saveSubject.complete();

        // THEN
        expect(photoService.create).toHaveBeenCalledWith(photo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Photo>>();
        const photo = { id: 123 };
        jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(photoService.update).toHaveBeenCalledWith(photo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAlbumById', () => {
        it('Should return tracked Album primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAlbumById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCalbumById', () => {
        it('Should return tracked Calbum primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCalbumById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
