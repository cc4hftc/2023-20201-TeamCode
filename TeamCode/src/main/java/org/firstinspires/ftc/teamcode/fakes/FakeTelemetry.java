package org.firstinspires.ftc.teamcode.fakes;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;

public class FakeTelemetry implements Telemetry {
    private boolean outputTelemetry = true;
    private int loopCount = 0;

    public void setOutputTelemetry(boolean flag) {
        outputTelemetry = flag;
    }
    private List<String> list = new ArrayList<String>();

    @Override
    public Item addData(String caption, String format, Object... args) {
        if (outputTelemetry) {
            String telemetryLine = (caption != null ? caption : "") + " " + String.format(format, args);
            list.add(telemetryLine);
        }

        return null;
    }

    @Override
    public Item addData(String caption, Object value) {
        if (outputTelemetry) {
            String telemetryLine = (caption != null ? caption : "") + " " + (value != null ? value.toString() : "");
            list.add(telemetryLine);
        }

        return null;
    }

    @Override
    public <T> Item addData(String caption, Func<T> valueProducer) {
        return null;
    }

    @Override
    public <T> Item addData(String caption, String format, Func<T> valueProducer) {
        return null;
    }

    @Override
    public boolean removeItem(Item item) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Object addAction(Runnable action) {
        return null;
    }

    @Override
    public boolean removeAction(Object token) {
        return false;
    }

    @Override
    public void speak(String text) {

    }

    @Override
    public void speak(String text, String languageCode, String countryCode) {

    }

    public void setLoopCount(int lc) {
        loopCount = lc;
    }

    @Override
    public boolean update() {
        if (outputTelemetry && !list.isEmpty()) {
            System.out.println("Telemetry Update (lc=" + loopCount + ")");
            System.out.println("--------------------------");
            for (String s: list ) {
                System.out.println(s);
            }
            System.out.println("");
            list.clear();
        }

        return true;
    }

    @Override
    public Line addLine() {
        return null;
    }

    @Override
    public Line addLine(String lineCaption) {
        return null;
    }

    @Override
    public boolean removeLine(Line line) {
        return false;
    }

    @Override
    public boolean isAutoClear() {
        return false;
    }

    @Override
    public void setAutoClear(boolean autoClear) {

    }

    @Override
    public int getMsTransmissionInterval() {
        return 0;
    }

    @Override
    public void setMsTransmissionInterval(int msTransmissionInterval) {

    }

    @Override
    public String getItemSeparator() {
        return null;
    }

    @Override
    public void setItemSeparator(String itemSeparator) {

    }

    @Override
    public String getCaptionValueSeparator() {
        return null;
    }

    @Override
    public void setCaptionValueSeparator(String captionValueSeparator) {

    }

    @Override
    public void setDisplayFormat(DisplayFormat displayFormat) {

    }

    @Override
    public Log log() {
        return null;
    }
}
