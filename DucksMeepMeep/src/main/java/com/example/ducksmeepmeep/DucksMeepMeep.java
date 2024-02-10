package com.example.ducksmeepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
public class DucksMeepMeep {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        Pose2d redBackdropCenter = new Pose2d(45, -36, Math.toRadians(0));
        //Pose2d redFrontStage = new Pose2d();
        Pose2d startingPose = new Pose2d(-36, -60, Math.toRadians(90));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15.6)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startingPose)
                                .lineToSplineHeading(new Pose2d(-36, -42, Math.toRadians(45)))
                                //.splineTo(new Vector2d(-36, -42), Math.toRadians(45))
                                .turn(Math.toRadians(45))
                                .turn(Math.toRadians(45))
                                .lineToSplineHeading(startingPose)
                                .lineToSplineHeading(new Pose2d(36, -60, Math.toRadians(90)))
                                //.splineToLinearHeading(startingPose, Math.toRadians(90))
                                //.splineToLinearHeading(new Pose2d(36, -54, Math.toRadians(90)), Math.toRadians(90))
                                .splineToLinearHeading(redBackdropCenter, Math.toRadians(0))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}