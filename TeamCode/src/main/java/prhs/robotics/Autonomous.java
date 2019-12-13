package prhs.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import prhs.robotics.util.Motors;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends OpMode {
    private static final double STEPS_PER_IN = 118.801;

    private DcMotor m_front_l; // port 0
    private DcMotor m_front_r; // port 1
    private DcMotor m_back_l;  // port 3
    private DcMotor m_back_r;  // port 4

    private int state = 0;
    /*
     * 0 = init
     * 1 = moving 12in forward
     * 2 = moving 36in right
     * 3 = done
     */

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
        this.m_front_l = this.hardwareMap.get(DcMotor.class, "m_front_l");
        this.m_front_r = this.hardwareMap.get(DcMotor.class, "m_front_r");
        this.m_back_l = this.hardwareMap.get(DcMotor.class, "m_back_l");
        this.m_back_r = this.hardwareMap.get(DcMotor.class, "m_back_r");

        // Initialize and reset motors
        Motors.reset_motor(m_front_l, DcMotor.RunMode.RUN_TO_POSITION);
        Motors.reset_motor(m_front_r, DcMotor.RunMode.RUN_TO_POSITION);
        Motors.reset_motor(m_back_l, DcMotor.RunMode.RUN_TO_POSITION);
        Motors.reset_motor(m_back_r, DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor speeds
        this.m_front_l.setPower(0.5);
        this.m_front_r.setPower(0.5);
        this.m_back_l.setPower(0.5);
        this.m_back_r.setPower(0.5);

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
        if (!this.m_front_l.isBusy() && !this.m_front_r.isBusy() && !this.m_back_l.isBusy() && !this.m_back_r.isBusy()) {
            // AAA
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore :(
            }

            int mfl_pos = this.m_front_l.getTargetPosition();
            int mfr_pos = this.m_front_r.getTargetPosition();
            int mbl_pos = this.m_back_l.getTargetPosition();
            int mbr_pos = this.m_back_r.getTargetPosition();

            switch (state) {
                case 0:
                    this.m_front_l.setTargetPosition(mfl_pos - (int) (STEPS_PER_IN * 12.0));
                    this.m_front_r.setTargetPosition(mfr_pos + (int) (STEPS_PER_IN * 12.0));
                    this.m_back_l.setTargetPosition(mbl_pos - (int) (STEPS_PER_IN * 12.0));
                    this.m_back_r.setTargetPosition(mbr_pos + (int) (STEPS_PER_IN * 12.0));

                    this.state = 1;
                    break;

                case 1:
                    this.m_front_l.setTargetPosition(mfl_pos + (int) (STEPS_PER_IN * 36.0));
                    this.m_front_r.setTargetPosition(mfr_pos - (int) (STEPS_PER_IN * 36.0));
                    this.m_back_l.setTargetPosition(mbl_pos - (int) (STEPS_PER_IN * 36.0));
                    this.m_back_r.setTargetPosition(mbr_pos + (int) (STEPS_PER_IN * 36.0));

                    this.state = 2;
                    break;

                case 2:
                    this.state = 3;
                    break;
                    
                default:
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
                "Motor Speeds",
                "%.2f, %.2f, %.2f, %.2f",
                this.m_front_l.getPower(),
                this.m_front_r.getPower(),
                this.m_back_l.getPower(),
                this.m_back_r.getPower()
        );

        this.telemetry.addData(
                "Motor Positions",
                "%d, %d, %d, %d",
                this.m_front_l.getCurrentPosition(),
                this.m_front_r.getCurrentPosition(),
                this.m_back_l.getCurrentPosition(),
                this.m_back_r.getCurrentPosition()
        );
    }
}
