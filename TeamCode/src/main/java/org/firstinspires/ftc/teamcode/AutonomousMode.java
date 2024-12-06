package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class AutonomousMode extends OpMode {

    private Blinker control_Hub;
    private Blinker expansion_Hub;
    private DcMotor armHinge;
    private Servo handServo;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    WebcamName camera; // webcam object
    VisionPortal portal; // "vision portal object" (Carlo: I have no idea what this does fully yet)

    public void init() {

        //webcam
        camera = hardwareMap.get(WebcamName.class, "Webcam");

        // setting up new vision portal
        portal = new VisionPortal.Builder()
                .setCamera(camera)
                .build();
    }

    public void loop() {};

}
