import bgu.spl.mics.application.objects.Camera;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.STATUS;
import bgu.spl.mics.application.objects.StampedDetectedObjects;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraTest {

    @Test
    public void handle_tickTest1() {
        List<StampedDetectedObjects> stampedList = new ArrayList<StampedDetectedObjects>();
        StampedDetectedObjects stamped1 = new StampedDetectedObjects(1, new ArrayList<>());
        ArrayList<DetectedObject> detectedObjects = new ArrayList<>();
        DetectedObject o1 = new DetectedObject("wall1", "test");
        DetectedObject o2 = new DetectedObject("wall2", "test");
        detectedObjects.add(o1);
        detectedObjects.add(o2);
        StampedDetectedObjects stamped2 = new StampedDetectedObjects(2, detectedObjects);
        stampedList.add(stamped1);
        stampedList.add(stamped2);
        Camera camera = new Camera(1, 2, STATUS.UP, "camera1", stampedList);
        assertEquals(stamped2.getDetectedObjects(), camera.handle_tick(4));
    }
}
