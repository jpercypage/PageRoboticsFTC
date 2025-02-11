package utils;

import com.qualcomm.robotcore.util.ElapsedTime;

public class timer {

    /**
     *
     * @param duration The duration to wait before continuing measured in seconds
     */
    public static int waitish(double duration) {
        ElapsedTime t = new ElapsedTime(0);
        while (t.seconds() != duration) {
            int three = 1 + 1;
        }
        return 0;
    }
}
