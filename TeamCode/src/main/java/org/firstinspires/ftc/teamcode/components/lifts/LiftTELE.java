package org.firstinspires.ftc.teamcode.components.lifts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftTELE {

    private final Telemetry telemetry;
    private final DcMotor liftMotor;
    private final Servo bucket;
    public LiftTELE(HardwareMap map, Telemetry telemetry) {
        try {
            this.telemetry = telemetry;
            this.liftMotor = map.get(DcMotor.class, "liftMotor");
            this.bucket = map.get(Servo.class, "bucket");
        } catch (Exception e) {
            throw new RuntimeException(new Throwable("lift or bucket motor couldn't initialize. Check connections or configuration naming"));
        }

        this.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.telemetry.addData("Lift: ", this.liftMotor.getCurrentPosition());
        this.telemetry.update();
    }
    /**
     * Raises the lift to the top
     */
    public void raise() {

        this.liftMotor.setTargetPosition(-4050);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.liftMotor.setPower(0.5D);
    }

    /**
     * Raises the lift to the top
     * @param speed (Optional) determines the speed for raising the lift. 0.0 - 1.0  default is 0.5
     */
    public void raise(double speed) {
        this.telemetry.addData("Lift: ", this.liftMotor.getCurrentPosition());

        this.liftMotor.setTargetPosition(-4050);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.liftMotor.setPower(speed);

        this.telemetry.update();

    }

    /**
     * lowers the lift to the bottom
     */
    public void lower() {

        this.liftMotor.setTargetPosition(0);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.liftMotor.setPower(-0.3D);
    }

    /**
     * lowers the lift to the bottom
     * @param speed (Optional) determines the speed for lowering the lift. 0.0 - 1.0  default is 0.3
     */
    public void lower(double speed) {
        this.telemetry.addData("Lift: ", this.liftMotor.getCurrentPosition());

        this.liftMotor.setTargetPosition(0);
        this.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.liftMotor.setPower(-speed);

        this.telemetry.update();
    }

    /**
     * Dumps the bucket
     */
    public void dump() {
        this.bucket.setPosition(1.0D);
    }


    /**
     * Resets the bucket after dump
     */
    public void resetBucket() {
        this.bucket.setPosition(0.5D);
    }






}
