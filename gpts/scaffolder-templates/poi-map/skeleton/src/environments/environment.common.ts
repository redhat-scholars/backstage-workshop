export const environment = {
    production: false,
    mapTitle: window['env']['mapTitle'] ||
          'POI Map Visualizer (Backstage Workshop RH Summit 23 Edition)',
    mapAttribution: window['env']['mapAttribution'] || 
          'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
          '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
          'Imagery &copy; <a href="http://mapbox.com">Mapbox</a>',
    mapUrl: window['env']['mapUrl'] || 
          'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}',
    mapToken: window['env']['mapToken'] || 
          'pk.eyJ1IjoiZ3JhaGFtZHVtcGxldG9uIiwiYSI6ImNpemR6cnFhbTF0YWszMnA5eTJ0dXY1ZW8ifQ.-91_VlsyyskWAWF54GYFMg',
    gatewayApiUrl: window['env']['gatewayApiUrl'] || 
          'http://localhost:8082',
    websocketEndpoint: window['env']['websocketEndpoint'] || 
          'ws://localhost:8082/ws-server-endpoint'
  };
  