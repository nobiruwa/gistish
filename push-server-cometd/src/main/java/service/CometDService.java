package service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.annotation.Listener;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.BayeuxServer;
// import org.cometd.annotation.Subscription;

@javax.inject.Named
@javax.inject.Singleton
@Service("notificationService")
public class CometDService {
    @Inject
    private BayeuxServer bayeuxServer;
    @Session
    private ServerSession serverSession;

    @PostConstruct
    public void init() {
        System.out.println("Echo Service Initialized");
    }

    @Listener("/echo")
    public void listen(ServerSession remote, ServerMessage.Mutable message) {
        String channel = message.getChannel();

        Object data = message.getData();
        remote.deliver(serverSession, channel, data);
    }
}
