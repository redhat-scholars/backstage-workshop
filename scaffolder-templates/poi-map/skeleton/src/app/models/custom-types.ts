export enum RegistryEventType {
  REGISTERED = 'REGISTERED',
  UNREGISTERED = 'UNREGISTERED',
}

export interface Coordinates {
  lat: number;
  lng: number;
}

export interface Backend {
  id: string;
  displayName: string;
  coordinates: Coordinates;
  zoom: number;
}

export interface BackendChangeEvent {
  eventType: RegistryEventType;
  backendInfo: Backend;
}

export interface PoiRecord {
    name: string;
    description: string;
    coordinates: number[];
}
