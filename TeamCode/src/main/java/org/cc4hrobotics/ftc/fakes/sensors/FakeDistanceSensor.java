package org.cc4hrobotics.ftc.fakes.sensors;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class FakeDistanceSensor implements DistanceSensor {

    private double distance;

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double getDistance(DistanceUnit unit) {
        return unit.fromUnit(DistanceUnit.CM,distance);
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "";
    }

    @Override
    public String getConnectionInfo() {
        return "";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
