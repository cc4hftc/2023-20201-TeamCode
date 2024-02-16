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
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.List;


@Autonomous(name="Red Backstage Duck Detect", group="DucksAuto")
public class AutoRedDuckDetection extends LinearOpMode {
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
     * The variable to store the location of the prop (0=>left, 1=>center, 2=>right)
     */
    private int randomizationLocation;

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

//        Trajectory Drive_to_center = drive.trajectoryBuilder(new Pose2d())
//                .forward((Strt_to_center-Rbot_width) )
//                .build();
//        Trajectory SideShiftLeft = drive.trajectoryBuilder(new Pose2d())
//                .strafeLeft(Center_to_tapeSIDE)
//                .build();
//        Trajectory SideShiftRight= drive.trajectoryBuilder( new Pose2d())
//                .strafeRight(Center_to_tapeSIDE)
//                .build();
//        Trajectory Go_To_Forward_tape=drive.trajectoryBuilder( new Pose2d())
//                .forward(14)
//                .build();
//        Trajectory Go_Back_Forward_tape=drive.trajectoryBuilder( new Pose2d())
//                .back(12)
//                .build();
//        Trajectory PARK=drive.trajectoryBuilder( new Pose2d())
//                .strafeRight(99)
//                .build();

        waitForStart();

        if (opModeIsActive()) {
            int iterations = 1;
            while ( iterations < 400 ) {
                List<Recognition> currentRecognitions = tfod.getRecognitions();
                if ( currentRecognitions.size() > 1 ) {
                    Recognition recognition = currentRecognitions.get(0);
                    double x = (recognition.getLeft() + recognition.getRight()) / 2;
                    if ( x <= 220 ) {
                        telemetry.addData("Prop Location", "Left");
                        telemetry.update();
                        randomizationLocation = 1;
                        visionPortal.setProcessorEnabled(tfod, false);
                        break;
                    } else if ( x > 220 && x <= 420 ) {
                        telemetry.addData("Prop Location", "Center");
                        telemetry.update();
                        randomizationLocation = 2;
                        visionPortal.setProcessorEnabled(tfod, false);
                        break;
                    } else if ( x > 420 ) {
                        telemetry.addData("Prop Location", "Right");
                        telemetry.update();
                        randomizationLocation = 3;
                        visionPortal.setProcessorEnabled(tfod, false);
                        break;
                    }
                }
                sleep(25);
                iterations++;
            }
        }

//        if (isStopRequested()) return;
//        Trajectory backwards_Tune = drive.trajectoryBuilder(new Pose2d())
//                .back(Colision_Tune_distance)
//                .build();
//
//        drive.followTrajectory(Drive_to_center);
//
//        if (Ldistance.getDistance(DistanceUnit.CM)<=30){
//            lift.setPower(0.5);
//            sleep(3500);
//            lift.setPower(0.0);
//            sleep(1000);
//            drive.followTrajectory(backwards_Tune);
//            drive.followTrajectory(SideShiftLeft);
//            claw1.setPower(1.0);
//            claw2.setPower(1.0);
//        } else if (Rdistance.getDistance(DistanceUnit.CM)<=30) {
//            lift.setPower(0.5);
//            sleep(3500);
//            lift.setPower(0.0);
//            sleep(1000);
//            drive.followTrajectory(backwards_Tune);
//            drive.followTrajectory(SideShiftRight);
//            claw1.setPower(1.0);
//            claw2.setPower(1.0);
//        } else{
//            drive.followTrajectory(Go_Back_Forward_tape);
//            lift.setPower(0.5);
//            sleep(3500);
//            lift.setPower(0.0);
//            sleep(1000);
//            drive.followTrajectory(Go_To_Forward_tape);
//            claw1.setPower(1.0);
//            claw2.setPower(1.0);
//        }
//        sleep(3000);
//       drive.followTrajectory(backwards_Tune);
//        claw1.setPower(0.00);
//        claw2.setPower(0.00);
//        drive.followTrajectory(PARK);

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

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
        //builder.setCameraResolution(new Size(640, 480));

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
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

    }   // end method telemetryTfod()
}
