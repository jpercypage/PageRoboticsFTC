package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DriveTrain;
import org.firstinspires.ftc.teamcode.TelemetrySystem;

@TeleOp(name = "Manual Mode")
public class Main extends OpMode {

    public static final String VERSION = "1.1.2";
    static DriveTrain driveTrain;
    static HardwareMap hardwareMap;
    static TelemetrySystem telemetry;


    @Override
    public void init() {
        telemetry = new TelemetrySystem(this);
        driveTrain = new DriveTrain();
    }

    @Override
    public void loop() {

    }

}