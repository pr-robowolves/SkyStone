package prhs.robotics.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import prhs.robotics.util.Motors;

public abstract class Controller extends OpMode {
    public abstract Command nextCommand();

    private DcMotor m_front_l; // port 0
    private DcMotor m_front_r; // port 1
    private DcMotor m_back_l;  // port 3
    private DcMotor m_back_r;  // port 4

    private Command current_command;
    private CommandContext command_context;

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

        // Create command context
        this.command_context = new CommandContext(m_front_l, m_front_r, m_back_l, m_back_r);

        // Initialization finished
        this.telemetry.addData("Status", "Ready");
    }

    @Override
    public void start() {
        // Clear telemetry data before beginning main loop
        this.telemetry.clearAll();

        // Start first command
        this.current_command = this.nextCommand();
        if (this.current_command != null) {
            this.current_command.run(this.command_context);
        }
    }

    @Override
    public void loop() {
        // Clear telemetry
        this.telemetry.clearAll();

        // Poll current command for completion
        if (this.current_command != null && this.current_command.poll(this.command_context)) {
            // If complete, start next command if it exists
            this.current_command = this.nextCommand();
            if (this.current_command != null) {
                this.current_command.run(this.command_context);
            }
        }

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