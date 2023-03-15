FROM node:18.14.2-alpine3.16 AS build
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
ENV NODE_ENV production
RUN npm ci --omit=dev
COPY . .
RUN npm run build

FROM --platform=linux/amd64 nginx:1.23.3-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/ng-poi-map /usr/share/nginx/html
RUN chgrp -R 0 /var/cache/nginx && chmod -R g+rwX /var/cache/nginx/
RUN chgrp -R 0 /var/run && chmod -R g+rwX /var/run
RUN chgrp -R 0 /usr/share/nginx/html && chmod -R g+rwX /usr/share/nginx/html

ENV APP_TITLE="POI Map Visualizer (Backstage Workshop RH Summit 23 Edition)"
ENV MAP_ATTRIBUTION="Map data &copy; <a href='http://openstreetmap.org'>OpenStreetMap</a> contributors, <a href='http://creativecommons.org/licenses/by-sa/2.0/'>CC-BY-SA</a>, Imagery &copy; <a href='http://mapbox.com'>Mapbox</a>"
ENV MAP_URL=https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}
ENV MAP_TOKEN=
ENV GATEWAY_API_URL=
ENV WEBSOCKET_ENDPOINT=

EXPOSE 8080
CMD ["/bin/sh",  "-c",  "envsubst < /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js && exec nginx -g 'daemon off;'"]
