import { TestBed } from '@angular/core/testing';

import { BackendRegistryService } from './backend-registry.service';

describe('BackendRegistryService', () => {
  let service: BackendRegistryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BackendRegistryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
