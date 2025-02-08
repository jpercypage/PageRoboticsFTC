package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.BigBertha;

import org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsAUTO;
import org.firstinspires.ftc.teamcode.components.lifts.LiftAUTO;

@Autonomous(name = "Auto")
public class AutonomousMode extends LinearOpMode {

    @Override
    public void runOpMode() {

        //initializations
        DriveMotorsAUTO motors = new DriveMotorsAUTO(hardwareMap);
        BigBertha bertha = new BigBertha(hardwareMap, telemetry);
        LiftAUTO lift = new LiftAUTO(hardwareMap, telemetry);


        waitForStart();


        motors.drive(22);
        motors.rotate(-90);
        motors.drive(40);
        motors.rotate(135);
        motors.drive(-15);
        motors.run();

        lift.raise();
        lift.dump();
        lift.run();

        long jank = 0;
        while(opModeIsActive()) {
            jank += 1;
            if (jank >= 793500) {
                lift.reset();
                lift.lower();
                lift.run();
                jank = -10000000000000000L;
            }
        }
    }
}
