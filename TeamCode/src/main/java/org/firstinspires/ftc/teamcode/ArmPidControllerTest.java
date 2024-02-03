package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
@Config
public class ArmPidControllerTest extends OpMode {

    private PIDFController controller;

    public static double p = 2, i = 0, d = 0;
    public static double f = 0.002;

    public static int target = 0;//good to go
    private final double ticks_in_degree = 28.0 / 360.0;

    private DcMotorEx arm_motor;
    @Override
    public void init() {

        controller = new PIDFController(p,i,d,f);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        arm_motor = hardwareMap.get(DcMotorEx.class, "lift");
        arm_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        target = arm_motor.getCurrentPosition();


    }

    @Override
    public void loop() {
        controller.setPIDF(p,i,d,f);
        int armPos = arm_motor.getCurrentPosition();
        double pid = controller.calculate(armPos, target);

        arm_motor.setVelocity(pid);

        telemetry.addData("f", f);
        telemetry.addData("pid", pid);
        telemetry.addData("pos", armPos);
        telemetry.addData("target", target);
        telemetry.update();

    }
}
