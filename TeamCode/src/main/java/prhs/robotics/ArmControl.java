package prhs.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ArmControl extends OpMode {
    private DcMotor motor0;
    private DcMotor motor1;

    private DcMotor armMotor;
    private Servo servo0;
    private Servo servo1;

    @Override
    public void init() {
        // Increase telemetry update speed
        // (may be removed in the future)
        this.telemetry.setMsTransmissionInterval(10);

        // Clear telemetry data
        this.telemetry.setAutoClear(false);
        this.telemetry.clearAll();

        // Begin initialization
        this.telemetry.addData("Status", "Initializing");

        // Get motors
        this.motor0 = this.hardwareMap.get(DcMotor.class, "motor0");
        this.motor1 = this.hardwareMap.get(DcMotor.class, "motor1");

        this.armMotor = this.hardwareMap.get(DcMotor.class, "motor2");

        this.servo0 = this.hardwareMap.get(Servo.class, "servo0");
        this.servo1 = this.hardwareMap.get(Servo.class, "servo1");

        // Initialize and reset motors
        this.motor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motor0.setTargetPosition(0);
        this.motor1.setTargetPosition(0);
        this.armMotor.setTargetPosition(0);

        this.motor0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialization finished
        this.telemetry.addData("Status", "Ready");
    }

    @Override
    public void start() {
        // Clear telemetry data before beginning main loop
        this.telemetry.clearAll();
    }

    @Override
    public void loop() {
        // Clear telemetry
        this.telemetry.clearAll();

        // Read joystick values
        float js_l_x = this.gamepad1.left_stick_x;
        float js_l_y = this.gamepad1.left_stick_y;

        float js_r_x = this.gamepad1.right_stick_x;
        float js_r_y = this.gamepad1.right_stick_y;

        // Report values
        this.telemetry.addData(
                "Left Joystick Position",
                "(%.2f, %.2f)",
                js_l_x,
                js_l_y
        );

        this.telemetry.addData(
                "Right Joystick Position",
                "(%.2f, %.2f)",
                js_r_x,
                js_r_y
        );

        // Write speeds to motors (calculates steering)
        this.motor0.setPower(js_l_y - js_l_x);
        this.motor1.setPower(-js_l_y - js_l_x);

        this.armMotor.setPower(js_r_y);

        // Report motor data
        this.telemetry.addData(
                "Motor Speed",
                "0: %.2f | 1: %.2f",
                this.motor0.getPower(),
                this.motor1.getPower()
        );

        this.telemetry.addData(
                "Motor Position",
                "0: %d | 1: %d",
                this.motor0.getCurrentPosition(),
                this.motor1.getCurrentPosition()
        );
    }
}
