package org.firstinspires.ftc.teamcode.components.driveModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.components.Camera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.xml.transform.dom.DOMLocator;

public class DriveMotorsAUTO {

    final int ticksPerRev = 1120;
    final double ticksPerInch = 1120 / 9.27636;

    double ticksPerDegree = 30.05;

    final DcMotor[] motors = new DcMotor[4];

    final int BufferZone = 75;

    Camera camera = null;
    enum motorType {
            LEFTFRONT,
            RIGHTFRONT,
            RIGHTREAR,
            LEFTREAR,

    };

    enum actionType {
        DRIVE,
        ROTATE,
        STRAFE
    }

    ArrayList<Double> ActionValues = new ArrayList<Double>();

    ArrayList<actionType> Actions = new ArrayList<actionType>();

    public DriveMotorsAUTO(HardwareMap map, Telemetry tele) {
        try {
            // take in hardware map as param and then use that instead of our own
            this.motors[motorType.LEFTFRONT.ordinal()] = map.get(DcMotor.class, "frontLeft");
            this.motors[motorType.RIGHTFRONT.ordinal()] = map.get(DcMotor.class, "frontRight");
            this.motors[motorType.LEFTREAR.ordinal()] = map.get(DcMotor.class, "rearLeft");
            this.motors[motorType.RIGHTREAR.ordinal()] = map.get(DcMotor.class, "rearRight");
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init DriveMotors. Check connections or configuration naming"));
        }

        try {
            this.camera = new Camera(map, tele);
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init Camera. Check connections or configuration naming"));
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

        this.Actions.add(actionType.DRIVE);
        this.ActionValues.add(distance);
    }

    /**
        @param degrees Using Degrees. + clockwise, - counter-clockwise
     */
    public void rotate(double degrees) {
        this.Actions.add(actionType.ROTATE);
        this.ActionValues.add(degrees);
    }

    /**
     *
     * @param distance Measures in Inches. + left, - right
     */
    public void strafe(double distance) {
        this.Actions.add(actionType.STRAFE);
        this.ActionValues.add(distance);
    }

    /*
        Detects april tag and then drives towards it
     */
    public void driveToTag() {


        for (int i = 3; i > 0; i--) {
            double[] DTS = this.camera.detectAprilTag();

            this.Actions.add(actionType.ROTATE);
            Actions.add(actionType.STRAFE);
            Actions.add(actionType.DRIVE);

            this.ActionValues.add(DTS[1] / i);
            ActionValues.add(DTS[2] / i);
            ActionValues.add(DTS[0] / i);

        }
    }
    void RUNdrive(double distance) {
        for (DcMotor motor : motors) {
            motor.setTargetPosition(motor.getCurrentPosition() + (int) (ticksPerInch * distance));
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            motor.setPower(1);

        }
    }

    void RUNstrafe(double distance) {
        for (int i = 0; i <= motors.length - 1; i++) {
            DcMotor motor = motors[i];
            if (i % 2 == 0) {
                motor.setTargetPosition(motor.getCurrentPosition() + (int) (ticksPerInch * distance));
            } else {
                motor.setTargetPosition(motor.getCurrentPosition() - (int) (ticksPerInch * distance));
            }
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.5);
        }
    }
    void RUNrotate(double degrees) {

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
                actionType action = Actions.get(index);
                double value = this.ActionValues.get(index);

                switch (action) {
                    case DRIVE: this.RUNdrive(value);
                    case ROTATE: this.RUNrotate(value);
                    case STRAFE: this.RUNstrafe(value);
                }
                waiting = true;
            } else if (reachedPosition()) {
                waiting = false;
                index += 1;
            }
        }
        this.Actions.clear();
    }

    boolean reachedPosition() {
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