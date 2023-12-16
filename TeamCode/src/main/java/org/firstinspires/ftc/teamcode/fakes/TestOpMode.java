package org.firstinspires.ftc.teamcode.fakes;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.fakes.control.FakeGamepad;

import java.util.concurrent.TimeUnit;

public abstract class TestOpMode {
    public Gamepad gamepad1 = new FakeGamepad();

    /**
     * Gamepad 2
     */
    public Gamepad gamepad2 = new FakeGamepad();

    /**
     * The {@link #telemetry} field contains an object in which a user may accumulate data which
     * is to be transmitted to the driver station. This data is automatically transmitted to the
     * driver station on a regular, periodic basis.
     */
    public Telemetry telemetry = new FakeTelemetry();

    /**
     * Hardware Mappings
     */
    public HardwareMap hardwareMap = new FakeHardwareMap(null, null);

    /**
     * number of seconds this op mode has been running, this is
     * updated before every call to loop.
     */
    public double time = 0.0;

    // internal time tracking
    private long startTime = 0; // in nanoseconds

    /**
     * OpMode constructor
     * <p>
     * The op mode name should be unique. It will be the name displayed on the driver station. If
     * multiple op modes have the same name, only one will be available.
     */
    public TestOpMode() {
        startTime = System.nanoTime();
    }

    /**
     * User defined init method
     * <p>
     * This method will be called once when the INIT button is pressed.
     */
    abstract public void init();

    /**
     * User defined init_loop method
     * <p>
     * This method will be called repeatedly when the INIT button is pressed.
     * This method is optional. By default this method takes no action.
     */
    public void init_loop() {};

    /**
     * User defined start method.
     * <p>
     * This method will be called once when the PLAY button is first pressed.
     * This method is optional. By default this method takes not action.
     * Example usage: Starting another thread.
     *
     */
    public void start() {};

    /**
     * User defined loop method
     * <p>
     * This method will be called repeatedly in a loop while this op mode is running
     */
    abstract public void loop();

    /**
     * User defined stop method
     * <p>
     * This method will be called when this op mode is first disabled
     *
     * The stop method is optional. By default this method takes no action.
     */
    public void stop() {};

    /**
     * Requests that this OpMode be shut down if it the currently active opMode, much as if the stop
     * button had been pressed on the driver station; if this is not the currently active OpMode,
     * then this function has no effect. Note as part of this processing, the OpMode's {@link #stop()}
     * method will be called, as that is part of the usual shutdown logic. Note that {@link #requestOpModeStop()}
     * may be called from <em>any</em> thread.
     *
     * @see #stop()
     */
    public final void requestOpModeStop() { }

    /**
     * Immediately stops execution of the calling OpMode; and transitions to the STOP state.
     * No further code in the OpMode will execute once this has been called.
     */
    public final void terminateOpModeNow() {
        throw new OpModeManagerImpl.ForceStopException();
    }

    /**r
     * Get the number of seconds this op mode has been running
     * <p>
     * This method has sub millisecond accuracy.
     * @return number of seconds this op mode has been running
     */
    public double getRuntime() {
        final double NANOSECONDS_PER_SECOND = TimeUnit.SECONDS.toNanos(1);
        return (System.nanoTime() - startTime) / NANOSECONDS_PER_SECOND;
    }

    /**
     * Reset the runtime to zreo.
     */
    public void resetRuntime() {
        startTime = System.nanoTime();
    }

    //----------------------------------------------------------------------------------------------
    // Telemetry management
    //----------------------------------------------------------------------------------------------

    /**
     * Refreshes the user's telemetry on the driver station with the contents of the provided telemetry
     * object if a nominal amount of time has passed since the last telemetry transmission. Once
     * transmitted, the contents of the telemetry object are (by default) cleared.
     *
     * @param telemetry the telemetry data to transmit
     * @see #telemetry
     * @see Telemetry#update()
     */
    public void updateTelemetry(Telemetry telemetry) {
        telemetry.update();
    }


}
