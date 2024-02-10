package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.round;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * This is an example minimal implementation of the mecanum drivetrain
 * for demonstration purposes.  Not tested and not guaranteed to be bug free.
 *
 * @author Brandon Gong
 */
@TeleOp(name="Duck's Mecanum Drive", group="Iterative Opmode")
@Config
public class Driver_Controlled extends OpMode {
    /*
     * The mecanum drivetrain involves four separate motors that spin in
     * different directions and different speeds to produce the desired
     * movement at the desired speed.
     */

    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;
    private DcMotorEx lift = null;
    private DcMotor hang = null;
    private CRServo claw = null;
    private CRServo claw2 = null;
    private Servo plane_launcher = null;
    double POWER = 0;
    double multiplier;

    private PIDFController controller;

    public static double p = 12, i = 0, d = 0.002;
    public static double f = 0.002;

    public static int target = 0;//good to go
    private final double ticks_in_degree = 28.0 / 360.0;

    public static double armmult;

    @Override
    public void init() {

        // Name strings must match up with the config on the Robot Controller
        // app.
        front_left= hardwareMap.get(DcMotor.class, "leftFront");
        front_right  = hardwareMap.get(DcMotor.class, "rightFront");
        back_left    = hardwareMap.get(DcMotor.class, "leftRear");
        back_right   = hardwareMap.get(DcMotor.class, "rightRear");
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);

        lift = hardwareMap.get(DcMotorEx.class, "lift");

        hang = hardwareMap.get(DcMotor.class, "hang");

        claw = hardwareMap.get(CRServo.class, "claw1");
        claw2 = hardwareMap.get(CRServo.class, "claw2");
        claw2.setDirection(CRServo.Direction.REVERSE);

        plane_launcher = hardwareMap.get(Servo.class, "launcher");

        multiplier = 1;

        controller = new PIDFController(p,i,d,f);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        lift = hardwareMap.get(DcMotorEx.class, "lift");
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        target = lift.getCurrentPosition();

        armmult = 8;
    }

    @Override
    public void loop() {

        // Mecanum drive is controlled with three axes: drive (front-and-back),
        // strafe (left-and-right), and twist (rotating the whole chassis).
        double drive  = gamepad1.left_stick_y*-1;
        double strafe = gamepad1.left_stick_x;
        double twist  = gamepad1.right_stick_x;

        /*
         * If we had a gyro and wanted to do field-oriented control, here
         * is where we would implement it.
         *
         * The idea is fairly simple; we have a robot-oriented Cartesian (x,y)
         * coordinate (strafe, drive), and we just rotate it by the gyro
         * reading minus the offset that we read in the init() method.
         * Some rough pseudocode demonstrating:
         *
         * if Field Oriented Control:
         *     get gyro heading
         *     subtract initial offset from heading
         *     convert heading to radians (if necessary)
         *     new strafe = strafe * cos(heading) - drive * sin(heading)
         *     new drive  = strafe * sin(heading) + drive * cos(heading)
         *
         * If you want more understanding on where these rotation formulas come
         * from, refer to
         * https://en.wikipedia.org/wiki/Rotation_(mathematics)#Two_dimensions
         */

        // You may need to multiply some of these by -1 to invert direction of
        // the motor.  This is not an issue with the calculations themselves.
        double[] speeds = {
                (drive + strafe + twist),
                (drive - strafe - twist),
                (drive - strafe + twist),
                (drive + strafe - twist)
        };

        // Because we are adding vectors and motors only take values between
        // [-1,1] we may need to normalize them.

        // Loop through all values in the speeds[] array and find the greatest
        // *magnitude*.  Not the greatest velocity.
        double max = abs(speeds[0]);
        for (double speed : speeds) {
            if (max < abs(speed)) max = abs(speed);
        }

        // If and only if the maximum is outside of the range we want it to be,
        // normalize all the other speeds based on the given speed value.
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
        }

        // apply the calculated values to the motors.
        front_left.setPower(speeds[0] * multiplier);
        front_right.setPower(speeds[1] * multiplier);
        back_left.setPower(speeds[2] * multiplier);
        back_right.setPower(speeds[3] * multiplier);

        // if x and y
        // button pressed then launch airplane
        if (gamepad2.x && gamepad2.y) {
            plane_launcher.setPosition(180);
            try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
            plane_launcher.setPosition(0);
        }

        if (gamepad2.a) {//Close?
            claw.setPower(1.0);
            claw2.setPower(1.0);
        }
        else if (gamepad2.b) {//Open?----+
            claw.setPower(-1.0);
            claw2.setPower(-1.0);
        }
        else {
            claw.setPower(0.00);
            claw2.setPower(0.00);
        }

        if (gamepad1.left_bumper) {
            multiplier = 0.5;
        }
        else {
            multiplier = 1;
        }

        target += gamepad2.left_stick_y * armmult;

        if (target > 0) {
            target = 0;
        }

        hang.setPower(gamepad2.right_stick_y);
        hang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        controller.setPIDF(p,i,d,f);
        int armPos = lift.getCurrentPosition();
        double pid = controller.calculate(armPos, target);

        lift.setVelocity(pid);


        telemetry.addData("f", f);
        telemetry.addData("pid", pid);
        telemetry.addData("pos", armPos);
        telemetry.addData("target", target);
        telemetry.addData("stick", gamepad2.right_stick_y);
        telemetry.addData("armmult", armmult);
        telemetry.update();
    }
}