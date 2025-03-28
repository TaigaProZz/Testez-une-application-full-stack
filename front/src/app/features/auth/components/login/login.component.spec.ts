import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {of, throwError} from "rxjs";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {NgZone} from "@angular/core";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;
  let ngZone: NgZone;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [AuthService, SessionService],
      imports: [
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        BrowserAnimationsModule,
        RouterTestingModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form with email and password fields', () => {
    expect(component.form.contains('email')).toBeTruthy();
    expect(component.form.contains('password')).toBeTruthy();
  })

  it('should make the email field required', () => {
    const emailControl = component.form.get('email');
    emailControl?.setValue('');
    expect(emailControl?.valid).toBeFalsy();
  });

  it('should make the password field required', () => {
    const passwordControl = component.form.get('password');
    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalsy();
  });

  it('should submit form and navigate on success', () => {
    const loginRequest = { email: 'test@example.com', password: 'password' };
    const sessionInformation: SessionInformation =
      { id: 1,
        type: 'type',
        token: 'token',
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      };

    jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformation));
    jest.spyOn(sessionService, 'logIn');
    jest.spyOn(router, 'navigate');

    component.form.setValue(loginRequest);

    ngZone.run(() => {
      component.submit();
    });

    expect(authService.login).toHaveBeenCalledWith(loginRequest);
    expect(sessionService.logIn).toHaveBeenCalledWith(sessionInformation);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true on login error', () => {
    const loginRequest = { email: 'test@example.com', password: 'password' };

    jest.spyOn(authService, 'login').mockReturnValue(throwError('error'));

    component.form.setValue(loginRequest);

    ngZone.run(() => {
      component.submit();
    });

    expect(authService.login).toHaveBeenCalledWith(loginRequest);
    expect(component.onError).toBe(true);
  });

});
