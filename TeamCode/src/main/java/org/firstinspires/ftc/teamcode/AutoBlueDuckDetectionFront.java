package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Config
@Disabled
@Autonomous(name="Blue Front Duck Detect", group="DucksAuto")
public class AutoBlueDuckDetectionFront extends LinearOpMode {

    public static double STARTING_POSE_X = -36;
    public static double STARTING_POSE_Y = 61;
    public static double STARTING_POSE_ANGLE = 270;
    public static double LEFT_LINE_POSE_X = -36;
    public static double LEFT_LINE_POSE_Y = 32;
    public static double LEFT_LINE_POSE_ANGLE = 0;
    public static double CENTER_LINE_POSE_X = -36;
    public static double CENTER_LINE_POSE_Y = 36;
    public static double CENTER_LINE_POSE_ANGLE = 270;
    public static double RIGHT_LINE_POSE_X = -46;
    public static double RIGHT_LINE_POSE_Y = 40;
    public static double RIGHT_LINE_POSE_ANGLE = 270;
    public static double LEFT_PIXEL_RETREAT = 8; // in
    public static double LEFT_STRAFE = 22; // in
    public static double LEFT_FORWARD_TO_PARK = 104; // in
    public static double CENTER_PIXEL_RETREAT = 6; // in
    public static double CENTER_STRAFE = 16; // in
    public static double CENTER_FORWARD = 30; // in
    public static double CENTER_STRAFE_TO_PARK = 112; // in
    public static double RIGHT_STRAFE = 12; // in
    public static double RIGHT_FORWARD = 29; // in
    public static double RIGHT_STRAFE_TO_PARK = 94; // in
    public static long PIXEL_SERVO_WAIT_MILLISECS = 2500;

    DistanceSensor Ldistance;
    DistanceSensor Rdistance;
    private CRServo claw1 = null;
    private CRServo claw2 = null;
    private DcMotor lift = null;

    private static final boolean USE_WEBCAM = true; // true for webcam, false for phone camera
    //private AprilTagProcessor aprilTag;
    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    private static final String TFOD_MODEL_ASSET = "DucksPropModel_Full0.tflite";
    // TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller's storage,
    // this is used when uploading models directly to the RC using the model upload interface.
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/myCustomModel.tflite";
    // Define the labels recognized in the model for TFOD (must be in training order!)
    private static final String[] LABELS = {
            "duckprop",
    };

    // Camera resolution
    private static final int CAMHORIZRES = 640;
    private static final int CAMVERTRES = 480;

    // Duck Spike Mark Detection Thresholds
    private static final int  LEFTSPIKETHRESHOLD = 200;
    private static final int  RIGHTSPIKETHRESHOLD = 440;

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;

    /**
     * The variable to store the minimum confidence of duck prop recognition.
     */
    private float tfodMinConfidence = 0.92f;

    /**
     * The variable to store the location of the prop (1=>left, 2=>center, 3=>right)
     */
    private int spikeMark = 0;

    /**
     * The variable to store our instance of the vision portal.
     */
    private  VisionPortal visionPortal;


    @Override
    public void runOpMode() {

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Ldistance = hardwareMap.get(DistanceSensor.class, "Lsensor");
        Rdistance = hardwareMap.get(DistanceSensor.class, "Rsensor");

        claw1 = hardwareMap.get(CRServo.class, "claw1");
        claw2 = hardwareMap.get(CRServo.class, "claw2");
        claw2.setDirection(CRServo.Direction.REVERSE);
        lift = hardwareMap.get(DcMotor.class, "lift");
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        initDoubleVision();
        visionPortal.setProcessorEnabled(aprilTag, false);

        Pose2d startingPose = new Pose2d(
                STARTING_POSE_X,
                STARTING_POSE_Y,
                Math.toRadians(STARTING_POSE_ANGLE));
        drive.setPoseEstimate(startingPose);

        Pose2d blueFrontLeftLine = new Pose2d(
                LEFT_LINE_POSE_X,
                LEFT_LINE_POSE_Y,
                Math.toRadians(LEFT_LINE_POSE_ANGLE));
        Pose2d blueFrontCenterLine = new Pose2d(
                CENTER_LINE_POSE_X,
                CENTER_LINE_POSE_Y,
                Math.toRadians(CENTER_LINE_POSE_ANGLE));
        Pose2d blueFrontRightLine = new Pose2d(
                RIGHT_LINE_POSE_X,
                RIGHT_LINE_POSE_Y,
                Math.toRadians(RIGHT_LINE_POSE_ANGLE));

        waitForStart();

        if (opModeIsActive()) {
            int iterations = 1;
            boolean propFound = false;
            while ( iterations < 1000 && spikeMark == 0 ) {
                //while ( opModeIsActive() ) {
                List<Recognition> currentRecognitions = tfod.getRecognitions();
                for ( Recognition recognition : currentRecognitions ) {
                    double x = (recognition.getLeft() + recognition.getRight()) / 2;

                    if ( x <= LEFTSPIKETHRESHOLD ) {
                        telemetry.addData("Prop Location", "Left");
                        telemetry.update();
                        propFound = true;
                        spikeMark = 1;
                        //visionPortal.setProcessorEnabled(tfod, false);
                        break;
                    } else if ( x > LEFTSPIKETHRESHOLD && x <= RIGHTSPIKETHRESHOLD ) {
                        telemetry.addData("Prop Location", "Center");
                        telemetry.update();
                        propFound = true;
                        spikeMark = 2;
                        //visionPortal.setProcessorEnabled(tfod, false);
                        break;
                    } else if ( x > RIGHTSPIKETHRESHOLD ) {
                        telemetry.addData("Prop Location", "Right");
                        telemetry.update();
                        propFound = true;
                        spikeMark = 3;
                        //visionPortal.setProcessorEnabled(tfod, false);
                        break;
                    }
                }
                sleep(25);
                iterations++;
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

        if (isStopRequested()) return;

        // Drive to the prop spike mark
        if ( spikeMark == 1 ) { // Left
            drive.followTrajectory(
                    drive.trajectoryBuilder( startingPose )
                            .splineTo(
                                    blueFrontLeftLine.vec(),
                                    blueFrontLeftLine.getHeading())
                            .build()
            );
        } else if ( spikeMark == 2 ) { // Center
            drive.followTrajectory(
                    drive.trajectoryBuilder( startingPose )
                            .lineToSplineHeading( blueFrontCenterLine )
                            .build()
            );
        } else if ( spikeMark == 3 ) { // Right
            drive.followTrajectory(
                    drive.trajectoryBuilder( startingPose )
                            .lineToSplineHeading(blueFrontRightLine)
                            .build()
            );
        }

        // Deposit the purple pixel
        claw1.setPower(1.0);
        claw2.setPower(1.0);
        sleep(PIXEL_SERVO_WAIT_MILLISECS);
        claw1.setPower(0.0);
        claw2.setPower(0.0);

        // Drive to park by backdrop
        if ( spikeMark == 1 ) { // Left
            Trajectory leftPixelRetreat = drive.trajectoryBuilder( blueFrontLeftLine )
                    .back(LEFT_PIXEL_RETREAT)
                    .build();
            drive.followTrajectory( leftPixelRetreat );
            Trajectory leftStrafe = drive.trajectoryBuilder( leftPixelRetreat.end() )
                    .strafeLeft(LEFT_STRAFE)
                    .build();
            drive.followTrajectory( leftStrafe );
            Trajectory leftForwardToPark = drive.trajectoryBuilder( leftStrafe.end() )
                    .forward(LEFT_FORWARD_TO_PARK)
                            .build();
            drive.followTrajectory( leftForwardToPark );
        } else if ( spikeMark == 2 ) { // Center
            Trajectory centerPixelRetreat = drive.trajectoryBuilder( blueFrontCenterLine )
                    .back(CENTER_PIXEL_RETREAT)
                    .build();
            drive.followTrajectory( centerPixelRetreat );
            Trajectory centerStrafe = drive.trajectoryBuilder( centerPixelRetreat.end() )
                    .strafeRight(CENTER_STRAFE)
                    .build();
            drive.followTrajectory( centerStrafe );
            Trajectory centerForward = drive.trajectoryBuilder( centerStrafe.end() )
                    .forward(CENTER_FORWARD)
                    .build();
            drive.followTrajectory( centerForward );
            Trajectory centerStrafeToPark = drive.trajectoryBuilder( centerForward.end() )
                    .strafeLeft(CENTER_STRAFE_TO_PARK)
                    .build();
            drive.followTrajectory( centerStrafeToPark );
        } else if ( spikeMark == 3 ) { // Right
            Trajectory rightStrafe = drive.trajectoryBuilder( blueFrontRightLine )
                    .strafeLeft(RIGHT_STRAFE)
                    .build();
            drive.followTrajectory( rightStrafe );
            Trajectory rightForward = drive.trajectoryBuilder( rightStrafe.end() )
                    .forward(RIGHT_FORWARD)
                    .build();
            drive.followTrajectory( rightForward );
            Trajectory rightStrafeToPark = drive.trajectoryBuilder( rightForward.end() )
                    .strafeLeft(RIGHT_STRAFE_TO_PARK)
                    .build();
            drive.followTrajectory( rightStrafeToPark );
        }

    } // end runOpMode()

    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initDoubleVision() {
        // Create the AprilTagProcessor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                .setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)
                .build();

        tfod.setMinResultConfidence(tfodMinConfidence);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(new Size(CAMHORIZRES, CAMVERTRES));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessors(tfod, aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initDoubleVision()

    private void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

    }   // end method telemetryAprilTag()

    private String getPropLocation( double x ) {
        if ( x <= LEFTSPIKETHRESHOLD ) {
            return "Left";
        } else if ( x > LEFTSPIKETHRESHOLD && x <= RIGHTSPIKETHRESHOLD ) {
            return "Center";
        } else if ( x > RIGHTSPIKETHRESHOLD ) {
            return "Right";
        } else {
            return "Unknown";
        }
    }

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y, getPropLocation(x));
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

    }   // end method telemetryTfod()
}
