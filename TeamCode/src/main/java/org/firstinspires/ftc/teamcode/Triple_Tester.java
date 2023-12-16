package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@Autonomous(name="ArmUp/Down", group="eric")
public class  Triple_Tester extends LinearOpMode {
    DistanceSensor Ldistance;
    DistanceSensor Rdistance;
    private CRServo claw = null;
    private CRServo claw2 = null;
    private DcMotor lift = null;
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Ldistance = hardwareMap.get(DistanceSensor.class, "Lsensor");
        Rdistance = hardwareMap.get(DistanceSensor.class, "Rsensor");

        claw = hardwareMap.get(CRServo.class, "claw1");
        claw2 = hardwareMap.get(CRServo.class, "claw2");
        claw2.setDirection(CRServo.Direction.REVERSE);
        lift = hardwareMap.get(DcMotor.class, "lift");

        if(isStopRequested()) return;

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setTargetPosition(lift.getCurrentPosition()-440);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(-0.1);
        while (lift.isBusy()) {
            lift.setPower(-0.1);
        }
        lift.setPower(0.0);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw.setPower(-1.0);
        claw2.setPower(-1.0);
        sleep(3000);
        claw.setPower(0.00);
        claw2.setPower(0.00);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setTargetPosition(lift.getCurrentPosition()+480);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(0.3);
        while (lift.isBusy()) {
            lift.setPower(0.3);
        }
        lift.setPower(0.0);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }}
