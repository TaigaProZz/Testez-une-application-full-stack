import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { By } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSession: Session = {
    id: 1,
    name: 'a',
    description: 'd',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockTeacher: Teacher = {
    id: 1,
    firstName: 'a',
    lastName: 'a',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({})),
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher)),
  };

  const mockMatSnackBar = { open: jest.fn() };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule,
      ],
      declarations: [DetailComponent],
      providers: [
        FormBuilder,
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
        {
          provide: SessionService,
          useValue: { sessionInformation: { id: 1, admin: true } },
        },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    }).compileComponents();

  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should fetch session and teacher details', fakeAsync(() => {
      component.ngOnInit();
      tick();

      expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
      expect(component.session).toEqual(mockSession);
      expect(mockTeacherService.detail).toHaveBeenCalledWith('1');
      expect(component.teacher).toEqual(mockTeacher);
    }));
  });

  describe('Participation', () => {
    it('should handle Participate', fakeAsync(() => {
      component.participate();
      tick();

      expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
      expect(mockSessionApiService.detail).toHaveBeenCalled();
    }));

    it('should handle unParticipate', fakeAsync(() => {
      component.unParticipate();
      tick();

      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
      expect(mockSessionApiService.detail).toHaveBeenCalled();
    }));
  });

  describe('UI tests', () => {
    it('should display title user firstname', () => {
      fixture.detectChanges();
      const titleElement = fixture.debugElement.query(By.css('h1')).nativeElement;
      expect(titleElement.textContent).toContain('A');
    });

    it('should show delete button for admins', () => {
      component.isAdmin = true;
      fixture.detectChanges();

      const deleteButton = fixture.debugElement.query(By.css('button[color="warn"]'));
      expect(deleteButton).toBeTruthy();
    });

    it('should show participate button for normal users if not participating', () => {
      component.isAdmin = false;
      component.isParticipate = false;
      fixture.detectChanges();

      const participateButton = fixture.debugElement.query(By.css('button[color="primary"]'));
      expect(participateButton).toBeTruthy();
    });
  });
});
