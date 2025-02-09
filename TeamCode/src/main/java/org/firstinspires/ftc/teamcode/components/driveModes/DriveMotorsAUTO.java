package org.firstinspires.ftc.teamcode.components.driveModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Camera;

import java.util.ArrayList;

import utils.timer;

public class DriveMotorsAUTO {

    final int ticksPerRev = 1120;
    final double ticksPerInch = 1120 / 9.27636;

    double ticksPerDegree = 30.05;

    final int correctionScale = 3; // Higher number means better tag accuracy but slower code, lower number means worse tag accuracy but faster code
    final double MAXSPEED = 1;
    final int BufferZone = 50;

    final DcMotor[] motors = new DcMotor[4];
    Camera camera = null;

    int LEFTFRONT = 0;
    int RIGHTFRONT = 1;
    int RIGHTREAR = 2;
    int LEFTREAR = 3;

    enum actionType {
        DRIVE,
        ROTATE,
        STRAFE,
        TAG
    }

    enum RunModes {
        SHORTESTPATH,
        EXACT
    }
    RunModes RunMode;

    ArrayList<Double> ActionValues = new ArrayList<Double>();

    ArrayList<actionType> Actions = new ArrayList<actionType>();

    public DriveMotorsAUTO(HardwareMap map, Telemetry tele) {
        try {
            // take in hardware map as param and then use that instead of our own
            this.motors[LEFTFRONT] = map.get(DcMotor.class, "frontLeft");
            this.motors[RIGHTFRONT] = map.get(DcMotor.class, "frontRight");
            this.motors[LEFTREAR] = map.get(DcMotor.class, "rearLeft");
            this.motors[RIGHTREAR] = map.get(DcMotor.class, "rearRight");
            this.camera = new Camera(map, tele);
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init DriveMotors. Check connections or configuration naming"));
        }

        for (DcMotor motor: motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        motors[RIGHTFRONT].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[RIGHTREAR].setDirection(DcMotorSimple.Direction.REVERSE);

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
       @param distance Measures in Inches. + left, - right
     */
    public void strafe(double distance) {
        this.Actions.add(actionType.STRAFE);
        this.ActionValues.add(distance);
    }

    /**
     *  Detects april tag and then drives towards it
     */
    public void driveToTag() {
        this.Actions.add(actionType.TAG);
        this.ActionValues.add(0d);
    }

    void RUNdriveToTag() {

        int leftFrontDistance;
        int rightFrontDistance;
        int rightRearDistance;
        int leftRearDistance;

        for (int correction = correctionScale; correction >= correctionScale; correction--) {
            double[] DTS = this.camera.detectAprilTag();

            double degreesOfRotation  = (DTS[2] / correction) * ticksPerDegree;
            double LFRR_diagonal      = ((DTS[0] - DTS[3]) / correction) * ticksPerInch;
            double RFLR_diagonal      = ((DTS[0] + DTS[3]) / correction) * ticksPerInch;

            leftFrontDistance  = (int) (LFRR_diagonal + degreesOfRotation);
            rightRearDistance  = (int) (LFRR_diagonal - degreesOfRotation);

            rightFrontDistance = (int) (RFLR_diagonal - degreesOfRotation);
            leftRearDistance   = (int) (RFLR_diagonal + degreesOfRotation);

            setTargetPositions(new int[]{
                    leftFrontDistance,
                    rightFrontDistance,
                    rightRearDistance,
                    leftRearDistance
            });

            runMotors();
            waitTillReachedPosition();
        }
    }
    void RUNdrive(double distance) {
        int d = (int) (ticksPerInch * distance);
        setTargetPositions(new int[]{d, d, d, d});
        runMotors();
    }

    void RUNstrafe(double distance) {

        int d = (int) (distance * ticksPerInch);
        setTargetPositions(new int[]{d, -d, d, -d});
        runMotors(0.5);
    }
    void RUNrotate(double degrees) {
        int d = (int) (degrees * ticksPerDegree);
        setTargetPositions(new int[]{d, -d, -d, d});
        runMotors();
    }



    public void run() {
        int index = 0;
        boolean waiting = false;
        actionType action;
        double value;
        while (index <= Actions.size() - 1) {

            if (!waiting) {
                action = Actions.get(index);
                value  = ActionValues.get(index);

                switch (action) {
                    case DRIVE  :  RUNdrive(value);
                    case ROTATE :  RUNrotate(value);
                    case STRAFE :  RUNstrafe(value);
                    case TAG    :  RUNdriveToTag();
                }
                waiting = true;
            } else {
                waitTillReachedPosition();
                waiting = false;
                index += 1;
            }
        }
        this.Actions.clear();
    }

    void waitTillReachedPosition() {
        while(true) {
            int motorsReachedPosition = 0;
            for(DcMotor motor : motors) {
                if (motor.getCurrentPosition() >= motor.getTargetPosition() - (BufferZone) && motor.getCurrentPosition() <= motor.getTargetPosition() + (BufferZone) || !motor.isBusy()) {
                    motor.setPower(0);
                    motorsReachedPosition += 1;
                    if (motorsReachedPosition >= 3) {
                        return;
                    }
                }
            }
                timer.waitish(0.02);
        }
    }

    void setTargetPositions(int[] d) {
        this.motors[LEFTFRONT]  .setTargetPosition(this.motors[LEFTFRONT]  .getCurrentPosition() + d[0]);
        this.motors[RIGHTFRONT] .setTargetPosition(this.motors[RIGHTFRONT] .getCurrentPosition() + d[1]);
        this.motors[RIGHTREAR]  .setTargetPosition(this.motors[RIGHTREAR]  .getCurrentPosition() + d[2]);
        this.motors[LEFTREAR]   .setTargetPosition(this.motors[LEFTREAR]   .getCurrentPosition() + d[3]);
    }

    void runMotors(double speed) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(speed);
        }
    }

    void runMotors() {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(MAXSPEED);
        }
    }
}