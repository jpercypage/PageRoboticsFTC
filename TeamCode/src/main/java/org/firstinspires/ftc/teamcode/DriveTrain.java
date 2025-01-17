package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

@TeleOp(
        name = "Manual Mode V3"
)
public class DriveTrain extends OpMode {
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private double strafe;
    private double forward;
    private double turn;
    private Servo leftArmHinge;
    private Servo rightArmHinge;
    private Servo grabber;
    private DcMotor liftMotor;
    private Servo bucket;
    private double position = 0.99D;

    public void init() {
        motor_init();
        hinge_init();

        bucket = hardwareMap.get(Servo.class, "bucket");
        telemetry.addData("Bucket Servo: ", "Initialized");
        ZeroPowerBehavior at_zero = ZeroPowerBehavior.BRAKE;

        motor_set_zero(rightFront, Direction.REVERSE, at_zero);
        motor_set_zero(leftFront, Direction.FORWARD, at_zero);
        motor_set_zero(leftRear, Direction.FORWARD, at_zero);
        motor_set_zero(rightRear, Direction.REVERSE, at_zero);

        telemetry.addData("All Systems: ", "Functional");
    }

    public void loop() {
        strafe = gamepad1.left_stick_x;
        forward = -gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;
        wheel_controls();
        hinge_control();
        lift_control();
    }

    private void motor_init() {

        leftFront = hardwareMap.get(DcMotor.class, "frontLeft");
        telemetry.addData("Front Left: ", "Initialized");

        rightFront = hardwareMap.get(DcMotor.class, "frontRight");
        telemetry.addData("Front Right: ", "Initialized");

        leftRear = hardwareMap.get(DcMotor.class, "rearLeft");
        telemetry.addData("Rear Left: ", "Initialized");

        rightRear = hardwareMap.get(DcMotor.class, "rearRight");
        telemetry.addData("Rear Right: ", "Initialized");

        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        telemetry.addData("Lift Motor: ", "Initialized");

        liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
    }

    private void wheel_controls() {
        leftFront.setPower((forward + strafe + turn) * 0.75D);
        rightFront.setPower((forward - strafe - turn) * 0.75D);
        leftRear.setPower((forward - strafe + turn) * 0.75D);
        rightRear.setPower((forward + strafe - turn) * 0.75D);
    }

    private void motor_set_zero(DcMotor motor, Direction dir, ZeroPowerBehavior at_zero) {
        motor.setDirection(dir);
        motor.setZeroPowerBehavior(at_zero);
    }

    private void hinge_init() {
        leftArmHinge = hardwareMap.get(Servo.class, "lHinge");
        telemetry.addData("Left Hinge: ", "Initialized");

        rightArmHinge = hardwareMap.get(Servo.class, "rHinge");
        telemetry.addData("Right Hinge: ", "Initialized");

        grabber = hardwareMap.get(Servo.class, "grabber");
        telemetry.addData("Grabber Servo: ", "Initialized");

        leftArmHinge.setPosition(1.0D);
        rightArmHinge.setPosition(1.0D);

    }

    private void hinge_control() {
        double lift = -gamepad2.left_stick_y;
        if (lift > 0.0D) {
            position += 0.003D;
        } else if (lift < 0.0D) {
            position -= 0.003D;
        }

        position = Math.max(0.0D, Math.min(1.0D, position));
        leftArmHinge.setPosition(position);
        rightArmHinge.setPosition(position);


        if (gamepad2.left_bumper) {
            grabber.setPosition(1.0D);
        } else if (gamepad2.right_bumper) {
            grabber.setPosition(0.0D);
        } else {
            grabber.setPosition(0.5D);
        }

    }

    private void lift_control() {

        // Make sure string is tight and on the inside of spool before running


        if (gamepad2.x) {
            liftMotor.setTargetPosition(-4050);
            liftMotor.setMode(RunMode.RUN_TO_POSITION);
            liftMotor.setPower(0.5D);
        } else if (gamepad2.b) {
            liftMotor.setTargetPosition(0);
            liftMotor.setMode(RunMode.RUN_TO_POSITION);
            liftMotor.setPower(-0.3D);
        }

        telemetry.addData("lift: ", liftMotor.getCurrentPosition());
        telemetry.update();


        if (gamepad2.left_trigger > 0.0F) {
            bucket.setPosition(0.0D);
        } else if (gamepad2.right_trigger > 0.0F) {
            bucket.setPosition(1.0D);
        } else {
            bucket.setPosition(0.5D);
        }

    }
}

