package prhs.robotics.util;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Motors {
    public static void reset_motor(DcMotor motor, DcMotor.RunMode mode) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(mode);
    }
}
