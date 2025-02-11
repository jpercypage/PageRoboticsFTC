package org.firstinspires.ftc.teamcode.components.lifts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import utils.timer;


import java.util.ArrayList;


public class LiftAUTO {

    private final Telemetry telemetry;
    private final DcMotor liftMotor;
    private final Servo bucket;

    private final int BufferZone = 25;
    private enum actionType {
        RAISE,
        LOWER,
        DUMP,
    }

    private ArrayList<actionType> Actions = new ArrayList<actionType>();
    public LiftAUTO(HardwareMap map, Telemetry telemetry) {
        try {
            this.telemetry = telemetry;
            this.liftMotor = map.get(DcMotor.class, "liftMotor");
            this.bucket = map.get(Servo.class, "bucket");
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("lift or bucket motor couldn't initialize. Check connections or configuration naming"));
        }

        this.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }


    /**
     * Raises the lift to the top
     */
    public void raise() {
        this.Actions.add(actionType.RAISE);
    }


    /**
     * Dumps the bucket
     */
    public void dump() {
        this.Actions.add(actionType.DUMP);
    }


    /**
     * lowers the lift to the bottom
     */
    public void lower() {
        this.Actions.add(actionType.LOWER);
    }

    private void RUNraise() {

        this.liftMotor.setTargetPosition(-3900);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.liftMotor.setPower(1);

    }

    private void RUNlower() {

        this.liftMotor.setTargetPosition(0);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.liftMotor.setPower(-0.9D);
    }

    private void RUNdump() {
        this.bucket.setPosition(1.0D);
    }
    private void resetBucket() {
        this.bucket.setPosition(0.5D);
    }

    private boolean liftFinished() {
        boolean flag = true;
        while(flag) {
            if (this.liftMotor.getCurrentPosition() >= this.liftMotor.getTargetPosition() - (BufferZone) && this.liftMotor.getCurrentPosition() <= this.liftMotor.getTargetPosition() + (BufferZone)) {
                this.liftMotor.setPower(0);
                flag = false;
            }
        }
        return true;
    }


    public void run() {

        boolean waiting = false;
        int index = 0;
        actionType action = null;
        while (index <= this.Actions.size() - 1) {

            if (!waiting) {
                action = this.Actions.get(index);

                switch (action) {
                    case RAISE: RUNraise();
                    case DUMP: RUNdump();
                    case LOWER: RUNlower();
                }

                waiting = true;
            } else if (action != actionType.DUMP && this.liftFinished()) {
                    waiting = false;
                    index ++;

            } else {
                timer.waitish(1);
                waiting = false;
                index ++;
            }
        }
        this.Actions.clear();
    }
}