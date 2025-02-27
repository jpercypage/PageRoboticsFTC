package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(
        name = "Hinge Test"
)
public class Tester extends OpMode {


    int position;
    DcMotor lift;

    public void init() {
        lift = hardwareMap.get(DcMotor.class, "hinge");
        telemetry.addData("INIT", "MOVE ARM TO TOP POSITION BEFORE START");
        telemetry.update();
        position = 0;
    }

    public void loop() {
        
        if(position == 0) {
            lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        telemetry.addData("Position", lift.getCurrentPosition());
        telemetry.update();

        //telemetry.addData("RUNNING", "SLOWLY LOWERING ARM \n STOP WHEN ON GROUND");
        //position -= 10;
        //lift.setPosition(position);
        //lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //lift.setPower(0.5D);
        //sleep(500);
    }



}
