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
	private static MessageBusImpl messageBus = new MessageBusImpl();
    private ConcurrentHashMap<MicroService, pair<BlockingQueue<Event>>, <BlockingQueue<Broadcast>>> microServiceMap;
    private Map<Class<? extends Event>, Queue<MicroService>> eventsMap;
    private Map<Class<? extends Broadcast>, List<MicroService>> broadcastMap;
	private Map<Event, Future> futureMap;




    private MessageBusImpl(){
        microServiceMap = new ConcurrentHashMap<>();
        eventsMap = new ConcurrentHashMap<>();
        broadcastMap = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
    }

	public static MessageBusImpl getInstance() {
        return messageBus;
    }


    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        eventsMap.computeIfAbsent(type, k -> new ConcurrentLinkedQueue<>()).add(m); //ConcurrentLinkedQueue is thread safe
    }


    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        broadcastMap.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(m); //CopyOnWriteArrayList is thread safe
    }


    @Override
    public <T> void complete(Event<T> e, T result) {
        // TODO Auto-generated method stub


    }


    @Override
    public void sendBroadcast(Broadcast b) {
		List<MicroService> subscribers = broadcastMap.get(b.getClass());
		if (subscribers != null){
			synchronized(subscribers){
        		for (MicroService m : subscribers)
					microServiceMap.get(m).add(b);
			}	
		}
    }


   
    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Queue<MicroService> subscribers = eventsMap.get(e.getClass());
		if (subscribers == null || subscribers.isEmpty()){
			return null;
		}
		else{
			synchronized(subscribers){
				MicroService m = subscribers.poll();
				microServiceMap.get(m).add(e);
				subscribers.add(m);
				//how to return future????
				return null; //delete this :) you are the most beatiful queen in the world!!!
			}
		}
    }


    @Override
    public void register(MicroService m) {
        microServiceMap.putIfAbsent(m, new LinkedBlockingQueue<>());
    }


    @Override
    public void unregister(MicroService m) {
        synchronized(eventsMap){// to check!
        for (Queue<MicroService> subscribers : eventsMap.values()) {
            subscribers.remove(m);
        }
    }
        synchronized(broadcastMap){ // to check!
        for (List<MicroService> subscribers : broadcastMap.values()) {
            subscribers.remove(m);
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


