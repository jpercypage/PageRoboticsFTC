package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DriveTrain;

@TeleOp(name = "Manual Mode")
public class Main extends OpMode {

    DriveTrain driveTrain;
    HardwareMap hardwareMap;

    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap);
    }

    @Override
    public void loop() {

    }

}