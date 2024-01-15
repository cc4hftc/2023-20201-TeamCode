package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@Autonomous(name="RedPArk", group="eric")
public class Red_Park extends LinearOpMode {
    DistanceSensor Ldistance;
    DistanceSensor Rdistance;
    private CRServo claw = null;
    private CRServo claw2 = null;
    private DcMotor lift = null;
    public int Strt_to_center=38+3;
    public int Rbot_width=18-8;
    public int Center_to_tapeSIDE=11+7;
    public int Strt_to_TAPEForwardOnly=46+8;
    public int Colision_Tune_distance=4;
    @Override
    public void runOpMode() {

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Ldistance = hardwareMap.get(DistanceSensor.class, "Lsensor");
        Rdistance = hardwareMap.get(DistanceSensor.class, "Rsensor");

        claw = hardwareMap.get(CRServo.class, "claw1");
        claw2 = hardwareMap.get(CRServo.class, "claw2");
        claw2.setDirection(CRServo.Direction.REVERSE);
        lift = hardwareMap.get(DcMotor.class, "lift");

        Trajectory Drive_to_center = drive.trajectoryBuilder(new Pose2d())
                .forward((Strt_to_center-Rbot_width))
                .build();
        Trajectory SideShiftLeft = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(Center_to_tapeSIDE)
                .build();
        Trajectory SideShiftRight= drive.trajectoryBuilder( new Pose2d())
                .strafeRight(Center_to_tapeSIDE)
                .build();
        Trajectory Go_To_Forward_tape=drive.trajectoryBuilder( new Pose2d())
                .forward(Strt_to_TAPEForwardOnly-Strt_to_center)
                .build();
        Trajectory PARK=drive.trajectoryBuilder( new Pose2d())
                .strafeRight(99)
                .build();
        waitForStart();

        if (isStopRequested()) return;
        Trajectory backwards_Tune = drive.trajectoryBuilder(new Pose2d())
                .back(Colision_Tune_distance)
                .build();
        drive.followTrajectory(Drive_to_center);
        telemetry.addData("Rdistance: ",Rdistance.getDistance(DistanceUnit.CM));
        telemetry.update();
       if (Ldistance.getDistance(DistanceUnit.CM)<=30){
           drive.followTrajectory(backwards_Tune);
           drive.followTrajectory(SideShiftLeft);
           claw.setPower(1.0);
           claw2.setPower(1.0);
        } else if (Rdistance.getDistance(DistanceUnit.CM)<=30) {
           drive.followTrajectory(backwards_Tune);
           drive.followTrajectory(SideShiftRight);
           claw.setPower(1.0);
           claw2.setPower(1.0);
       } else{
           claw.setPower(1.0);
           claw2.setPower(1.0);
       }
        sleep(3000);
       drive.followTrajectory(backwards_Tune);
        claw.setPower(0.00);
        claw2.setPower(0.00);
        drive.followTrajectory(PARK);
}}
