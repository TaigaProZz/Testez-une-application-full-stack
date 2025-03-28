import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {of, throwError} from "rxjs";
import {AuthService} from "../../services/auth.service";
import {NgZone} from "@angular/core";
import {Router} from "@angular/router";
import {RegisterRequest} from "../../interfaces/registerRequest.interface";
import {RouterTestingModule} from "@angular/router/testing";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let ngZone: NgZone;
  const registerRequest: RegisterRequest = { firstName: "a", lastName: "a", email: 'a@g.com', password: 'a' };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [AuthService],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form with firstname, lastname, email and password fields', () => {
    expect(component.form.contains('firstName')).toBeTruthy();
    expect(component.form.contains('lastName')).toBeTruthy();
    expect(component.form.contains('email')).toBeTruthy();
    expect(component.form.contains('password')).toBeTruthy();
  })

  it('should make the firstName field required', () => {
    const firstNameControl = component.form.get('firstName');
    firstNameControl?.setValue('');
    expect(firstNameControl?.valid).toBeFalsy();
  });

  it('should make the lastName field required', () => {
    const lastNameControl = component.form.get('lastName');
    lastNameControl?.setValue('');
    expect(lastNameControl?.valid).toBeFalsy();
  });

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

    jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
    jest.spyOn(router, 'navigate');

    component.form.setValue(registerRequest);

    ngZone.run(() => {
      component.submit();
    });

    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should set onError to true on register error', () => {
    jest.spyOn(authService, 'register').mockReturnValue(throwError('error'));

    component.form.setValue(registerRequest);

    ngZone.run(() => {
      component.submit();
    });

    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(component.onError).toBe(true);
  });
});
