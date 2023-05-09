package com.rhdevelopers.summit.workshop;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class BackendRegistry {

    public enum RegistryEventType {
        REGISTERED,
        UNREGISTERED
    }

    public static record Coordinates(double lat, double lng) {};

    public static record Backend(
        String id,
        String displayName,
        Coordinates coordinates,
        int zoom
    ) {};

    public static record BackendIdentifier(String resourceName) {};

    public static record BackendProxy(Backend backendInfo,PoiRemoteService restClient) {};

    private Map<BackendIdentifier,BackendProxy> registry = new ConcurrentHashMap<>();

    WsServerEndpoint websocket;

    public BackendRegistry(WsServerEndpoint websocket) {
        this.websocket = websocket;
        var withFakeBackend = ConfigProvider.getConfig().getValue("fake.backend.registration", Boolean.class);
        if(withFakeBackend) {
            var fakeBackendEndpoint = ConfigProvider.getConfig().getValue("fake.backend.endpoint", String.class);
            var identifier = new BackendIdentifier(SummitBackendResource.SUMMIT_BACKEND_INFO.id);
            Log.debugv("registering fake backend {0} with endpoint {1}",identifier,fakeBackendEndpoint);
            this.registry.put(identifier,new BackendProxy(SummitBackendResource.SUMMIT_BACKEND_INFO,PoiRemoteService.createRestClient(fakeBackendEndpoint)));
        }
    }

    @PostConstruct
    public void autoRegisterParksBackend() {
        var withParksBackend = ConfigProvider.getConfig().getValue("parks.backend.registration", Boolean.class);
        if(withParksBackend) {
            var identifier = new BackendIdentifier(ConfigProvider.getConfig().getValue("parks.backend.id", String.class));
            var parksBackendEndpoint = ConfigProvider.getConfig().getValue("parks.backend.endpoint", String.class);
            var restClient = PoiRemoteService.createRestClient(parksBackendEndpoint);
            Uni<Response> backendPolling = Uni.createFrom().item(() -> {
                                        Log.debugv("trying to auto-register parks backend {0} @ endpoint {1}",identifier,parksBackendEndpoint);
                                        return restClient.getInfo();
                                    })
                                    .onFailure().retry()
                                    .withBackOff(Duration.ofSeconds(2),Duration.ofSeconds(4))
                                    .indefinitely();
            backendPolling.subscribe().with(
                    response -> {
                        var backendInfo = response.readEntity(Backend.class);
                        Log.debugv("retrieved backend info: {0}",backendInfo);
                        registry.put(identifier, new BackendProxy(backendInfo,restClient));
                        websocket.broadcastBackendChangeNotification(RegistryEventType.REGISTERED, backendInfo);
                    },
                    failure -> Log.errorv("unexpected error during auto-registration of backend with identifier {0} -> {1}",identifier,failure.getMessage())
            );
        }
    }

    public void register(String resourceName, String resourceEndpoint) {
        var identifier = new BackendIdentifier(resourceName);
        if(!registry.containsKey(identifier)) {
            Log.debugv("registering backend with identifier {0}",identifier);
            var restClient = PoiRemoteService.createRestClient(resourceEndpoint);
            var backendInfo = restClient.getInfo().readEntity(Backend.class);
            Log.debugv("retrieved backend info: {0}",backendInfo);
            registry.put(identifier, new BackendProxy(backendInfo,restClient));
            websocket.broadcastBackendChangeNotification(RegistryEventType.REGISTERED, backendInfo);
        } else {
            Log.debugv("backend for {0} already exists -> ignoring registration!",identifier);
        }
    }

    public void unregister(String resourceName, String resourceEndpoint) {
        var identifier = new BackendIdentifier(resourceName);
        if(registry.containsKey(identifier)) {
            Log.debugv("unregistering backend for identifier {0}",identifier);
            var backendProxy = registry.remove(identifier);
            websocket.broadcastBackendChangeNotification(RegistryEventType.UNREGISTERED, backendProxy.backendInfo);
        } else {
            Log.debugv("backend for {0} does not exist yet/anymore",identifier);
        }
    }

    public Map<BackendIdentifier,BackendProxy> getAllRegisteredBackends() {
        return Collections.unmodifiableMap(registry);
    }

    public BackendProxy getBackendForId(String backendId) {
        return registry.get(new BackendIdentifier(backendId));
    }
    
}
