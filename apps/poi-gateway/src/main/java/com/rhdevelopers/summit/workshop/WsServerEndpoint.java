package com.rhdevelopers.summit.workshop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhdevelopers.summit.workshop.BackendRegistry.RegistryEventType;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
@ServerEndpoint(
	value = "/ws-server-endpoint",
	encoders = {WsServerEndpoint.BackendChangeEventEncoder.class}
)
public class WsServerEndpoint {

	private Map<String,Session> sessions = new ConcurrentHashMap<>();

	private BackendRegistry backendRegistry;

	public static record BackendChangeEvent(RegistryEventType eventType, BackendRegistry.Backend backendInfo) {};
	
	public static class BackendChangeEventEncoder implements Encoder.Text<BackendChangeEvent> {

		private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

		@Override
		public void init(EndpointConfig config) { }

		@Override
		public void destroy() {}

		@Override
		public String encode(BackendChangeEvent object) throws EncodeException {
			try {
				return OBJECT_MAPPER.writeValueAsString(object);
			} catch (JsonProcessingException e) {
				Log.errorv("failed to encode BackendChangeEvent: {0}",e.getMessage());
			}
			return null;
		}

	}

	public WsServerEndpoint(BackendRegistry backendRegistry) {
		this.backendRegistry = backendRegistry;
	}

	@OnOpen
	public void onOpen(Session session) {
		Log.debugv("client connected with id: {0}",session.getId());
		sessions.put(session.getId(),session);
		Log.debugv("number of currently connected clients: {0}",sessions.size());
		sendBackendInfos(session);
	}
 
	@OnError
	public void onError(Session session, Throwable error) {
		Log.errorv("ws communication error for session id {0}:{1}",session.getId(),error.getMessage());
	}

	@OnClose
	public void onClose(Session session) {
		Log.debugv("client disconnected with id: {0}",session.getId());
		sessions.remove(session.getId());
		Log.debugv("number of currently connected clients: {0}",sessions.size());
	}

	@OnMessage
	public void onMessage(String message) {
		Log.debugv("received: {0} -> noop for server endpoint",message);
	}

	public void broadcastBackendChangeNotification(RegistryEventType eventType, BackendRegistry.Backend backendInfo) {
		Log.debugv("notify {0} clients about backend changes",sessions.size());
        sessions.values().forEach(s -> {
			var message = new BackendChangeEvent(eventType, backendInfo);
			Log.debugv("sending {0} to connected client id: {1}",message,s.getId());
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    Log.errorv("ws communication error: {0}",result.getException());
                }
            });
        });
	}

	public void sendBackendInfos(Session session) {
		var backends = backendRegistry.getAllRegisteredBackends();
		if(backends.isEmpty()) {
			Log.debug("currently no available backends... stay tuned");
			return;
		}
		Log.debugv("notify client id {0} about {1} registered backend(s)",session.getId(),backends.size());
		backends.entrySet().forEach(e -> {
			var message = new BackendChangeEvent(RegistryEventType.REGISTERED,e.getValue().backendInfo());
			Log.debugv("sending backend info: {0}",message);
			session.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
					Log.errorv("ws communication error: {0}",result.getException());
                }
            });
		});
	}
    
}
