package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

public class DriveTrain {

    //hashmap that stores all motors
    // [String: Motor Name] - [DcMotor: Motor Object]
    HashMap<String, DcMotor> motors = new HashMap<String, DcMotor>();

    //wheel motion variables
    //internal arrangement: strafe, forward, turn
    float[] whlMotionVars = {0, 0, 0};

    public DriveTrain(HardwareMap hardwareMap) {
        String[] hwMotors = hardwareMap.getAllNames(DcMotor.class).toArray(new String[0]);

        // initializes motors
        for (String mtrName : hwMotors) {
            //maps DcMotor object to an actual hardware motor
            motors.put(mtrName, hardwareMap.get(DcMotor.class, mtrName));
            //sets the mapped motor logical direction of rotation
            motors.get(mtrName).setDirection(DcMotorSimple.Direction.FORWARD);
            //sets the mapped motor's zero power behavior
            motors.get(mtrName).setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

    };

}
