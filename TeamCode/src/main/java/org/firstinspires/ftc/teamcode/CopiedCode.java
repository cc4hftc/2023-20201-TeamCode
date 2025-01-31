package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@Autonomous(name="DistanceSensor_ExtraDriving", group="eric")
public class CopiedCode extends LinearOpMode {
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

        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d())
                .forward(3.6)
                .build();

        waitForStart();

        if (isStopRequested()) return;
        Trajectory backwards = drive.trajectoryBuilder(new Pose2d())
                        .back(1.0)
                                .build();
        drive.followTrajectory(myTrajectory);
        telemetry.addData("Rdistance: ",Rdistance.getDistance(DistanceUnit.CM));
        telemetry.update();
       if (Ldistance.getDistance(DistanceUnit.CM)<=30){
            myTrajectory = drive.trajectoryBuilder(new Pose2d())
                    .strafeLeft(2.0)
                    .build();
        } else if (Rdistance.getDistance(DistanceUnit.CM)<=30){
            myTrajectory = drive.trajectoryBuilder( new Pose2d())
                    .strafeRight(1.2)
                    .build();
        } else{
            myTrajectory = drive.trajectoryBuilder(new Pose2d())
                    .forward(1.0)
                    .build();
        }
        drive.followTrajectory(myTrajectory);
        drive.followTrajectory(backwards);
        claw.setPower(1.0);
        claw2.setPower(1.0);
        sleep(3000);
        claw.setPower(0.00);
        claw2.setPower(0.00);
}}
