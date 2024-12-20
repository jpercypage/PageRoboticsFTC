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
public class roughDriveTrain extends OpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    double strafe;
    double forward;
    double turn;
    Servo leftArmHinge;
    Servo rightArmHinge;
    Servo grabber;
    DcMotor liftMotor;
    double raising;
    Servo bucket;
    double lift;
    double position = 0.99D;
    final double HingeSpeed = 0.003D;

    public void init() {
        this.motor_init();
        this.hinge_init();
        this.bucket = (Servo)this.hardwareMap.get(Servo.class, "bucket");
        this.telemetry.addData("Bucket Servo: ", "Initialized");
        ZeroPowerBehavior at_zero = ZeroPowerBehavior.BRAKE;
        this.motor_set_zero(this.rightFront, Direction.REVERSE, at_zero);
        this.motor_set_zero(this.leftFront, Direction.FORWARD, at_zero);
        this.motor_set_zero(this.leftRear, Direction.FORWARD, at_zero);
        this.motor_set_zero(this.rightRear, Direction.REVERSE, at_zero);
        this.telemetry.addData("All Systems: ", "Functional");
    }

    public void loop() {
        this.strafe = (double)this.gamepad1.left_stick_x;
        this.forward = (double)(-this.gamepad1.left_stick_y);
        this.turn = (double)this.gamepad1.right_stick_x;
        this.wheel_controls();
        this.hinge_control();
        this.lift_control();
    }

    private void motor_init() {
        this.leftFront = (DcMotor)this.hardwareMap.get(DcMotor.class, "frontLeft");
        this.telemetry.addData("Front Left: ", "Initialized");
        this.rightFront = (DcMotor)this.hardwareMap.get(DcMotor.class, "frontRight");
        this.telemetry.addData("Front Right: ", "Initialized");
        this.leftRear = (DcMotor)this.hardwareMap.get(DcMotor.class, "rearLeft");
        this.telemetry.addData("Rear Left: ", "Initialized");
        this.rightRear = (DcMotor)this.hardwareMap.get(DcMotor.class, "rearRight");
        this.telemetry.addData("Rear Right: ", "Initialized");
        this.liftMotor = (DcMotor)this.hardwareMap.get(DcMotor.class, "liftMotor");
        this.telemetry.addData("Lift Motor: ", "Initialized");
        this.liftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    }

    private void wheel_controls() {
        this.leftFront.setPower((this.forward + this.strafe + this.turn) * 0.75D);
        this.rightFront.setPower((this.forward - this.strafe - this.turn) * 0.75D);
        this.leftRear.setPower((this.forward - this.strafe + this.turn) * 0.75D);
        this.rightRear.setPower((this.forward + this.strafe - this.turn) * 0.75D);
    }

    private void motor_set_zero(DcMotor motor, Direction dir, ZeroPowerBehavior at_zero) {
        motor.setDirection(dir);
        motor.setZeroPowerBehavior(at_zero);
    }

    private void hinge_init() {
        this.leftArmHinge = (Servo)this.hardwareMap.get(Servo.class, "lHinge");
        this.telemetry.addData("Left Hinge: ", "Initialized");
        this.rightArmHinge = (Servo)this.hardwareMap.get(Servo.class, "rHinge");
        this.telemetry.addData("Right Hinge: ", "Initialized");
        this.leftArmHinge.setPosition(1.0D);
        this.rightArmHinge.setPosition(1.0D);
        this.grabber = (Servo)this.hardwareMap.get(Servo.class, "grabber");
        this.telemetry.addData("Grabber Servo: ", "Initialized");
    }

    private void hinge_control() {
        this.lift = (double)(-this.gamepad2.left_stick_y);
        if (this.lift > 0.0D) {
            this.position += 0.003D;
        } else if (this.lift < 0.0D) {
            this.position -= 0.003D;
        }

        this.position = Math.max(0.0D, Math.min(1.0D, this.position));
        this.leftArmHinge.setPosition(this.position);
        this.rightArmHinge.setPosition(this.position);
        this.telemetry.addData("Position: ", this.position);
        if (this.gamepad2.left_bumper) {
            this.grabber.setPosition(1.0D);
        } else if (this.gamepad2.right_bumper) {
            this.grabber.setPosition(0.0D);
        } else {
            this.grabber.setPosition(0.5D);
        }

    }

    private void lift_control() {
        this.raising = (double)(-this.gamepad2.right_stick_y);
        this.liftMotor.setMode(RunMode.RUN_USING_ENCODER);
        if (this.raising > 0.5D) {
            this.liftMotor.setPower(this.raising / 2.5D);
        } else if (this.raising < -0.5D) {
            this.liftMotor.setPower(this.raising / 2.5D);
        } else {
            this.liftMotor.setPower(0.0D);
        }

        if (this.gamepad2.left_trigger > 0.0F) {
            this.bucket.setPosition(0.0D);
        } else if (this.gamepad2.right_trigger > 0.0F) {
            this.bucket.setPosition(1.0D);
        } else {
            this.bucket.setPosition(0.5D);
        }

    }
}

