package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.BigBertha;
import org.firstinspires.ftc.teamcode.components.DriveMotors;
import org.firstinspires.ftc.teamcode.components.Lift;

@TeleOp(
        name = "Manual Mode V3"
)
public class DriveTrain extends OpMode {

    private DriveMotors motors;
    private Lift lift;
    private BigBertha bigBertha;

    public void init() {
        motors = new DriveMotors(hardwareMap);
        lift = new Lift(hardwareMap);
        bigBertha = new BigBertha(hardwareMap, telemetry);

        telemetry.addData("Init: ", "Complete");
        telemetry.update();
    }

    public void loop() {
        double strafe = gamepad1.left_stick_x;
        double forward = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        //Handles driving
        motors.controls(forward, strafe, turn);

        //Handles BigBertha lowering and raising
        bigBertha.controls(-gamepad2.left_stick_y);

        // Controls grabber
        if (gamepad2.left_bumper) {
            bigBertha.grab();
        } else if (gamepad2.right_bumper) {
            bigBertha.eject();
        } else {
            bigBertha.stop();
        }

        // raises and lowers the lift. Make sure string is tight and on the inside of spool before running
        if (gamepad2.x) {
            lift.raise(1);
        } else if (gamepad2.b) {
            lift.lower(0.9);
        }
        if (gamepad2.right_trigger > 0.0F) {
            lift.dump();
        } else {
            lift.resetBucket();
        }
    }
}


