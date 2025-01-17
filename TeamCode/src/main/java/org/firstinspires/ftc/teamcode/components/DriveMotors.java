package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Objects;

public class DriveMotors {

    private final HashMap<String, DcMotor> motors = new HashMap<>();

    public DriveMotors(HardwareMap map) {
        try {
            // take in hardware map as param and then use that instead of our own
            this.motors.put("leftFront", map.get(DcMotor.class, "frontLeft"));
            this.motors.put("rightFront", map.get(DcMotor.class, "frontRight"));
            this.motors.put("leftRear", map.get(DcMotor.class, "rearLeft"));
            this.motors.put("rightRear", map.get(DcMotor.class, "rearRight"));
        } catch (Exception e) {
            //throw new RuntimeException(new Throwable("Failed to init DriveMotors. Check connections or configuration naming"));
            throw new RuntimeException(e);
        }


        getMotor("rightFront").setDirection(DcMotorSimple.Direction.REVERSE);
        getMotor("rightRear").setDirection(DcMotorSimple.Direction.REVERSE);

    }

    /**
     * drives with a custom speed
     *
     * @param forward -1.00 = max backwards. : 1.00 = max forwards.
     * @param strafe  -1.00 = max left. : 1.00 = max right.
     * @param turn    -1.00 = rotate max left. : 1.00 = rotate max right.
     */
    public void controls(double forward, double strafe, double turn) {

        getMotor("leftFront").setPower((forward + strafe + turn) * 0.75D);
        getMotor("rightFront").setPower((forward - strafe - turn) * 0.75D);
        getMotor("leftRear").setPower((forward - strafe + turn) * 0.75D);
        getMotor("rightRear").setPower((forward + strafe - turn) * 0.75D);
    }


    /**
     * drives with a custom speed
     *
     * @param forward -1.00 = max backwards. : 1.00 = max forwards.
     * @param strafe  -1.00 = max left. : 1.00 = max right.
     * @param turn    -1.00 = rotate max left. : 1.00 = rotate max right.
     * @param speed   0.00 = no movement. : 1.00 = full speed. Default value is 0.75. Full speed is not recommended.
     */
    public void controls(double forward, double strafe, double turn, double speed) {

        getMotor("leftFront").setPower((forward + strafe + turn) * speed);
        getMotor("rightFront").setPower((forward - strafe - turn) * speed);
        getMotor("leftRear").setPower((forward - strafe + turn) * speed);
        getMotor("rightRear").setPower((forward + strafe - turn) * speed);
    }


    public void driveFeet(double feet, double speed) {
        for (DcMotor motor : motors.values()) {
            motor.setPower(speed);
        }

    }

    private DcMotor getMotor(String motor) {
        return Objects.requireNonNull(this.motors.get(motor));
    }
}
