package org.firstinspires.ftc.teamcode;

// Import general FTC and motion libraries
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// Import libraries needed for webcam
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

// Import libraries needed for vision portal with specified webcam resolution
import org.firstinspires.ftc.vision.VisionPortal;
import android.util.Size;

// Import April Tag libraries
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

// Import TensorFlow libraries
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

// Import list for iterating over objects detected
import java.util.List;
@Disabled
public class DucksAutonomous extends LinearOpMode {

    // Red Frontstage
    public static final Pose2d redFrontStartingPose = new Pose2d(-36, -61, Math.toRadians(90));
    public static final Pose2d redFrontLeftLine = new Pose2d(-46, -40 , Math.toRadians(90));
    public static final Pose2d redFrontCenterLine = new Pose2d(-36, -36, Math.toRadians(90));
    public static final Pose2d redFrontRightLine = new Pose2d(-36, -32, Math.toRadians(0));

    // Red Backstage
    public static final Pose2d redBackStartingPose = new Pose2d(12, -61, Math.toRadians(90));
    public static final Pose2d redBackLeftLine = new Pose2d(13.5, -30, Math.toRadians(180));
    public static final Pose2d redBackCenterLine = new Pose2d(12, -36, Math.toRadians(90));
    public static final Pose2d redBackRightLine = new Pose2d(22.5, -42, Math.toRadians(90));

    // Blue Frontstage
    public static final Pose2d blueFrontStartingPose = new Pose2d(-36, 61, Math.toRadians(270));
    public static final Pose2d blueFrontLeftLine = new Pose2d(-36, 32 , Math.toRadians(0));
    public static final Pose2d blueFrontCenterLine = new Pose2d(-36, 36, Math.toRadians(270));
    public static final Pose2d blueFrontRightLine = new Pose2d(-46, 40, Math.toRadians(270));

    // Blue Backstage
    public static final Pose2d blueBackStartingPose = new Pose2d(12, 61, Math.toRadians(270));
    public static final Pose2d blueBackLeftLine = new Pose2d(22.5, 40, Math.toRadians(270));
    public static final Pose2d blueBackCenterLine = new Pose2d(12, 36, Math.toRadians(270));
    public static final Pose2d blueBackRightLine = new Pose2d(13.5, 32, Math.toRadians(180));

    // Waypoints
    public static final Pose2d redStartingPoseReturnOffset = new Pose2d(0, 3, 0);
    public static final Pose2d redBackStageWaypoint = new Pose2d(36, -58, Math.toRadians(0));
    public static final Pose2d blueStartingPoseReturnOffset = new Pose2d(0, -3, 0);
    public static final Pose2d blueBackStageWaypoint = new Pose2d(36, 58, Math.toRadians(270));

    // Backdrop
    public static final Pose2d redBackdropLeft = new Pose2d(40, -30, Math.toRadians(0));;
    public static final Pose2d redBackdropCenter = new Pose2d(40, -36, Math.toRadians(0));;
    public static final Pose2d redBackdropRight = new Pose2d(40, -42, Math.toRadians(0));;
    public static final Pose2d blueBackdropLeft = new Pose2d(40, 42, Math.toRadians(0));;
    public static final Pose2d blueBackdropCenter = new Pose2d(40, 36, Math.toRadians(0));;
    public static final Pose2d blueBackdropRight = new Pose2d(4, 30, Math.toRadians(0));;

    // Parking
    public static final Pose2d redBackdropLeftParkWaypoint = new Pose2d(45, -14, Math.toRadians(180));
    public static final Pose2d redBackdropLeftPark = new Pose2d(59, -11, Math.toRadians(180));
    public static final Pose2d redBackdropRightParkWaypoint = new Pose2d(45, -58, Math.toRadians(180));
    public static final Pose2d redBackdropRightPark = new Pose2d(59, -58, Math.toRadians(180));
    public static final Pose2d blueBackdropLeftParkWaypoint = new Pose2d(45, 57, Math.toRadians(180));
    public static final Pose2d blueBackdropLeftPark = new Pose2d(59, 58, Math.toRadians(180));
    public static final Pose2d blueBackdropRightParkWaypoint = new Pose2d(45, 14, Math.toRadians(180));
    public static final Pose2d blueBackdropRightPark = new Pose2d(59, 11, Math.toRadians(180));

    DistanceSensor Ldistance;
    DistanceSensor Rdistance;
    private CRServo claw1 = null;
    private CRServo claw2 = null;
    private DcMotor lift = null;

    private static final boolean USE_WEBCAM = true; // true for webcam, false for phone camera

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

        Pose2d startingPose = DucksTrajectories.blueBackStartingPose;
        drive.setPoseEstimate(startingPose);

        waitForStart();

        if (opModeIsActive()) {
            int iterations = 1;
            boolean propFound = false;
            while ( iterations < 4000 && spikeMark == 0 ) {
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
                    drive.trajectoryBuilder( DucksTrajectories.blueBackStartingPose )
                            .lineToSplineHeading(DucksTrajectories.blueBackLeftLine)
                            .build()
            );
        } else if ( spikeMark == 2 ) { // Center
            drive.followTrajectory(
                    drive.trajectoryBuilder( DucksTrajectories.blueBackStartingPose )
                            .lineToSplineHeading(DucksTrajectories.blueBackCenterLine)
                            .build()
            );
        } else if ( spikeMark == 3 ) { // Right
            drive.followTrajectory(
                    drive.trajectoryBuilder( DucksTrajectories.blueBackStartingPose )
                            .splineTo(
                                    DucksTrajectories.blueBackRightLine.vec(),
                                    DucksTrajectories.blueBackRightLine.getHeading())
                            .build()
            );
        }

        // Deposit the purple pixel
        claw1.setPower(1.0);
        claw2.setPower(1.0);
        sleep(2500);
        claw1.setPower(0.0);
        claw2.setPower(0.0);

        // Drive to park by backdrop
        if ( spikeMark == 1 ) { // Left
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .back(12)
                            .build()
            );
            drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder( new Pose2d() )
                            .turn(Math.toRadians(90))
                            .build()
            );
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .strafeLeft(8)
                            .build()
            );
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .forward(36)
                            .build()
            );
        } else if ( spikeMark == 2 ) { // Center
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .back(12)
                            .build()
            );
            drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder( new Pose2d() )
                            .turn(Math.toRadians(90))
                            .build()
            );
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .strafeLeft(10)
                            .build()
            );
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .forward(46)
                            .build()
            );
        } else if ( spikeMark == 3 ) { // Right
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .back(12)
                            .build()
            );
            drive.followTrajectorySequence(
                    drive.trajectorySequenceBuilder( new Pose2d() )
                            .turn(Math.toRadians(-180))
                            .build()
            );
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .strafeLeft(28)
                            .build()
            );
            drive.followTrajectory(
                    drive.trajectoryBuilder( new Pose2d() )
                            .forward(32)
                            .build()
            );
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
