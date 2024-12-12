package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.HashMap;

public class DriveTrain {

    //hashmap that stores all motors
    // [String: Motor Name] - [DcMotor: Motor Object]
    HashMap<String, DcMotor> motors = new HashMap<String, DcMotor>();

    //wheel motion variables
    //arranged strafe, forward, turn
    float[] whlMotionVars = {0, 0, 0};

    public DriveTrain(HardwareMap hardwareMap) {
        
    };

}
