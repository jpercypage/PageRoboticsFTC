package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsTELE;

@TeleOp(
        name = "Just drive"
)
public class JustDrive extends OpMode {
    DriveMotorsTELE motors;
    public void init() {
        motors = new DriveMotorsTELE(hardwareMap, telemetry);
    }

    public void loop(){
        double strafe = gamepad1.left_stick_x;
        double forward = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        //telemetry.addData("Strafe", strafe);
        //telemetry.addData("Forward", forward);
        //telemetry.addData("turn", turn);
        //telemetry.addData("All together", (strafe + forward + turn));
        //telemetry.update();
        motors.controls(-forward, strafe, turn);
    }
}
