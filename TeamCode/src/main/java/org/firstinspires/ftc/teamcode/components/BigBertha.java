package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BigBertha {

    private final Servo leftArmHinge;
    private final Servo rightArmHinge;
    private final Servo grabber;

    private final Telemetry tele;
    private double position;

    //constructor
    public BigBertha(HardwareMap map, Telemetry tele) {
        try {

            this.leftArmHinge = map.get(Servo.class, "lHinge");
            this.rightArmHinge = map.get(Servo.class, "rHinge");
            this.grabber = map.get(Servo.class, "grabber");

        } catch (Exception e) {
            throw new RuntimeException(new Throwable("Failed to init BigBertha. Check connections or configuration naming"));
        }
        this.tele = tele;
        this.position = 0.8D;
        this.leftArmHinge.setPosition(1.0D);
        this.rightArmHinge.setPosition(1.0D);
    }

    /**
     * Takes control input and moves the hinge based on it.
     * @param lift the control input
     */
    public void controls(double lift) {
        if (lift > 0.0D) {
            this.position += 0.003D;
        } else if (lift < 0.0D) {
            this.position -= 0.003D;
        }

        this.tele.addData("ServoPos:", this.rightArmHinge.getPosition());
        this.tele.update();

        this.position = Math.max(0.3D, Math.min(0.8D, position));

        this.leftArmHinge.setPosition(position);
        this.rightArmHinge.setPosition(position);
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
}
