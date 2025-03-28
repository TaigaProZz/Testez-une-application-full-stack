import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { AuthService } from './auth.service';

import {Observable} from "rxjs";
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HttpClientModule} from "@angular/common/http";

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController;
  const apiUrl = "api/auth";
  const registerMock: RegisterRequest = {
    email: "a@gmail.com",
    password: "password",
    firstName: "firstName",
    lastName: "lastName",
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule, HttpClientTestingModule
      ],
    });
    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController)

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should register user', () => {
    service.register(registerMock).subscribe((response) => {
      expect(response).toBeTruthy()
    })

    const req = http.expectOne({
      method: 'POST',
      url: `${apiUrl}/register`,
    });
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerMock);

    req.flush(null);
  })

  it('should login user', () => {
    service.login(registerMock).subscribe((response) => {
      expect(response).toBeTruthy()
    })

    const req = http.expectOne({
      method: 'POST',
      url: `${apiUrl}/login`,
    });
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerMock);

    req.flush(null);
  })

});
