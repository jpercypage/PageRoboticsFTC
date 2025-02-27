package org.firstinspires.ftc.teamcode.components.lifts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import utils.timer;

public class LiftTELE {

    LiftAUTO lift;
    public LiftTELE(HardwareMap map, Telemetry telemetry) {
        this.lift = new LiftAUTO(map, telemetry);
    }
    /**
     * Raises the lift to the top
     */
    public void raise() {

        this.lift.RUNraise();
    }

    /**
     * lowers the lift to the bottom
     */
    public void lower() {
       this.lift.RUNlower();
    }

    /**
     * Dumps the bucket
     */
    public void dump() {
        lift.RUNdump();
    }
}
