import bgu.spl.mics.Event;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.CameraService;
import bgu.spl.mics.application.services.FusionSlamService;
import bgu.spl.mics.application.services.LiDarService;
import bgu.spl.mics.application.services.TimeService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;


public class messageBusTest{
    //להבין איך עושים getters שהם פבליק לטסטים
    @Test
    void registerTest(){
        Camera camera = new Camera(3, 3, STATUS.UP, "camera3", null);
        CameraService service = new CameraService(camera);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(service);
        assertNotNull(bus.getMicroServiceMap().get(service));
    }

    @Test
    void subscribeEventTest(){
        LiDarWorkerTracker lidar1 = new LiDarWorkerTracker(1, 2, STATUS.UP, null);
        LiDarService service1 = new LiDarService(lidar1);
        LiDarWorkerTracker lidar2 = new LiDarWorkerTracker(2, 2, STATUS.UP, null);
        LiDarService service2 = new LiDarService(lidar2);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(service1);
        bus.register(service2);
        bus.subscribeEvent(DetectObjectsEvent.class, service1);
        bus.subscribeEvent(DetectObjectsEvent.class, service2);
        assertEquals(2, bus.getEventsMap().get(DetectObjectsEvent.class).size());
    }

    @Test
    void subscribeBroadcastTest(){
        TimeService service = new TimeService(3,30);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(service);
        bus.subscribeBroadcast(TickBroadcast.class, service);
        assertTrue(bus.getBroadcastMap().get(TickBroadcast.class).contains(service));
    }

    @Test
    void sendEventTest(){
        Camera cam1 = new Camera(1, 3, STATUS.UP, "camera1", null);
        Camera cam2 = new Camera(2, 3, STATUS.UP, "camera2", null);
        Camera cam3 = new Camera(3, 3, STATUS.UP, "camera3", null);
        CameraService service1 = new CameraService(cam1);
        CameraService service2 = new CameraService(cam2);
        CameraService service3 = new CameraService(cam3);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(service1);
        bus.register(service2);
        bus.register(service3);
        bus.subscribeEvent(PoseEvent.class, service1);
        bus.subscribeEvent(PoseEvent.class, service2);
        bus.subscribeEvent(PoseEvent.class, service3);
        int oldSize = bus.getEventsMap().get(PoseEvent.class).size();
        PoseEvent e = new PoseEvent("test", new Pose(1,2,3,4));
        bus.sendEvent(e);
        assertEquals(oldSize, bus.getEventsMap().get(PoseEvent.class).size());
    }

    @Test
    void sendBroadcastTest(){
        FusionSlam fusionSlam = FusionSlam.getInstance();
        FusionSlamService service = new FusionSlamService(fusionSlam);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(service);
        bus.subscribeBroadcast(TickBroadcast.class, service);
        int oldSize = bus.getMicroServiceMap().get(service).size();
        bus.sendBroadcast(new TickBroadcast("", 3));
        assertEquals(oldSize + 1, bus.getMicroServiceMap().get(service).size());
    }

    @Test
    void completeTest(){
        Camera camera = new Camera(3, 3, STATUS.UP, "camera3", null);
        CameraService service = new CameraService(camera);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(service);
        DetectObjectsEvent e = new DetectObjectsEvent(null,"", 5);
        bus.sendEvent(e);
        //bus.complete(e, 5); //לברר עם מרעי את עניין הפיוצ'רים
        assertTrue(bus.getFutureMap().get(e).isDone() == true);
    }

    @Test
    void unregisterTest(){
        Camera camera = new Camera(3, 3, STATUS.UP, "camera3", null);
        CameraService cameraService = new CameraService(camera);
        TimeService timeService = new TimeService(3,30);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(cameraService);
        bus.register(timeService);
        int oldSize = bus.getMicroServiceMap().size();
        bus.unregister(cameraService);
        assertEquals(oldSize - 1, bus.getMicroServiceMap().size());

    }

    @Test
    void awaitMessageTest() {
        LiDarWorkerTracker lidar = new LiDarWorkerTracker(1, 2, STATUS.UP, null);
        LiDarService service = new LiDarService(lidar);
        MessageBusImpl bus = MessageBusImpl.getInstance();
        bus.register(service);
        TickBroadcast b = new TickBroadcast("test", 3);
        bus.subscribeBroadcast(TickBroadcast.class, service);
        bus.sendBroadcast(b);
        try {
            bus.awaitMessage(service);
        }
        catch (InterruptedException e) {}
        assertTrue(bus.getMicroServiceMap().get(service).isEmpty());
    }
}
