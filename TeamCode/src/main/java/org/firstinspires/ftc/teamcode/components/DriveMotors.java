package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveMotors {

    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftRear;
    private final DcMotor rightRear;


    public DriveMotors() {
        try {
            this.leftFront = hardwareMap.get(DcMotor.class, "frontLeft");
            this.rightFront = hardwareMap.get(DcMotor.class, "frontRight");
            this.leftRear = hardwareMap.get(DcMotor.class, "rearLeft");
            this.rightRear = hardwareMap.get(DcMotor.class, "rearRight");
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init DriveMotors. Check connections or configuration naming"));
        }

        this.rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        this.rightRear.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    /**
     drives with a custom speed
     @param forward -1.00 = max backwards. : 1.00 = max forwards.
     @param strafe -1.00 = max left. : 1.00 = max right.
     @param turn -1.00 = rotate max left. : 1.00 = rotate max right.
     */
    public void controls(double forward, double strafe, double turn) {

        leftFront.setPower((forward + strafe + turn) * 0.75D);
        rightFront.setPower((forward - strafe - turn) * 0.75D);
        leftRear.setPower((forward - strafe + turn) * 0.75D);
        rightRear.setPower((forward + strafe - turn) * 0.75D);
    }


    /**
     drives with a custom speed
     @param forward -1.00 = max backwards. : 1.00 = max forwards.
     @param strafe -1.00 = max left. : 1.00 = max right.
     @param turn -1.00 = rotate max left. : 1.00 = rotate max right.
     @param speed 0.00 = no movement. : 1.00 = full speed. Default value is 0.75. Full speed is not recommended.
     */
    public void controls(double forward, double strafe, double turn, double speed) {

        leftFront.setPower((forward + strafe + turn) * speed);
        rightFront.setPower((forward - strafe - turn) * speed);
        leftRear.setPower((forward - strafe + turn) * speed);
        rightRear.setPower((forward + strafe - turn) * speed);
    }

}
