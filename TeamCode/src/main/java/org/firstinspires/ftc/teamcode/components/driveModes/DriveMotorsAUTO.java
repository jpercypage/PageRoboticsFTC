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

    private final double ticksPerDegree = 32.777;

    private final HashMap<String, DcMotor> motors = new HashMap<>();

    private final int BufferZone = 50;

    private ArrayList<String> Actions = new ArrayList<String>();




    public DriveMotorsAUTO(HardwareMap map) {
        try {
            // take in hardware map as param and then use that instead of our own
            this.motors.put("leftFront", map.get(DcMotor.class, "frontLeft"));
            this.motors.put("rightFront", map.get(DcMotor.class, "frontRight"));
            this.motors.put("leftRear", map.get(DcMotor.class, "rearLeft"));
            this.motors.put("rightRear", map.get(DcMotor.class, "rearRight"));
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init DriveMotors. Check connections or configuration naming"));

        }


        for (DcMotor motor: motors.values()) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        getMotor("rightFront").setDirection(DcMotorSimple.Direction.REVERSE);
        getMotor("rightRear").setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void drive(double distance) {
        this.Actions.add("drive:" + distance);
    }




    public void rotate(double degrees) {
        this.Actions.add("rotate:" + degrees);
    }

    private void RUNdrive(double distance) {
        for (DcMotor motor : motors.values()) {
            motor.setTargetPosition(motor.getCurrentPosition() + (int) (ticksPerInch * distance));
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.3);
        }
    }

    //Right
    private void RUNrotate(double degrees) {

        for (DcMotor motor : motors.values()) {
            if (motor.getDirection() == DcMotorSimple.Direction.REVERSE) {
                motor.setTargetPosition(motor.getCurrentPosition() + (int) -(ticksPerDegree * degrees));
            } else {
                motor.setTargetPosition(motor.getCurrentPosition() + (int) (ticksPerDegree * degrees));
            }
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.3);
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
                }
                waiting = true;
            } else if (reachedPosition()) {
                waiting = false;
                index += 1;
            }
        }
    }


    private DcMotor getMotor(String motor) {
        return Objects.requireNonNull(this.motors.get(motor));
    }

    private boolean reachedPosition() {
        boolean flag = true;
        while(flag) {
            int motorsReachedPosition = 0;
            for(DcMotor motor : motors.values()) {
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