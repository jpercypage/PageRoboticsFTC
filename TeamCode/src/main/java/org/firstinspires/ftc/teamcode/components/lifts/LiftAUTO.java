package org.firstinspires.ftc.teamcode.components.lifts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.ServoHubConfiguration;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;


public class LiftAUTO {

    private final Telemetry telemetry;
    private final DcMotor liftMotor;
    private final Servo bucket;

    private final int BufferZone = 25;
    private final char LOWER = 'l';
    private final char RAISE = 'r';
    private final char DUMP = 'd';
    private final char RESET = 'c';
    private final int NONE = 0;
    private final int LIFT = 1;
    private final int BUCKET = 2;
    private final int RESETBUCKET = 3;

    private ServoHubConfiguration hub = new ServoHubConfiguration();
    private ArrayList<Character> Actions = new ArrayList<Character>();
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
        this.Actions.add(RAISE);
    }


    /**
     * Dumps the bucket
     */
    public void dump() {
        this.Actions.add(DUMP);
    }

    /**
     * Resets the bucket after dump
     */
    public void reset() {
        this.Actions.add(RESET);
    }

    /**
     * lowers the lift to the bottom
     */
    public void lower() {
        this.Actions.add(LOWER);
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
    private void RUNresetBucket() {
        this.bucket.setPosition(0.5D);
    }

    private boolean liftFinished() {
        boolean flag = true;
        while(flag) {
            if (this.liftMotor.getCurrentPosition() >= this.liftMotor.getTargetPosition() - (BufferZone) && this.liftMotor.getCurrentPosition() <= this.liftMotor.getTargetPosition() + (BufferZone)) {
                flag = false;
            }
        }
        return true;
    }

    private boolean bucketFinished(double d) {
        boolean flag = true;
        while(flag) {
            if (this.bucket.getController().getServoPosition(this.bucket.getPortNumber()) == d) {
                flag = false;
            }
        }
        return true;
    }


    public void run() {

        boolean waiting = false;
        int index = 0;
        int type = NONE;
        while (index <= this.Actions.size() - 1) {

            if (!waiting) {
                char action = this.Actions.get(index);

                if (action == RAISE) {
                    this.RUNraise();
                    type = LIFT;

                } else if (action == LOWER) {
                    this.RUNlower();
                    type = LIFT;

                } else if (action == DUMP) {
                    this.RUNdump();
                    type = BUCKET;

                } else if (action == RESET) {
                    this.RUNresetBucket();
                    type = RESETBUCKET;
                }

                waiting = true;
            } else if (type == LIFT && this.liftFinished()) {
                waiting = false;
                type = NONE;
                index ++;

            } else if (type == BUCKET && this.bucketFinished(1.0D)) {
                waiting = false;
                type = NONE;
                index++;

            } else if (type == RESETBUCKET && this.bucketFinished(0.5D)) {
                waiting = false;
                type = NONE;
                index++;
            }
        }
        this.Actions.clear();
    }
}