export enum RegistryEventType {
    REGISTERED = "REGISTERED",
    UNREGISTERED = "UNREGISTERED",
  }
  
  export type BackendChangeEvent = {
    eventType: RegistryEventType;
    backendInfo: Backend;
  };
  
  export type Backend = {
    id: string;
    displayName: string;
    coordinates: Coordinates;
    zoom: number;
  };
  
  export  type Coordinates = {
    lat: number;
    lng: number;
  };