package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BigBertha {

    public final DcMotor hinge;
    private final Servo grabber;

    private final Telemetry tele;
    private double position;

    // constructor
    public BigBertha(HardwareMap map, Telemetry tele) {
        try {

            this.hinge = map.get(DcMotor.class,"hinge");
            this.grabber = map.get(Servo.class, "grabber");

        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init BigBertha. Check connections or configuration naming"));
        }
        this.tele = tele;
      this.hinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    /**
     * Takes control input and moves the hinge based on it.
     * @param {double} The control input
     */
    public void controls(double lift) {
        if (lift > 0.0D) {
            this.position -= 0.003D;
        } else if (lift < 0.0D) {
            this.position += 0.003D;
        }


        if (lift != 0.0D) {
            this.position = Math.max(0.3D, Math.min(0.8D, position));
        }
    }

    public void deploy() {
        this.hinge.setTargetPosition(-75);
        this.hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.hinge.setPower(0.2);
    }
    public void retract() {
        this.hinge.setTargetPosition(0);
        this.hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.hinge.setPower(1);

    }
    /**
     * Grabs a block
     */
    public void grab() {
        this.grabber.setPosition(1.0D);
    }

    /**
     * Ejects a block
     */
    public void eject() {
        this.grabber.setPosition(0D);
    }

    /**
     * Turns off the servo
     */
    public void stop() {
        this.grabber.setPosition(0.5D);
    }

    /**
     * Sets the position of the left and right arm hinge
     * @param {double}
     */
    public void setPosition(double pos) {
        this.position = pos;

    }
}

