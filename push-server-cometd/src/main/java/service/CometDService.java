package service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.annotation.Listener;
import org.cometd.annotation.Configure;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.MarkedReference;
// import org.cometd.annotation.Subscription;
import org.cometd.server.authorizer.GrantAuthorizer;
import org.cometd.bayeux.server.ConfigurableServerChannel;

@javax.inject.Named
@javax.inject.Singleton
@Service("notificationService")
public class CometDService implements MessageEmitter.Listener {
    private final Logger logger = Logger.getLogger("CometDService");
    @Inject
    private BayeuxServer bayeuxServer;
    @Session
    private ServerSession serverSession;
    @Session
    private LocalSession localSession;

    @PostConstruct
    public void init() {
        System.out.println("Echo Service Initialized");
    }

    @Configure("/echo")
    public void configure(ConfigurableServerChannel channel) {
        channel.setLazy(true);
        channel.addAuthorizer(GrantAuthorizer.GRANT_PUBLISH);
    }

    @Listener("/echo")
    public void listen(ServerSession remote, ServerMessage.Mutable message) {
        String channel = message.getChannel();

        Object data = message.getData();
        remote.deliver(serverSession, channel, data);
    }

    public void onUpdates(List<Object> updates) {
        for (Object obj : updates) {
            // Create the channel name using the stock symbol
            String channelName = "/stock/" + "meigara";

            // Initialize the channel, making it persistent and lazy
            bayeuxServer.createChannelIfAbsent(channelName, new ConfigurableServerChannel.Initializer()
            {
                public void configureChannel(ConfigurableServerChannel channel)
                {
                    logger.severe("configureChannel");
                    channel.setPersistent(true);
                    channel.setLazy(true);
                }
            });

            // Convert the Update business object to a CometD-friendly format
            Map<String, Object> data = new HashMap<String, Object>(4);
            data.put("symbol", "銘柄");
            data.put("oldValue", "120");
            data.put("newValue", "300");

            // Publish to all subscribers
            ServerChannel channel = bayeuxServer.getChannel(channelName);
            logger.severe("publishing...");
            channel.publish(serverSession, data);
            channel.publish(localSession, data);
            logger.severe("published.");
        }
    }
}
