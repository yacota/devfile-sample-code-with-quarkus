package org.acme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/callback")
public class CallbackResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackResource.class);

    private final Map<String, Map<String, Object>> dirtyCache = new ConcurrentHashMap<>();

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void consumeCallBack(Map<String, Object> inputData,
                                @HeaderParam("log-user-id") String logUserId,
                                @HeaderParam("log-process-context") String logContextId) {
        LOGGER.info("Callback received with payload {} with userId {} and processId {}",
                inputData, logUserId, logContextId);
        storeInCache(logContextId, inputData);
    }

    @GET
    @Path("/heartbeat")
    @Consumes(MediaType.APPLICATION_JSON)
    public void consumeHeartBeat(@HeaderParam("log-user-id") String logUserId,
                                 @HeaderParam("log-process-context") String logContextId) {
        LOGGER.info("HeartBeat received with with userId {} and processId {}", logUserId, logContextId);
    }

    @GET
    @Path("/logs")
    @Produces(MediaType.TEXT_PLAIN)
    public Response exposeLogs() {
        return Response.ok(dirtyCache).build();
    }

    @DELETE
    @Path("/logs")
    public void deleteLogs() {
        LOGGER.info("Clearing cache");
        dirtyCache.clear();
    }

    private void storeInCache(String logContextId, Map<String, Object> payload) {
        if (logContextId != null && payload != null) {
            dirtyCache.put(logContextId, payload);
        }
    }

}