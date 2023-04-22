package org.cc4hrobotics.ftc;

import static org.mockito.Mockito.mock;

import android.content.Context;

import org.cc4hrobotics.ftc.fakes.FakeHardwareMap;
import org.cc4hrobotics.ftc.fakes.drive.FakeExtendedDcMotor;
import org.cc4hrobotics.ftc.fakes.sensors.FakeDistanceSensor;
import org.junit.Before;
import org.junit.Test;

public class LearnJavaTest {

    private FakeHardwareMap hardwareMap;

    private FakeExtendedDcMotor leftDriveMotor;
    private FakeExtendedDcMotor rightDriveMotor;
    private FakeDistanceSensor distanceSensor;

    @Test
    public void RunIt() {
        double distance = 10;
        LearnJavaOpMode ljt = new LearnJavaOpMode();
        ljt.hardwareMap = hardwareMap;
        ljt.init();

        sleep(5);

        distanceSensor.setDistance(distance);
        for (int i = 0; i< 100; i++ ) {
            ljt.loop();

                //Make adjustments to the gamepad and watch robot adjust
                ljt.gamepad1.left_stick_x -= 0.03;
                ljt.gamepad1.left_stick_y += 0.02;
                ljt.gamepad1.a = !ljt.gamepad1.a;

                if (leftDriveMotor.getPower() > 0 && rightDriveMotor.getPower() > 0)  {
                    distance = distance - 1;
                } else if (leftDriveMotor.getPower() < 0 && rightDriveMotor.getPower() < 0)  {
                    distance = distance + 1;
                }
                distanceSensor.setDistance(distance);
                sleep(3);
        }
    }


    @Before
    public void setup() {

        hardwareMap = new FakeHardwareMap(mock(Context.class));

        leftDriveMotor = new FakeExtendedDcMotor();
        hardwareMap.addDevice("left_drive", leftDriveMotor);

        rightDriveMotor = new FakeExtendedDcMotor();
        hardwareMap.addDevice("right_drive", rightDriveMotor);

        distanceSensor = new FakeDistanceSensor();
        hardwareMap.addDevice("distance_sensor", distanceSensor);
    }

    public void sleep(long seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
