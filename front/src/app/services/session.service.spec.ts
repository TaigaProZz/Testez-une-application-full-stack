import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";
import {Observable} from "rxjs";

describe('SessionService', () => {
  let service: SessionService;
  let sessionMock: SessionInformation = {
    token: "token",
    type: "type",
    id: 1,
    username: "username",
    firstName: "firstName",
    lastName: "lastName",
    admin: false
  }

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('$isLogged() should return observable', () => {
    expect(service.$isLogged()).toBeInstanceOf(Observable);
  });

  it('logIn() should set isLogged to true', () => {
    service.logIn(sessionMock);
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
    });
  });

  it('logOut() should set isLogged to false', () => {
    service.logIn(sessionMock);
    service.logOut();
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
    });
  });

  it('logIn() should set sessionInformation', () => {
    service.logIn(sessionMock);
    expect(service.sessionInformation).toBe(sessionMock);
  });

  it('logOut() should set sessionInformation to undefined', () => {
    service.logIn(sessionMock);
    service.logOut();
    expect(service.sessionInformation).toBe(undefined);
  });
});
