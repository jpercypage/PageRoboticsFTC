package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsAUTO;


@Autonomous(name = "Auto park")
public class AutonomousModePark extends LinearOpMode {

    @Override
    public void runOpMode() {

        //  Motor hardware map initialization
        DriveMotorsAUTO motors = new DriveMotorsAUTO(hardwareMap, telemetry);

        waitForStart();

        motors.driveToTag();
        motors.run(DriveMotorsAUTO.RunModes.EXACT);

    }
}
