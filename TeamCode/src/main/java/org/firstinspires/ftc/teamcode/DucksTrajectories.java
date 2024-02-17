package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class DucksTrajectories {

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

}
