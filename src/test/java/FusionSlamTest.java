
import bgu.spl.mics.application.objects.CloudPoint;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.TrackedObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FusionSlamTest {

   @Test
    public void handle_trackedObjectEventTest1() {
        FusionSlam fusionSlam = FusionSlam.getInstance();
        assertTrue(fusionSlam.getLandMarks().isEmpty());
   }

    @Test
    public void handle_trackedObjectEventTest2() {
        FusionSlam fusionSlam = FusionSlam.getInstance();
        List<CloudPoint> list = new ArrayList<>();
        CloudPoint cp1 = new CloudPoint(1,2);
        //TrackedObject o = new TrackedObject("test", 2, "test",);
//////WTF?!?!?!?!?!?!?!?!
    }

    @Test
    public void handle_trackedObjectEventTest3() {

    }

    @Test
    public void handle_trackedObjectEventTest4() {

    }

}
