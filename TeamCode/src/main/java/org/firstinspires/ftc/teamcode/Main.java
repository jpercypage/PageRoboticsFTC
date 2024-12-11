package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveTrain;

@TeleOp(name = "Manual Mode")
public class Main extends OpMode {

    DriveTrain driveTrain;

    @Override
    public void init() {
        driveTrain = new DriveTrain();

    }

    @Override
    public void loop() {

    }

}
