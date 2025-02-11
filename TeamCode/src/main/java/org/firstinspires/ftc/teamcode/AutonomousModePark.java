package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.BigBertha;
import org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsAUTO;
import org.firstinspires.ftc.teamcode.components.lifts.LiftAUTO;

@Autonomous(name = "Auto park")
public class AutonomousModePark extends LinearOpMode {

    @Override
    public void runOpMode() {

        //  Motor hardware map initialization
        DriveMotorsAUTO motors = new DriveMotorsAUTO(hardwareMap);

        waitForStart();

        motors.drive(5);
        motors.rotate(90);
        motors.drive(38);

        motors.rotate(-90);
        motors.drive(-4);
        motors.run();

        //  Stops opMode from ending
        while(opModeIsActive()) {
            int three = 1 + 1;
        }
    }
}
