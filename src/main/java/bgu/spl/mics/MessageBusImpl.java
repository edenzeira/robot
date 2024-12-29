package bgu.spl.mics;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only one public method (in addition to getters which can be public solely for unit testing) may be added to this class
 * All other methods and members you add the class must be private.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<MicroService, BlockingQueue<Message>> microServiceMap;
    private ConcurrentHashMap<Class<? extends Event>, BlockingQueue<MicroService>> eventsMap;
    private ConcurrentHashMap<Class<? extends Broadcast>, BlockingQueue<MicroService>> broadcastMap;
    private ConcurrentHashMap<Event, Future> futureMap;
    private static class MessageBusImplHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    private MessageBusImpl() {
        microServiceMap = new ConcurrentHashMap<>();
        eventsMap = new ConcurrentHashMap<>();
        broadcastMap = new ConcurrentHashMap<>();
        futureMap = new ConcurrentHashMap<>();
    }

    public static MessageBusImpl getInstance() { return MessageBusImplHolder.instance; }

    //לשנות מפאבליק!!!!!!!!!!!!!!!!
    public ConcurrentHashMap<MicroService, BlockingQueue<Message>> getMicroServiceMap() {
        return microServiceMap;
    }

    public ConcurrentHashMap<Class<? extends Event>, BlockingQueue<MicroService>> getEventsMap() {
        return eventsMap;
    }

    public ConcurrentHashMap<Class<? extends Broadcast>, BlockingQueue<MicroService>> getBroadcastMap() {
        return broadcastMap;
    }

    public ConcurrentHashMap<Event, Future> getFutureMap() {
        return futureMap;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        eventsMap.computeIfAbsent(type, k -> new LinkedBlockingQueue<>()).add(m);
    }


    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        broadcastMap.computeIfAbsent(type, k -> new LinkedBlockingQueue<>()).add(m);
    }


    @Override
    public <T> void complete(Event<T> e, T result) {
        futureMap.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        BlockingQueue<MicroService> subscribers = broadcastMap.get(b.getClass());
        if (subscribers != null) {
            synchronized (subscribers) {//in the general case of a framework having two or more services sending the same broadcast type
                for (MicroService m : subscribers)
                    microServiceMap.get(m).add(b);
            }
        }
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        BlockingQueue<MicroService> subscribers = eventsMap.get(e.getClass());
        if (subscribers == null || subscribers.isEmpty()) {
            return null;
        } else {
            synchronized (subscribers) {
                Future<T> future = new Future<>();
                futureMap.put(e, future);
                MicroService m = subscribers.poll();
                microServiceMap.get(m).add(e);
                subscribers.add(m);
                return future;
            }
        }
    }


    @Override
    public void register(MicroService m) {
        microServiceMap.putIfAbsent(m, new LinkedBlockingQueue<>());
    }


    @Override
    public void unregister(MicroService m) {
        synchronized (eventsMap) {// to check!
            for (Queue<MicroService> subscribers : eventsMap.values()) {
                subscribers.remove(m);
            }
        }
        synchronized (broadcastMap) { // to check!
            for (BlockingQueue<MicroService> subscribers : broadcastMap.values()) {
                subscribers.remove(m);
            }
        }
        synchronized (microServiceMap) { // to check!
            BlockingQueue<Message> messages = microServiceMap.get(m);
            for (Message msg: messages) {
                if(msg instanceof Event) {
                    complete((Event)msg, null);
                }
            }
        }
        microServiceMap.remove(m);
    }


    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        BlockingQueue<Message> messages = microServiceMap.get(m);
        if (messages == null)
            throw new IllegalStateException("The microservice" + m.getName() + "is not registered");

        return messages.take(); //if the queue is empty, waits until not empty
        //we need to check if try and catch (the function throws exception)
    }
}


