package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

public class TelemetrySystem extends TelemetryImpl implements Telemetry {

    TelemetrySystem(OpMode opMode) {
        super(opMode);
        super.setAutoClear(false);
        super.addData("Telemetry System: ","Initialized");
    }

    void trace(Object... args) {
        super.addData("(%t) [TRCE]","", args);
    }

    void info(Object... args) {
        super.addData("[INFO]", "", args);
    }

    void warn(Object... args) {
        super.addData("[WARN]", "", args);
    }

    void error(Object... args) {
        super.addData("[ERR]", "", args);
    }

    void critical(Object... args) {
        super.addData("[CRIT]", "", args);
    }

}