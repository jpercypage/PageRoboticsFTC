package org.firstinspires.ftc.teamcode.components.driveModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DriveMotorsAUTO {

    private final int ticksPerRev = 1120;
    private final double ticksPerInch = 1120 / 9.27636;

    private final double ticksPerDegree = 30.05;

    private final DcMotor[] motors = new DcMotor[4];

    private final int BufferZone = 75;

    private enum motorType {
            LEFTFRONT,
            RIGHTFRONT,
            RIGHTREAR,
            LEFTREAR,


    };
    private ArrayList<String> Actions = new ArrayList<String>();

    public DriveMotorsAUTO(HardwareMap map) {
        try {
            // take in hardware map as param and then use that instead of our own
            this.motors[motorType.LEFTFRONT.ordinal()] = map.get(DcMotor.class, "frontLeft");
            this.motors[motorType.RIGHTFRONT.ordinal()] = map.get(DcMotor.class, "frontRight");
            this.motors[motorType.LEFTREAR.ordinal()] = map.get(DcMotor.class, "rearLeft");
            this.motors[motorType.RIGHTREAR.ordinal()] = map.get(DcMotor.class, "rearRight");
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init DriveMotors. Check connections or configuration naming"));

        }


        for (DcMotor motor: motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        motors[motorType.RIGHTFRONT.ordinal()].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[motorType.RIGHTREAR.ordinal()].setDirection(DcMotorSimple.Direction.REVERSE);

    }

    /**
     @param distance Measures in Inches, + forward, - backwards
     */
    public void drive(double distance) {
        this.Actions.add("drive:" + distance);
    }

    /**
        @param degrees Using Degrees. + clockwise, - counter-clockwise
     */
    public void rotate(double degrees) {
        this.Actions.add("rotate:" + degrees);
    }

    /**
     *
     * @param distance Measures in Inches. + left, - right
     */
    public void strafe(double distance) {
        this.Actions.add("strafe:" + distance);
    }
    private void RUNdrive(double distance) {
        for (DcMotor motor : motors) {
            motor.setTargetPosition(motor.getCurrentPosition() + (int) (ticksPerInch * distance));
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            motor.setPower(1);

        }
    }

    private void RUNstrafe(double distance) {
        for (int i = 0; i <= motors.length - 1; i++) {
            DcMotor motor = motors[i];
            if (i % 2 == 0) {
                motor.setTargetPosition(motor.getCurrentPosition() + (int) (ticksPerInch * distance));
            } else {
                motor.setTargetPosition(motor.getCurrentPosition() - (int) (ticksPerInch * distance));
            }
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.75);
        }
    }
    private void RUNrotate(double degrees) {

        for (DcMotor motor : motors) {
            if (motor.getDirection() == DcMotorSimple.Direction.REVERSE) {
                motor.setTargetPosition(motor.getCurrentPosition() + (int) -(ticksPerDegree * degrees));
            } else {
                motor.setTargetPosition(motor.getCurrentPosition() + (int) (ticksPerDegree * degrees));
            }
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1);
        }
    }



    public void run() {
        int index = 0;
        boolean waiting = false;
        while (index <= this.Actions.size() - 1) {

            if (!waiting) {
                String action = this.Actions.get(index).split(":")[0];
                double value = Double.parseDouble(this.Actions.get(index).split(":")[1]);

                if (action.contains("rotate")) {
                    this.RUNrotate(value);
                } else if (action.contains("drive")) {
                    this.RUNdrive(value);
                } else if (action.contains("strafe")) {
                    this.RUNstrafe(value);
                }
                waiting = true;
            } else if (reachedPosition()) {
                waiting = false;
                index += 1;
            }
        }
        this.Actions.clear();
    }

    private boolean reachedPosition() {
        boolean flag = true;
        while(flag) {
            int motorsReachedPosition = 0;
            for(DcMotor motor : motors) {
                if (motor.getCurrentPosition() >= motor.getTargetPosition() - (BufferZone) && motor.getCurrentPosition() <= motor.getTargetPosition() + (BufferZone)) {
                    motorsReachedPosition += 1;
                }
            }

            if (motorsReachedPosition >= 3) {
                flag = false;
            }
        }

        return true;
    }
}