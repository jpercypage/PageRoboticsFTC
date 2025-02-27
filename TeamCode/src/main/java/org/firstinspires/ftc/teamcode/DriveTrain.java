package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.components.BigBertha;
import org.firstinspires.ftc.teamcode.components.lifts.LiftTELE;
import org.firstinspires.ftc.teamcode.components.driveModes.DriveMotorsTELE;

@TeleOp(
        name = "Manual Mode V3"
)
public class DriveTrain extends OpMode {

    private DriveMotorsTELE motors;
    private LiftTELE lift;
    private BigBertha bigBertha;

    public void init() {
        motors = new DriveMotorsTELE(hardwareMap, telemetry);
        lift = new LiftTELE(hardwareMap, telemetry);
        bigBertha = new BigBertha(hardwareMap, telemetry);

        telemetry.addData("Init: ", "Complete");
        telemetry.update();
    }

    public void loop() {
        // Initialize double values for gamepad stick angles

        double strafe = gamepad1.left_stick_x;
        double forward = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;


        // If motors are doing auto ignore inputs
//        if(!motors.isBusy) {

            // Handles driving
            motors.controls(forward, strafe, turn);

            // Handles BigBertha lowering and raising



            // Controls grabber
            if (gamepad2.left_bumper) {
                bigBertha.grab();
            } else if (gamepad2.right_bumper) {
                bigBertha.eject();
            } else {
                bigBertha.stop();
            }

//            if (gamepad1.x) {
//                motors.runAuto();
//            }

            if (gamepad2.y){
                lift.raise();
            }

            else if(gamepad2.x){
                lift.lower();

            }
            if(gamepad2.dpad_up) {
                bigBertha.retract();
            }
            else if(gamepad2.dpad_down){
                bigBertha.deploy();
            }
            else if (gamepad2.a)
                lift.dump();
            }
//        }
    }



