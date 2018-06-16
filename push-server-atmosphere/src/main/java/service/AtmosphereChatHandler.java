package service;

import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.handler.OnMessage;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Date;

/**
 * AtmosphereHandler providing a simple chat server implementation.
 */
@AtmosphereHandlerService(path="/chat",
                          interceptors= {AtmosphereResourceLifecycleInterceptor.class,
                                         BroadcastOnPostAtmosphereInterceptor.class,
                                         TrackMessageSizeInterceptor.class})
                                         public class AtmosphereChatHandler extends OnMessage<String> implements AtmosphereHandler  {
                                             private static final Logger logger = LoggerFactory.getLogger(AtmosphereChatHandler.class);
                                             @Override
                                             public void onMessage(AtmosphereResponse response, String message) throws IOException {
                                                 // Simple JSON -- Use Jackson for more complex structure
                                                 // Message looks like { "author" : "foo", "message" : "bar" }
                                                 String author = message.substring(message.indexOf(":") + 2, message.indexOf(",") - 1);
                                                 String chat = message.substring(message.lastIndexOf(":") + 2, message.length() - 2);

                                                 response.getWriter().write(new Data(author, chat).toString());
                                             }

                                             @Override
                                             public void onDisconnect(AtmosphereResponse response) throws IOException {
                                                 AtmosphereResourceEvent event = response.resource().getAtmosphereResourceEvent();
                                                 if (event.isCancelled()) {
                                                     logger.info("Browser {} unexpectedly disconnected", response.resource().uuid());
                                                 } else if (event.isClosedByClient()) {
                                                     logger.info("Browser {} closed the connection", response.resource().uuid());
                                                 }
                                             }

                                             private final static class Data {

                                                 private final String text;
                                                 private final String author;

                                                 public Data(String author, String text) {
                                                     this.author = author;
                                                     this.text = text;
                                                 }

                                                 public String toString() {
                                                     return "{ \"text\" : \"" + text + "\", \"author\" : \"" + author + "\" , \"time\" : " + new Date().getTime() + "}";
                                                 }
                                             }
                                         }
