package org.firstinspires.ftc.teamcode.components.driveModes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.CustomCamera;

import java.util.ArrayList;

import utils.timer;

public class DriveMotorsAUTO {

    final int ticksPerRev = 1120;
    static final double ticksPerInch = 1120 / 9.27636;

    static final double ticksPerDegree = 30.05;

    final int correctionScale = 3; // Higher number means better tag accuracy but slower code, lower number means worse tag accuracy but faster code
    final double MAXSPEED = 0.5;
    final int BufferZone = 50;

    final DcMotor[] motors = new DcMotor[4];
    CustomCamera customCamera;

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

    /**<p>
     * .SHORTESTPATH will optimise the actions at runtime to move to the same area in space using the shortest possible path.
     * best for open areas with little to no obstacles. shortest path will not run driveToTag() (yet)
     * </p>
     * <p>
     * .EXACT will run the actions exactly as specified at runtime. best for avoiding obstacles and using driveToTag()
     *</p>
     */
    public static enum RunModes {
        SHORTESTPATH,
        EXACT
    }
    RunModes RunMode;

    ArrayList<Double> ActionValues = new ArrayList<Double>();

    ArrayList<actionType> Actions = new ArrayList<actionType>();

    /**
     * <h1>Autonomous Drive Class</h1>
     * <p>Main object for handling autonomous driving</p>
     * @param map The HardwareMap object used in your AutoOPMode class
     * @param tele the Telemetry object used in your AutoOPMode class
     */
    public DriveMotorsAUTO(HardwareMap map, Telemetry tele) {
        try {
            // take in hardware map as param and then use that instead of our own
            this.motors[LEFTFRONT] = map.get(DcMotor.class, "frontLeft");
            this.motors[RIGHTFRONT] = map.get(DcMotor.class, "frontRight");
            this.motors[LEFTREAR] = map.get(DcMotor.class, "rearLeft");
            this.motors[RIGHTREAR] = map.get(DcMotor.class, "rearRight");
            this.customCamera = new CustomCamera(map, tele);
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init DriveMotors. Check connections or configuration naming"));
        }

        for (DcMotor motor: motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        motors[LEFTFRONT].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[RIGHTREAR].setDirection(DcMotorSimple.Direction.REVERSE);
        

    }

    /**
     * <p>Drives a certain distance on the y axis (forwards, backwards)</p>
     @param distance Measures in Inches
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
            double[] DTS = this.customCamera.detectAprilTag();

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


    /**
        @param mode the DriveMotorsAUTO.RunModes that dictates how this set of actions should be ran. See DriveMotorsAUTO.RunModes for a description of what they do
     */
    public void run(RunModes mode) {
        int index = 0;
        boolean waiting = false;
        actionType action;
        double value;
        this.RunMode = mode;
        switch (mode) {
            case EXACT:
                while (index <= Actions.size() - 2) {
                    action = Actions.get(index);
                    value = ActionValues.get(index);

                    switch (action) {
                        case DRIVE : RUNdrive(value);
                        case ROTATE: RUNrotate(value);
                        case STRAFE: RUNstrafe(value);
                        case TAG   : RUNdriveToTag();
                    }

                    waitTillReachedPosition();
                    index += 1;
                }


            case SHORTESTPATH:

                double totalRotation = 0;
                double totalDrive = 0;
                double totalStrafe = 0;

                while (index <= Actions.size() - 1) {
                    action = Actions.get(index);
                    value = ActionValues.get(index);

                    switch (action) {
                        case DRIVE : totalDrive += value;
                        case ROTATE: totalRotation += value;
                        case STRAFE: totalStrafe += value;
                    }
                }

                double degreesOfRotation  = (totalDrive) * ticksPerDegree;
                double LFRR_diagonal      = (totalDrive - totalStrafe) * ticksPerInch;
                double RFLR_diagonal      = (totalDrive + totalStrafe) * ticksPerInch;

                setTargetPositions(new int[]{
                        (int) (LFRR_diagonal + degreesOfRotation),
                        (int) (LFRR_diagonal - degreesOfRotation),
                        (int) (RFLR_diagonal - degreesOfRotation),
                        (int) (RFLR_diagonal + degreesOfRotation),
                });

                runMotors();
                waitTillReachedPosition();
        }
        this.Actions.clear();
    }



    private void waitTillReachedPosition() {
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

    private void setTargetPositions(int[] d) {
        this.motors[LEFTFRONT]  .setTargetPosition(this.motors[LEFTFRONT]  .getCurrentPosition() + d[0]);
        this.motors[RIGHTFRONT] .setTargetPosition(this.motors[RIGHTFRONT] .getCurrentPosition() + d[1]);
        this.motors[RIGHTREAR]  .setTargetPosition(this.motors[RIGHTREAR]  .getCurrentPosition() + d[2]);
        this.motors[LEFTREAR]   .setTargetPosition(this.motors[LEFTREAR]   .getCurrentPosition() + d[3]);
    }

    private void runMotors(double speed) {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(speed);
        }
    }

    private void runMotors() {
        for (DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(MAXSPEED);
        }
    }
}
