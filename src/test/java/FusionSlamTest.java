
import bgu.spl.mics.application.objects.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FusionSlamTest {

   @Test
    public void handle_trackedObjectEventTest() {
        FusionSlam fusionSlam = FusionSlam.getInstance();
        assertTrue(fusionSlam.getLandMarks().isEmpty());
   }

    @Test
    public void calculateGlobalCoordinatesTest() {
        FusionSlam fusionSlam = FusionSlam.getInstance();
        Pose pose = new Pose(5.0F, 10.0F, 30 , 1);
        CloudPoint c = new CloudPoint(2.0 , 3.0);
        CloudPoint ans = fusionSlam.calculateGlobalCoordinates(pose, c);
        assertEquals(5.232050807568878, ans.getX());
        assertEquals(13.598076211353316, ans.getY());
    }

    @Test
    public void calculateAvarageTest() {
       FusionSlam fusionSlam = FusionSlam.getInstance();
       List<CloudPoint> list = new ArrayList<>();
       list.add(new CloudPoint(2.0 , -3.0));
       list.add(new CloudPoint(-5.7 , 9.46));
       list.add(new CloudPoint(8.341 , 14.598));
       LandMark land = new LandMark("wall" , "wall" , list);
       List<CloudPoint> globalCoordinates = new ArrayList<>();
       globalCoordinates.add(new CloudPoint(5.68 , -2.7));
       globalCoordinates.add(new CloudPoint(7.894 , 10.53));
       globalCoordinates.add(new CloudPoint(-1.36 , 8.7));
       List<CloudPoint> ans = fusionSlam.calculateAvarage(land , globalCoordinates);
       assertEquals(3.84 , ans.get(0).getX());
       assertEquals(-2.85 , ans.get(0).getY());
       assertEquals(1.097 , ans.get(1).getX());
       assertEquals(9.995000000000001 , ans.get(1).getY());
       assertEquals(3.4904999999999995 , ans.get(2).getX());
       assertEquals(11.649000000000001 , ans.get(2).getY());
    }

}
