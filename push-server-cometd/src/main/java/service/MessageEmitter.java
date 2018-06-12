package service;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageEmitter implements Runnable
{
    private final Logger logger = Logger.getLogger("MessageEmitter");
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    public MessageEmitter()
    {
    }

    public List<Listener> getListeners()
    {
        return listeners;
    }

    public void start()
    {
        run();
    }

    public void stop()
    {
        scheduler.shutdownNow();
    }

    public void run()
    {
        Random random = new Random();

        List<Object> updates = new ArrayList<Object>();
        updates.add(new Object());

        // Notify the listeners
        for (Listener listener : listeners)
        {
            logger.severe("run loop: notify the listener");
            listener.onUpdates(updates);
        }

        // Randomly choose how long for the next update
        // We use a max delay of 1 second to simulate a high rate of updates
        long howLong = random.nextInt(1000);
        scheduler.schedule(this, howLong, TimeUnit.MILLISECONDS);
    }

    public interface Listener extends EventListener
    {
        void onUpdates(List<Object> updates);
    }
}
