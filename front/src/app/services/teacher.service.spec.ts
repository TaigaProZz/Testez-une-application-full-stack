import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {Teacher} from "../interfaces/teacher.interface";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe('TeacherService', () => {
  let service: TeacherService;
  let http: HttpTestingController;
  const apiUrl = "api/teacher";

  const teacherMock: Teacher = {
    id: 1,
    lastName: "a",
    firstName: "a",
    createdAt: new Date(),
    updatedAt: new Date()
  }

  const teachListMock: Teacher[] = [
    {
      id: 1,
      lastName: "a",
      firstName: "a",
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      lastName: "b",
      firstName: "b",
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule, HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    http = TestBed.inject(HttpTestingController)
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return teachers', () => {
    service.all().subscribe((response) => {
      expect(response).toEqual([teachListMock])
    })
    const req = http.expectOne({
      method: 'GET',
      url: `${apiUrl}`,
    });
    req.flush([teachListMock]);
  })

  it('should return teacher detail', () => {
    const id = "1";
    service.detail(id).subscribe((response) => {
      expect(response).toEqual(teacherMock)
    })
    const req = http.expectOne({
      method: 'GET',
      url: `${apiUrl}/${id}`,
    });
    req.flush(teacherMock);
  })

});
