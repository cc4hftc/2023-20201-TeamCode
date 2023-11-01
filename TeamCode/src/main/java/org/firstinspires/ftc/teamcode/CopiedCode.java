package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@Autonomous(name="ByeByeBye1", group="eric")
public class CopiedCode extends LinearOpMode {
    DistanceSensor Ldistance;
    DistanceSensor Rdistance;
    DcMotor intake_left = null;
    DcMotor intake_right = null;
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Ldistance = hardwareMap.get(DistanceSensor.class, "Lsensor");
        Rdistance = hardwareMap.get(DistanceSensor.class, "Rsensor");

        intake_left  = hardwareMap.get(DcMotor.class, "intakeLeft");
        intake_right = hardwareMap.get(DcMotor.class, "intakeRight");
        intake_left.setDirection(DcMotorSimple.Direction.REVERSE);

        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d())
                .forward(10)
                .build();

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectory(myTrajectory);

        intake_left.setPower(-1.00);
        intake_right.setPower(-1.00);
        sleep(1000);
        intake_left.setPower(0);
        intake_right.setPower(0);

        telemetry.addData("Left", Ldistance.getDistance(DistanceUnit.CM));
        telemetry.addData("Right", Rdistance.getDistance(DistanceUnit.CM));
        telemetry.update();
    }
}
