FROM registry.access.redhat.com/ubi8/nodejs-18 AS build
COPY --chown=1001:0 package.json package-lock.json ./
RUN npm ci
COPY --chown=1001:0 . .
ENV NODE_ENV production
RUN npm run build

FROM --platform=linux/amd64 registry.access.redhat.com/ubi8/nginx-120
# COPY --chown=1001:1001 nginx.conf /etc/nginx/nginx.conf
COPY --chown=1001:0 nginx.conf "${NGINX_CONF_PATH}"
# COPY --from=build /usr/src/app/dist/ng-poi-map /usr/share/nginx/html
COPY --chown=1001:0 --from=build /opt/app-root/src/dist/ng-poi-map .

# USER root
# RUN chown -R nginx /usr/share/nginx/html

ENV MAP_TITLE="POI Map (Janus IDP & Openshift Workshop Red Hat Summit 2023)"
ENV MAP_ATTRIBUTION="Map data &copy; <a href='http://openstreetmap.org'>OpenStreetMap</a> contributors, <a href='http://creativecommons.org/licenses/by-sa/2.0/'>CC-BY-SA</a>, Imagery &copy; <a href='http://mapbox.com'>Mapbox</a>"
ENV MAP_URL=https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}
ENV MAP_TOKEN=
ENV GATEWAY_API_URL=
ENV WEBSOCKET_ENDPOINT=

# USER nginx
EXPOSE 8080
# CMD ["/bin/sh",  "-c",  "envsubst < /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js && exec nginx -g 'daemon off;'"]
CMD ["/bin/sh",  "-c",  "envsubst < assets/env.template.js > assets/env.js && exec nginx -g 'daemon off;'"]
