package org.firstinspires.ftc.teamcode;

import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

@TeleOp(name = "Manual Mode V2")
public class roughDriveTrain extends OpMode {

    //variables declarations for drive train motors
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;

    //wheel motion variables
    double strafe;
    double forward;
    double turn;


    @Override
    public void init() {

        //all wheel motors are in here. (Carlo)
        wheel_init();

        //wheels zero power behavior, it shouldn't change with the new version, hopefully
        DcMotor.ZeroPowerBehavior at_zero = DcMotor.ZeroPowerBehavior.BRAKE;
        motor_set_direction(rightFront, DcMotorSimple.Direction.REVERSE);
        motor_set_direction(leftFront, DcMotorSimple.Direction.FORWARD);
        motor_set_direction(leftRear, DcMotorSimple.Direction.FORWARD);
        motor_set_direction(rightRear, DcMotorSimple.Direction.REVERSE);


        telemetry.addData("All Systems: ", "Functional");

    }//end initializations

    //will run constantly
    public void loop() {

        //the different movements
        strafe = gamepad1.left_stick_x;
        forward = -gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;

        //the method to make the robot move
        wheel_controls();


    }//end loop method

    //wheel motor related methods
    private void wheel_init() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        telemetry.addData("Front Left: ", "Initialized");

        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        telemetry.addData("Front Right: ", "Initialized");

        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        telemetry.addData("Rear Left: ", "Initialized");

        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        telemetry.addData("Rear Right: ", "Initialized");

    }//end method
    private void wheel_controls() {

        //wheel movements
        leftFront.setPower(-(forward+strafe+turn) / 1.5);
        rightFront.setPower(-(forward-strafe-turn) / 1.5);
        leftRear.setPower(((forward-strafe+turn)) / 1.5);
        rightRear.setPower(((forward+strafe-turn)) / 1.5);

    }//end method
    private void motor_set_direction(DcMotor motor, DcMotorSimple.Direction dir) {

        //method that sets motor stop behavior and direction in which it spins when set to go forward (see driver hub config file)
        motor.setDirection(dir);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    
}// end whole class
