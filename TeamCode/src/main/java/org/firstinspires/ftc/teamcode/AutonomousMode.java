package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsAUTO;

@Autonomous(name = "Auto")
public class AutonomousMode extends LinearOpMode {


    @Override
    public void runOpMode() {
        DriveMotorsAUTO motors = new DriveMotorsAUTO(hardwareMap);

        waitForStart();

        // Actions //
        motors.drive(12);
        motors.rotate(90);


        // RUN //
        motors.run();
    }
}
