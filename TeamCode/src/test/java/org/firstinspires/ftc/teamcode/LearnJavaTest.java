package org.firstinspires.ftc.teamcode;

import static org.mockito.Mockito.mock;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.TestableOpMode;
import org.firstinspires.ftc.teamcode.fakes.FakeHardwareMap;
import org.firstinspires.ftc.teamcode.fakes.FakeTelemetry;
import org.firstinspires.ftc.teamcode.fakes.drive.FakeExtendedDcMotor;
import org.firstinspires.ftc.teamcode.fakes.sensors.FakeDigitalChannel;
import org.firstinspires.ftc.teamcode.fakes.sensors.FakeDistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.junit.Before;
import org.junit.Test;

public class LearnJavaTest {

    private FakeHardwareMap hardwareMap;

    private FakeExtendedDcMotor leftDriveMotor;
    private FakeExtendedDcMotor rightDriveMotor;
    private FakeDistanceSensor distanceSensor;
    private FakeDistanceSensor distanceSensor2;
    private FakeDigitalChannel digitalChannel;
    private FakeTelemetry fakeTelemetry = new FakeTelemetry();

    @Test
    public void RunIt() {
        double distance = 10;
        TestableOpMode ljt = new TestableOpMode();

        ljt.telemetry = fakeTelemetry;
        ljt.hardwareMap = hardwareMap;
        ljt.init();

        digitalChannel.setState(true);
        sleep(5);

        distanceSensor2.setDistance(100000);
        distanceSensor.setDistance(distance);
        for (int i = 0; i< 100; i++ ) {

            fakeTelemetry.setLoopCount(i);
            //Make adjustments to the gamepad and watch robot adjust
            ljt.gamepad1.left_stick_x -= 0.03;
            ljt.gamepad1.left_stick_y += 0.02;
            ljt.gamepad1.a = !ljt.gamepad1.a;

            if (leftDriveMotor.getPower() > 0 && rightDriveMotor.getPower() > 0)  {
                distance = distance - 1;
            } else if (leftDriveMotor.getPower() < 0 && rightDriveMotor.getPower() < 0)  {
                distance = distance + 1;
            }
            distanceSensor.setDistance(Math.max(distance, 0.0));

            distanceSensor2.setDistance(distanceSensor2.getDistance(DistanceUnit.CM)/2);

            if (i == 25 && digitalChannel.getMode() == DigitalChannel.Mode.INPUT) {
                digitalChannel.setState(false);
                //override to zero for testing...
                distanceSensor2.setDistance(3.14);
            } else {
                digitalChannel.setState(true);
            }



            ljt.loop();
            sleep(3);
        }
    }


    @Before
    public void setup() {

        hardwareMap = new FakeHardwareMap(mock(Context.class), mock(OpModeManagerNotifier.class));

        leftDriveMotor = new FakeExtendedDcMotor();
        hardwareMap.addDevice("left_drive", leftDriveMotor);

        rightDriveMotor = new FakeExtendedDcMotor();
        hardwareMap.addDevice("right_drive", rightDriveMotor);

        distanceSensor = new FakeDistanceSensor();
        hardwareMap.addDevice("distance_sensor", distanceSensor);

        distanceSensor2 = new FakeDistanceSensor();
        hardwareMap.addDevice("distance_sensor2", distanceSensor2);

        digitalChannel = new FakeDigitalChannel();
        hardwareMap.addDevice("sensor_digital", digitalChannel);


    }

    public void sleep(long seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
