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

        //initializations
        DriveMotorsAUTO motors = new DriveMotorsAUTO(hardwareMap);
        BigBertha bertha = new BigBertha(hardwareMap, telemetry);
        LiftAUTO lift = new LiftAUTO(hardwareMap, telemetry);


        waitForStart();


        lift.reset();
        lift.run();

        motors.drive(8);
        motors.strafe(-45);
        motors.rotate(45);
        motors.run();

        lift.raise();
        lift.dump();
        lift.run();

        //do not remove
        long jank = 0;
        while(opModeIsActive()) {
            jank += 1;
            if (jank >= 353500) {
                lift.reset();
                lift.lower();
                lift.run();

                motors.drive(20);
                motors.rotate(-45);
                motors.strafe(35);
                //motors.rotate(90);
                //motors.drive(14);

                motors.run();



                jank = -10000000000000000L;
            }// end if statement
        }//end while loop
    }//end run opp mode
}//end class
