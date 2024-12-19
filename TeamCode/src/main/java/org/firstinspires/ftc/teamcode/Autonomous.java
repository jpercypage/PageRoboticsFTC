package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "Autonomous Mode")
public class Autonomous extends OpMode {
    DriveTrain driveTrain;
    HardwareMap hardwareMap;

    @Override
    public void init() {
        driveTrain = new DriveTrain();
    }

    @Override
    public void loop() {

    }
}
