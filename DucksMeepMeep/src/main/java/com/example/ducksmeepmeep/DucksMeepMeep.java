package com.example.ducksmeepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
public class DucksMeepMeep {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        double frontStageRobotBackdropDelay = 5.0;
        double backStageRobotBackdropDelay = 0.5;

        // Red Frontstage
        Pose2d redFrontStartingPose = new Pose2d(-36, -61, Math.toRadians(90));
        Pose2d redFrontLeftLine = new Pose2d(-46, -40 , Math.toRadians(90));
        Pose2d redFrontCenterLine = new Pose2d(-36, -36, Math.toRadians(90));
        Pose2d redFrontRightLine = new Pose2d(-36, -32, Math.toRadians(0));

        // Red Backstage
        Pose2d redBackStartingPose = new Pose2d(12, -61, Math.toRadians(90));
        Pose2d redBackLeftLine = new Pose2d(13.5, -32, Math.toRadians(180));
        Pose2d redBackCenterLine = new Pose2d(12, -36, Math.toRadians(90));
        Pose2d redBackRightLine = new Pose2d(22.5, -40, Math.toRadians(90));

        // Blue Frontstage
        Pose2d blueFrontStartingPose = new Pose2d(-36, 61, Math.toRadians(270));
        Pose2d blueFrontLeftLine = new Pose2d(-36, 32 , Math.toRadians(0));
        Pose2d blueFrontCenterLine = new Pose2d(-36, 36, Math.toRadians(270));
        Pose2d blueFrontRightLine = new Pose2d(-46, 40, Math.toRadians(270));

        // Blue Backstage
        Pose2d blueBackStartingPose = new Pose2d(12, 61, Math.toRadians(270));
        Pose2d blueBackLeftLine = new Pose2d(22.5, 40, Math.toRadians(270));
        Pose2d blueBackCenterLine = new Pose2d(12, 36, Math.toRadians(270));
        Pose2d blueBackRightLine = new Pose2d(13.5, 32, Math.toRadians(180));

        // Waypoints
        Pose2d redStartingPoseReturnOffset = new Pose2d(0, 3, 0);
        Pose2d redBackStageWaypoint = new Pose2d(36, -58, Math.toRadians(90));
        Pose2d blueStartingPoseReturnOffset = new Pose2d(0, -3, 0);
        Pose2d blueBackStageWaypoint = new Pose2d(36, 58, Math.toRadians(270));

        // Backdrop
        Pose2d redBackdropLeft = new Pose2d(45, -30, Math.toRadians(0));;
        Pose2d redBackdropCenter = new Pose2d(45, -36, Math.toRadians(0));;
        Pose2d redBackdropRight = new Pose2d(45, -42, Math.toRadians(0));;
        Pose2d blueBackdropLeft = new Pose2d(45, 42, Math.toRadians(0));;
        Pose2d blueBackdropCenter = new Pose2d(45, 36, Math.toRadians(0));;
        Pose2d blueBackdropRight = new Pose2d(45, 30, Math.toRadians(0));;

        // Parking
        Pose2d redBackdropLeftParkWaypoint = new Pose2d(45, -14, Math.toRadians(180));
        Pose2d redBackdropLeftPark = new Pose2d(59, -11, Math.toRadians(180));
        Pose2d redBackdropRightParkWaypoint = new Pose2d(45, -58, Math.toRadians(180));
        Pose2d redBackdropRightPark = new Pose2d(59, -58, Math.toRadians(180));
        Pose2d blueBackdropLeftParkWaypoint = new Pose2d(45, 57, Math.toRadians(180));
        Pose2d blueBackdropLeftPark = new Pose2d(59, 58, Math.toRadians(180));
        Pose2d blueBackdropRightParkWaypoint = new Pose2d(45, 14, Math.toRadians(180));
        Pose2d blueBackdropRightPark = new Pose2d(59, 11, Math.toRadians(180));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redFrontStartingPose)
                                .splineTo(redFrontCenterLine.vec(), redFrontCenterLine.getHeading())
                                .waitSeconds(1.5)
                                .lineToSplineHeading(
                                        redFrontStartingPose.plus(new Pose2d(0, 3 , 0)))
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackStageWaypoint)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropCenter)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropRightParkWaypoint)
                                .splineToLinearHeading(redBackdropRightPark, redBackdropRightPark.getHeading())
                                .build()
                );

        // Red Front Left
        RoadRunnerBotEntity redFrontLeftBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redFrontStartingPose)
                                .lineToSplineHeading(redFrontLeftLine)
                                .waitSeconds(1.5)
                                .lineToSplineHeading(
                                        redFrontStartingPose.plus(redStartingPoseReturnOffset))
                                .waitSeconds(frontStageRobotBackdropDelay)
                                .lineToSplineHeading(redBackStageWaypoint)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropLeft)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropRightParkWaypoint)
                                .splineToLinearHeading(redBackdropRightPark, redBackdropRightPark.getHeading())
                                .build()
                );

        // Red Front Center
        RoadRunnerBotEntity redFrontCenterBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redFrontStartingPose)
                                .lineToSplineHeading(redFrontCenterLine)
                                .waitSeconds(1.5)
                                .lineToSplineHeading(
                                        redFrontStartingPose.plus(redStartingPoseReturnOffset))
                                .waitSeconds(frontStageRobotBackdropDelay)
                                .lineToSplineHeading(redBackStageWaypoint)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropCenter)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropRightParkWaypoint)
                                .splineToLinearHeading(redBackdropRightPark, redBackdropRightPark.getHeading())
                                .build()
                );

        // Red Front Right
        RoadRunnerBotEntity redFrontRightBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redFrontStartingPose)
                                .splineTo(redFrontRightLine.vec(), redFrontRightLine.getHeading())
                                .waitSeconds(1.5)
                                .lineToConstantHeading(
                                        redFrontStartingPose.plus(redStartingPoseReturnOffset).vec())
                                .waitSeconds(frontStageRobotBackdropDelay)
                                .lineToConstantHeading(redBackStageWaypoint.vec())
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropRight)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropRightParkWaypoint)
                                .splineToLinearHeading(redBackdropRightPark, redBackdropRightPark.getHeading())
                                .build()
                );

        // Red Back Left
        RoadRunnerBotEntity redBackLeftBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redBackStartingPose)
                                .splineTo(redBackLeftLine.vec(), redBackLeftLine.getHeading())
                                .waitSeconds(1.5)
                                .lineToConstantHeading(
                                        redBackStartingPose.plus(redStartingPoseReturnOffset).vec())
                                .waitSeconds(backStageRobotBackdropDelay)
                                .lineToConstantHeading(redBackStageWaypoint.vec())
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropLeft)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropLeftParkWaypoint)
                                .splineToLinearHeading(redBackdropLeftPark, redBackdropLeftPark.getHeading())
                                .build()
                );


        // Red Back Center
        RoadRunnerBotEntity redBackCenterBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redBackStartingPose)
                                .lineToSplineHeading(redBackCenterLine)
                                .waitSeconds(1.5)
                                .lineToSplineHeading(
                                        redBackStartingPose.plus(redStartingPoseReturnOffset))
                                .waitSeconds(backStageRobotBackdropDelay)
                                .lineToSplineHeading(redBackStageWaypoint)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropCenter)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropLeftParkWaypoint)
                                .splineToLinearHeading(redBackdropLeftPark, redBackdropLeftPark.getHeading())
                                .build()
                );

        // Red Back Right
        RoadRunnerBotEntity redBackRightBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(redBackStartingPose)
                                .lineToSplineHeading(redBackRightLine)
                                .waitSeconds(1.5)
                                .lineToSplineHeading(
                                        redBackStartingPose.plus(redStartingPoseReturnOffset))
                                .waitSeconds(backStageRobotBackdropDelay)
                                .lineToSplineHeading(redBackStageWaypoint)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropRight)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(redBackdropLeftParkWaypoint)
                                .splineToLinearHeading(redBackdropLeftPark, redBackdropLeftPark.getHeading())
                                .build()
                );

        // Blue Front Left
        RoadRunnerBotEntity blueFrontLeftBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueFrontStartingPose)
                                .splineTo(blueFrontLeftLine.vec(), blueFrontLeftLine.getHeading())
                                .waitSeconds(1.5)
                                .lineToConstantHeading(
                                        blueFrontStartingPose.plus(blueStartingPoseReturnOffset).vec())
                                .waitSeconds(frontStageRobotBackdropDelay)
                                .lineToConstantHeading(blueBackStageWaypoint.vec())
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropLeft)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropLeftParkWaypoint)
                                .splineToLinearHeading(blueBackdropLeftPark, blueBackdropLeftPark.getHeading())
                                .build()
                );

        // Blue Front Center
        RoadRunnerBotEntity blueFrontCenterBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueFrontStartingPose)
                                .lineToSplineHeading(blueFrontCenterLine)
                                .waitSeconds(1.5)
                                .lineToSplineHeading(
                                        blueFrontStartingPose.plus(blueStartingPoseReturnOffset))
                                .waitSeconds(frontStageRobotBackdropDelay)
                                .lineToSplineHeading(blueBackStageWaypoint)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropCenter)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropLeftParkWaypoint)
                                .splineToLinearHeading(blueBackdropLeftPark, blueBackdropLeftPark.getHeading())
                                .build()
                );

        // Blue Front Right
        RoadRunnerBotEntity blueFrontRightBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueFrontStartingPose)
                                .lineToSplineHeading(blueFrontRightLine)
                                .waitSeconds(1.5)
                                .lineToConstantHeading(
                                        blueFrontStartingPose.plus(blueStartingPoseReturnOffset).vec())
                                .waitSeconds(frontStageRobotBackdropDelay)
                                .lineToConstantHeading(blueBackStageWaypoint.vec())
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropRight)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropLeftParkWaypoint)
                                .splineToLinearHeading(blueBackdropLeftPark, blueBackdropLeftPark.getHeading())
                                .build()
                );

        // Blue Back Left
        RoadRunnerBotEntity blueBackLeftBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueBackStartingPose)
                                .lineToSplineHeading(blueBackLeftLine)
                                .waitSeconds(1.5)
                                .lineToConstantHeading(
                                        blueBackStartingPose.plus(blueStartingPoseReturnOffset).vec())
                                .waitSeconds(backStageRobotBackdropDelay)
                                .lineToConstantHeading(blueBackStageWaypoint.vec())
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropLeft)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropRightParkWaypoint)
                                .splineToLinearHeading(blueBackdropRightPark, blueBackdropRightPark.getHeading())
                                .build()
                );


        // Blue Back Center
        RoadRunnerBotEntity blueBackCenterBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueBackStartingPose)
                                .lineToSplineHeading(blueBackCenterLine)
                                .waitSeconds(1.5)
                                .lineToSplineHeading(
                                        blueBackStartingPose.plus(blueStartingPoseReturnOffset))
                                .waitSeconds(backStageRobotBackdropDelay)
                                .lineToSplineHeading(blueBackStageWaypoint)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropCenter)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropRightParkWaypoint)
                                .splineToLinearHeading(blueBackdropRightPark, blueBackdropRightPark.getHeading())
                                .build()
                );

        // Blue Back Right
        RoadRunnerBotEntity blueBackRightBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(26.5, 39.4, 2.46859,
                        Math.toRadians(188.22825), 15.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(blueBackStartingPose)
                                .splineTo(blueBackRightLine.vec(), blueBackRightLine.getHeading())
                                .waitSeconds(1.5)
                                .lineToConstantHeading(
                                        blueBackStartingPose.plus(blueStartingPoseReturnOffset).vec())
                                .waitSeconds(backStageRobotBackdropDelay)
                                .lineToConstantHeading(blueBackStageWaypoint.vec())
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropRight)
                                .waitSeconds(0.5)
                                .lineToSplineHeading(blueBackdropRightParkWaypoint)
                                .splineToLinearHeading(blueBackdropRightPark, blueBackdropRightPark.getHeading())
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                //.addEntity(myBot)
                // Randomized to left line
                //.addEntity(redBackLeftBot)
                //.addEntity(redFrontLeftBot)
                //.addEntity (blueBackLeftBot)
                //.addEntity(blueFrontLeftBot)
                // Randomized to center line
                //.addEntity(redBackCenterBot)
                //.addEntity(redFrontCenterBot)
                //.addEntity(blueBackCenterBot)
                //.addEntity(blueFrontCenterBot)
                // Randomized to right line
                .addEntity(redBackRightBot)
                .addEntity(redFrontRightBot)
                .addEntity(blueBackRightBot)
                .addEntity(blueFrontRightBot)
                .start();
    }
}