import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import {User} from "../interfaces/user.interface";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {error} from "@angular/compiler-cli/src/transformers/util";

describe('UserService', () => {
  let service: UserService;
  let http: HttpTestingController;
  const apiUrl = "api/user/";
  const userMock: User = {
    id: 1,
    email: 'john.doe@example.com',
    lastName: "a",
    firstName: "a",
    admin: false,
    password: "a",
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule, HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    http = TestBed.inject(HttpTestingController)
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  describe('GetUserById', () => {
    it('should return user', () => {
      const id = "1";
      service.getById(id).subscribe((response) => {
        expect(response).toEqual(userMock)
      })
      const req = http.expectOne({
        method: 'GET',
        url: apiUrl + id,
      });
      req.flush(userMock);
    })
  })

  describe('DeleteUser', () => {
    it('should delete user', () => {
      const id = "1";
      service.delete(id).subscribe((response) => {
        expect(response).toBeTruthy()
      })

      const req = http.expectOne({
        method: 'DELETE',
        url: apiUrl + id,
      });

      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    })
  })
});
