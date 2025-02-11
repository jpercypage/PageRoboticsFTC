package org.firstinspires.ftc.teamcode.components.driveModes;

import static org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsAUTO.ticksPerDegree;
import static org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsAUTO.ticksPerInch;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Camera;
import org.firstinspires.ftc.teamcode.components.lifts.LiftAUTO;


public class DriveMotorsTELE {

    private int LEFTFRONT = 0;
    private int RIGHTFRONT = 1;
    private int RIGHTREAR = 2;
    private int LEFTREAR = 3;

    private double SPEED = 0.5;
    public boolean isBusy = false;
    DcMotor[] motors;
    private final DriveMotorsAUTO driveModeAuto;
    private final LiftAUTO liftAUTO;


    public DriveMotorsTELE(HardwareMap map, Telemetry tele) {

        this.driveModeAuto = new DriveMotorsAUTO(map, tele);
        this.liftAUTO = new LiftAUTO(map, tele);
        this.motors = this.driveModeAuto.motors;

    }


    /**
     * drives with a custom speed
     *
     * @param forward -1.00 = backwards. : 1.00 = forwards.
     * @param strafe  -1.00 = left. : 1.00 = right.
     * @param turn    -1.00 = rotate left. : 1.00 = rotate right.
     */
    public void controls(double forward, double strafe, double turn) {
        this.motors[LEFTFRONT]  .setPower((forward + strafe + turn) * SPEED);
        this.motors[RIGHTFRONT] .setPower((forward - strafe - turn) * SPEED);
        this.motors[RIGHTREAR]  .setPower((forward + strafe - turn) * SPEED);
        this.motors[LEFTREAR]   .setPower((forward - strafe + turn) * SPEED);
    }



    public void runAuto() {
        // Set isBusy to true so that no other inputs are handled until auto is done
        isBusy = true;

        // Drive to april tag and position under basket
        driveModeAuto.driveToTag();
        driveModeAuto.strafe(4);
        driveModeAuto.rotate(45);
        driveModeAuto.run(DriveMotorsAUTO.RunModes.EXACT);

        // Dump into top basket
        liftAUTO.raise();
        liftAUTO.dump();
        liftAUTO.lower();
        liftAUTO.run();

        // Get out of corner
        driveModeAuto.drive(-8);
        driveModeAuto.rotate(180);
        driveModeAuto.run(DriveMotorsAUTO.RunModes.SHORTESTPATH);

        isBusy = false;
    }




}
