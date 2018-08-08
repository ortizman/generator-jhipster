import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class SmartTableService {
  
  constructor(private http: HttpClient) { }
  
  getData() {
    return this.http.get(environment.baseEndpoint + '/api/users');
  }
}