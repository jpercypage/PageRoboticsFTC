package org.firstinspires.ftc.teamcode;

import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;


@TeleOp(name = "Drive Train V1")
public class roughDriveTrain extends OpMode {

    //TODO: Create an array for drive train motors
    //variables declarations for drive train motors
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;

    // TODO: move arm code into a different class and import into drivetrain
    // servos
    Servo handServo;

    //arm motors
    DcMotor armHinge;
    DcMotor armExtender;

    //method that sets motor stop behavior and direction in which it spins when set to go forward (see driver hub config file)
    private void initialize_motor(DcMotor motor, DcMotorSimple.Direction dir, DcMotor.ZeroPowerBehavior at_zero) {

        motor.setDirection(dir);
        motor.setZeroPowerBehavior(at_zero);

    }

    private void wheel_init() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        telemetry.addData("Front Left: ", "Initialized");

        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        telemetry.addData("Front Right: ", "Initialized");

        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        telemetry.addData("Rear Left: ", "Initialized");

        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        telemetry.addData("Rear Right: ", "Initialized");

    }



    @Override
    public void init() {

        //all wheel motors are in here. (Carlo)
        wheel_init();

        //TODO: Change configurations to fit with new arm
        armHinge = hardwareMap.get(DcMotor.class, "armHinge");
        telemetry.addData("Arm: ", "Initialized");

        handServo = hardwareMap.get(Servo.class, "handServo");
        telemetry.addData("Grabber/Hand: ", "Initialized");

        armExtender = hardwareMap.get(DcMotor.class, "armExtender");
        telemetry.addData("Extender: ", "Initialized");

        //wheels zero power behavior
        DcMotor.ZeroPowerBehavior at_zero = DcMotor.ZeroPowerBehavior.BRAKE;
        initialize_motor(rightFront, DcMotorSimple.Direction.REVERSE, at_zero);
        initialize_motor(leftFront, DcMotorSimple.Direction.FORWARD, at_zero);
        initialize_motor(leftRear, DcMotorSimple.Direction.FORWARD, at_zero);
        initialize_motor(rightRear, DcMotorSimple.Direction.REVERSE, at_zero);

        armHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Systems: ", "OK");

    }

    double strafe;
    double forward;
    double turn;
    float armInput = 0;
    boolean extendArm = false;
    float armExtenderSpeed = 0;
    float rightRotation = 0;
    float leftRotation = 0;


    public void loop() {

        strafe = gamepad1.left_stick_x;
        forward = -gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;

        // arm raising (to be iterated later)
        armInput = gamepad2.right_stick_y / 12; // Positive down, negative = up. goes from -1 to 1

        //set arm hinge to run using encoder (the motor is to do its best to run at targeted velocity)
        armHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        float checkArmPosition = armHinge.getCurrentPosition();

        //arm extension (checks if you can extend it or not)
        if (checkArmPosition > -95) {
            extendArm = false; //when in between
        } else if (checkArmPosition < -95) {
            extendArm = true; // when its fully upward
        } else if (checkArmPosition > -10) {
            extendArm = false; //when completely horizontal
        }

        if (gamepad2.b == true) {
            armExtenderSpeed = -1;
        } else if (gamepad2.a == true) {
            armExtenderSpeed = 1;
        } else {
            armExtenderSpeed = 0;
        }

        armExtender.setPower(armExtenderSpeed);

        rightRotation = -gamepad2.right_trigger;
        leftRotation = -gamepad2.left_trigger;

        if (rightRotation != 0 && leftRotation == 0) {
            handServo.setPosition(0);
        } else if (leftRotation == 0 && rightRotation == 0) {
            handServo.setPosition(0.5);
        } else if (leftRotation != 0 && rightRotation == 0) {
            handServo.setPosition(1);
        } else if (leftRotation != 0 && rightRotation != 0) {
            handServo.setPosition(0);
        }

        telemetry.addData("is working: ", "true");
        telemetry.update();

        leftFront.setPower(-(forward+strafe+turn) / 1.5);
        rightFront.setPower(-(forward-strafe-turn) / 1.5);
        leftRear.setPower(((forward-strafe+turn)) / 1.5);
        rightRear.setPower(((forward+strafe-turn)) / 1.5);
    }
}// end whole class
