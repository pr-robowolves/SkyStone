package prhs.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends OpMode {
    private static final double STEPS_PER_IN = 112.648;

    private DcMotor motor0;
    private DcMotor motor1;

    private int state = 0;
    /*
     * 0 = init
     * 1 = moving 12in
     * 2 = turning
     * 3 = moving 36in
     * 4 = done
     */

    private void reset_encoders() {
        this.motor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motor0.setTargetPosition(0);
        this.motor1.setTargetPosition(0);

        this.motor0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.motor0.setPower(1.0);
        this.motor1.setPower(1.0);
    }

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

        // Initialize and reset motors
        this.reset_encoders();

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

        // If motors aren't working...
        if (!this.motor0.isBusy() && !this.motor1.isBusy()) {
            this.reset_encoders();

            switch (state) {
                case 0:
                    this.motor0.setTargetPosition(-(int) (STEPS_PER_IN * 12.0));
                    this.motor1.setTargetPosition((int) (STEPS_PER_IN * 12.0));

                    this.state = 1;
                    break;

                case 1:
                    this.motor0.setTargetPosition(2880);
                    this.motor1.setTargetPosition(2880);

                    this.state = 2;
                    break;

                case 2:
                    this.motor0.setTargetPosition(-(int) (STEPS_PER_IN * 36.0));
                    this.motor1.setTargetPosition((int) (STEPS_PER_IN * 4636.0));

                    this.state = 3;
                    break;

                case 3:
                    break;
            }
        }

        this.telemetry.addData(
                "State",
                "%d",
                this.state
        );

        // Report motor data
        this.telemetry.addData(
                "Motor Speeds (0, 1)",
                "%.2f, %.2f",
                this.motor0.getPower(),
                this.motor1.getPower()
        );

        this.telemetry.addData(
                "Motor Positions (0, 1)",
                "%d, %d",
                this.motor0.getCurrentPosition(),
                this.motor1.getCurrentPosition()
        );
    }
}
