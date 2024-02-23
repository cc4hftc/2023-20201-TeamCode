package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.fakes.TestOpMode;
@Disabled
public class TestableOpModeExample extends TestOpMode {

    DcMotor leftDrive;
    DcMotor rightDrive;
    DistanceSensor distanceSensor;
    DistanceSensor distanceSensor2;
    DigitalChannel digitalTouch;

    double distance = -1;


    @Override
    public void init() {
        String myName = "Alan Smith";
        telemetry.addData("Hello",myName);
        telemetry.update();

        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distance_sensor");
        distanceSensor2 = hardwareMap.get(DistanceSensor.class, "distance_sensor2");
        digitalTouch = hardwareMap.get(DigitalChannel.class, "sensor_digital");

        digitalTouch.setMode(DigitalChannel.Mode.INPUT);
    }

    @Override
    public void loop() {
        leftDrive.setPower(1);
        rightDrive.setPower(1);
        telemetry.addData("Left stick x", gamepad1.left_stick_x);
        telemetry.addData("Left stick y", gamepad1.left_stick_y);
        telemetry.addData("leftDrive Power ", leftDrive.getPower());
        telemetry.addData("Distance", distanceSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("Distance2", distanceSensor2.getDistance(DistanceUnit.CM));
        telemetry.addData("specialDist", distance);
        telemetry.addData("A button", gamepad1.a);
        if (digitalTouch.getState() == true) {
            telemetry.addData("Digital Touch", "Is Not Pressed");
        } else {
            distance = distanceSensor2.getDistance(DistanceUnit.CM);
            telemetry.addData("Digital Touch", "Is Pressed");
        }

        telemetry.update();
    }
}
