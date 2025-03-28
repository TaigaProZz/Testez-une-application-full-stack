import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Session} from "../interfaces/session.interface";

describe('SessionsService', () => {
  let service: SessionApiService;
  let http: HttpTestingController;
  const apiUrl = "api/session";

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

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule, HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    http = TestBed.inject(HttpTestingController)
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return sessions', () => {
    service.all().subscribe((response) => {
      expect(response).toEqual([mockSession])
    })
    const req = http.expectOne({
      method: 'GET',
      url: `${apiUrl}`,
    });
    req.flush([mockSession]);
  })

  it('should return session detail', () => {
    const id = "1";
    service.detail(id).subscribe((response) => {
      expect(response).toEqual(mockSession)
    })
    const req = http.expectOne({
      method: 'GET',
      url: `${apiUrl}/${id}`,
    });
    req.flush(mockSession);
  })

  it('should delete session', () => {
    const id = "1";
    service.delete(id).subscribe((response) => {
      expect(response).toEqual(null)
    })
    const req = http.expectOne({
      method: 'DELETE',
      url: `${apiUrl}/${id}`,
    });
    req.flush(null);
  })

  it('should create session', () => {
    service.create(mockSession).subscribe((response) => {
      expect(response).toEqual(mockSession)
    })
    const req = http.expectOne({
      method: 'POST',
      url: `${apiUrl}`,
    });
    req.flush(mockSession);
  })

  it('should update session', () => {
    const id = "1";
    service.update(id, mockSession).subscribe((response) => {
      expect(response).toEqual(mockSession)
    })
    const req = http.expectOne({
      method: 'PUT',
      url: `${apiUrl}/${id}`,
    });
    req.flush(mockSession);
  })

  it('should participate in session', () => {
    const id = "1";
    const userId = "1";
    service.participate(id, userId).subscribe((response) => {
      expect(response).toEqual(null)
    })
    const req = http.expectOne({
      method: 'POST',
      url: `${apiUrl}/${id}/participate/${userId}`,
    });
    req.flush(null);
  })

  it('should unparticipate in session', () => {
    const id = "1";
    const userId = "1";
    service.unParticipate(id, userId).subscribe((response) => {
      expect(response).toEqual(null)
    })
    const req = http.expectOne({
      method: 'DELETE',
      url: `${apiUrl}/${id}/participate/${userId}`,
    });
    req.flush(null);
  })

});
