import { icon, Layer, marker, Marker, markerClusterGroup, tileLayer, TileLayer } from "leaflet";
import { PoiRecord } from "../models/custom-types";

export const createTileLayer = (
    tileId: string,
    tileUrl: string,
    attribution: string,
    token: string
): TileLayer => {
    return tileLayer(tileUrl, {
      id: tileId,
      attribution: attribution,
      tileSize: 512,
      maxZoom: 18,
      zoomOffset: -1,
      accessToken: token
    });
}

export const createDefaultMarker = (
  info: string,
  lat: number,
  lng: number
): Marker => {
  return marker([lat, lng], {
    icon: icon({
      iconSize: [25, 41],
      iconAnchor: [13, 41],
      iconUrl: 'leaflet/marker-icon.png',
      iconRetinaUrl: 'leaflet/marker-icon-2x.png',
      shadowUrl: 'leaflet/marker-shadow.png'
    })
  }
  ).bindPopup(info);
}

export const createNewBackendLayer = (
  dataPoints:PoiRecord[]
  ): Layer =>  {
  const newLayer = markerClusterGroup();
  dataPoints.map(poi => {
    var info = '<p><strong>'+poi.name+'</strong>';
    info += (poi.description ? '<br/><i>'+poi.description+'</i>' : '');
    info += '</p><p>lat: '+poi.coordinates[0]+'<br/>lon: '+poi.coordinates[1]+'</p>';
    createDefaultMarker(info,poi.coordinates[0],poi.coordinates[1])
      .addTo(newLayer)
    }
  );
  return newLayer;
}
