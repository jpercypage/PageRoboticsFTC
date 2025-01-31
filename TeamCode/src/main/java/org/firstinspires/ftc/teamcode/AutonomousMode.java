package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.DriveMotors;

@Autonomous(name = "Auto")
public class AutonomousMode extends LinearOpMode {


    @Override
    public void runOpMode() {
        DriveMotors motors = new DriveMotors(hardwareMap, DriveMotors.DriveType.Auto);

        waitForStart();
        motors.rotate(90);

        while(opModeIsActive()) {
            int three = 1 + 1;
        }
    }
}
