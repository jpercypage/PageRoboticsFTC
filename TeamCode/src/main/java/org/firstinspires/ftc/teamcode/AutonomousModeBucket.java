package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.BigBertha;

import org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsAUTO;
import org.firstinspires.ftc.teamcode.components.lifts.LiftAUTO;

@Autonomous(name = "Auto bucket")
public class AutonomousModeBucket extends LinearOpMode {

    @Override
    public void runOpMode() {

        // initializations
        DriveMotorsAUTO motors = new DriveMotorsAUTO(hardwareMap, telemetry);
        BigBertha bertha = new BigBertha(hardwareMap, telemetry);
        LiftAUTO lift = new LiftAUTO(hardwareMap, telemetry);

        waitForStart();

        // new auto code ¯\_(ツ)_/¯ if works yet
        motors.drive(5);
        motors.rotate(90);
        motors.driveToTag();
        motors.rotate(-45);
        motors.drive(-2);
        motors.run();

        lift.raise();
        lift.dump();
        lift.lower();
        lift.run();

        /* old auto code 100% working
        motors.drive(8);
        motors.strafe(-45);
        motors.rotate(45);
        motors.run();
        */
    }
}
