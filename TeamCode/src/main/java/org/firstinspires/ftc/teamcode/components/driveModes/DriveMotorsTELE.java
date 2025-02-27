package org.firstinspires.ftc.teamcode.components.driveModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class DriveMotorsTELE {

    private int LEFTFRONT = 0;
    private int RIGHTFRONT = 1;
    private int RIGHTREAR = 2;
    private int LEFTREAR = 3;

    private double Buffer = 0.05;
    private double MAXSPEED = 0.7;
    public boolean isBusy = false;
    DcMotor[] motors;
    private final DriveMotorsAUTO driveModeAuto;
    //private final LiftAUTO liftAUTO;
    private final Telemetry tele;

    private double LEFTFRONTPOW = 0;
    private double RIGHTFRONTPOW = 0;
    private double RIGHTREARPOW = 0;
    private double LEFTREARPOW = 0;
    public DriveMotorsTELE(HardwareMap map, Telemetry tele) {
        this.tele = tele;
        this.driveModeAuto = new DriveMotorsAUTO(map, tele);
        //this.liftAUTO = new LiftAUTO(map, tele);
        this.motors = this.driveModeAuto.motors;

    }


    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));

    }

    public static double round(double value, int places) {
        if (value == 0) return 0;
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    /**
     * drives with a custom speed
     *
     * @param forward -1.00 = backwards. : 1.00 = forwards.
     * @param strafe  -1.00 = left. : 1.00 = right.
     * @param turn    -1.00 = rotate left. : 1.00 = rotate right.
     */
    public void controls(double forward, double strafe, double turn) {

        if (motors[0].getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            for (DcMotor motor : motors) {
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }


        if ((forward + strafe + turn) > 0.1 || (forward + strafe + turn) < -0.1) {
            tele.addData("", "ON");
            LEFTFRONTPOW  +=  (forward + strafe + turn) * Buffer;
            RIGHTFRONTPOW +=  (forward - strafe - turn) * Buffer;
            RIGHTREARPOW  +=  (forward + strafe - turn) * Buffer;
            LEFTREARPOW   +=  (forward - strafe + turn) * Buffer;

            LEFTFRONTPOW  =  clamp((float) LEFTFRONTPOW , -1, 1);
            RIGHTFRONTPOW =  clamp((float) RIGHTFRONTPOW, -1, 1);
            RIGHTREARPOW  =  clamp((float) RIGHTREARPOW , -1, 1);
            LEFTREARPOW   =  clamp((float) LEFTREARPOW  , -1, 1);

        } else {
            tele.addData("", "OFF");
            LEFTFRONTPOW  =  round(LEFTFRONTPOW  * Buffer, 2);
            RIGHTFRONTPOW =  round(RIGHTFRONTPOW * Buffer, 2);
            RIGHTREARPOW  =  round(RIGHTREARPOW  * Buffer, 2);
            LEFTREARPOW   =  round(LEFTREARPOW   * Buffer, 2);
        }

        tele.update();
        this.motors[LEFTFRONT]  .setPower(clamp(LEFTFRONTPOW, -MAXSPEED, MAXSPEED));
        this.motors[RIGHTFRONT] .setPower(clamp(RIGHTFRONTPOW, -MAXSPEED, MAXSPEED));
        this.motors[RIGHTREAR]  .setPower(clamp(RIGHTREARPOW, -MAXSPEED, MAXSPEED));
        this.motors[LEFTREAR]   .setPower(clamp(LEFTREARPOW, -MAXSPEED, MAXSPEED));
    }



//    public void runAuto() {
//        // Set isBusy to true so that no other inputs are handled until auto is done
//        isBusy = true;
//
//        // Drive to april tag and position under basket
//        driveModeAuto.driveToTag();
//        driveModeAuto.strafe(4);
//        driveModeAuto.rotate(45);
//        driveModeAuto.run(DriveMotorsAUTO.RunModes.EXACT);
//
//        // Dump into top basket
//        //liftAUTO.raise();
//        //liftAUTO.dump();
//        //liftAUTO.lower();
//        //liftAUTO.run();
//
//        // Get out of corner
//        driveModeAuto.drive(-8);
//        driveModeAuto.rotate(180);
//        driveModeAuto.run(DriveMotorsAUTO.RunModes.SHORTESTPATH);
//
//        isBusy = false;
//    }




}
