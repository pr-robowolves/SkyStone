package prhs.robotics;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import prhs.robotics.util.Motors;

@TeleOp(name="[USE THIS] Drive")
public class Drive extends OpMode {
    private DcMotor m_front_l; // port 0
    private DcMotor m_front_r; // port 1
    private DcMotor m_back_l;  // port 3
    private DcMotor m_back_r;  // port 4

    // Flipper Servos
    private Servo servo2;
    private Servo servo3;

    // Grabber servo
    private Servo servo4;

    // private double armPosition = 0.2; // Start arm slightly down
    private double flipperPosition = 0.5; // Start flippers open
    private double grabberPosition = 0.0;

    private ElapsedTime timer;

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

        this.servo2 = this.hardwareMap.get(Servo.class, "servo2");
        this.servo3 = this.hardwareMap.get(Servo.class, "servo3");
        this.servo4 = this.hardwareMap.get(Servo.class, "servo4");

        // Initialize and reset motors
        Motors.reset_motor(m_front_l, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motors.reset_motor(m_front_r, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motors.reset_motor(m_back_l, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motors.reset_motor(m_back_r, DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set servo directions
        this.servo2.setDirection(Servo.Direction.REVERSE);
        this.servo3.setDirection(Servo.Direction.FORWARD);
        this.servo4.setDirection(Servo.Direction.FORWARD);

        // Create timer
        this.timer = new ElapsedTime();

        // Initialization finished
        this.telemetry.addData("Status", "Ready");
    }

    @Override
    public void start() {
        // Clear telemetry data before beginning main loop
        this.telemetry.clearAll();

        // Reset timer
        this.timer.reset();
    }

    @Override
    public void loop() {
        // Clear telemetry
        this.telemetry.clearAll();

        // Read timer, save timescale
        double timescale = this.timer.seconds();
        this.timer.reset();

        this.telemetry.addData("Timescale", "%.2f", timescale);

        // Read gamepad values
        float js_1_lx = this.gamepad1.left_stick_x;
        float js_1_ly = this.gamepad1.left_stick_y;

        float js_1_rx = this.gamepad1.right_stick_x;
        float js_1_ry = this.gamepad1.right_stick_y;

        float gp_2_lt = this.gamepad2.left_trigger;
        float gp_2_rt = this.gamepad2.right_trigger;

        boolean gp_2_lb = this.gamepad2.left_bumper;
        boolean gp_2_rb = this.gamepad2.right_bumper;

        boolean gp_2_a = this.gamepad2.a;
        boolean gp_2_b = this.gamepad2.b;
        boolean gp_2_x = this.gamepad2.x;
        boolean gp_2_y = this.gamepad2.y;

        // Report values
        this.telemetry.addData(
                "GP1 Joystick Position",
                "(%.2f, %.2f)",
                js_1_lx,
                js_1_ly
        );

        this.telemetry.addData(
                "GP2 Triggers",
                "(%.2f, %.2f)",
                gp_2_lt,
                gp_2_rt
        );

        this.telemetry.addData(
                "GP2 Bumpers",
                "(%b, %b)",
                gp_2_lb,
                gp_2_rb
        );

        this.telemetry.addData(
                "GP2 Buttons",
                "%s%s%s%s",
                gp_2_a ? 'A' : 'a',
                gp_2_b ? 'B' : 'b',
                gp_2_x ? 'X' : 'x',
                gp_2_y ? 'Y' : 'y'
        );

        // Declare motor speeds
        float p_front_l = 0f;
        float p_front_r = 0f;
        float p_back_l = 0f;
        float p_back_r = 0f;

        // Traditional control (left stick)
        p_front_l += js_1_ly - js_1_lx;
        p_front_r += -js_1_ly - js_1_lx;
        p_back_l += js_1_ly - js_1_lx;
        p_back_r += -js_1_ly - js_1_lx;

        // Strafe control (right stick)
        p_front_l += js_1_ry - js_1_rx;
        p_front_r += -js_1_ry + js_1_rx;
        p_back_l += js_1_ry + js_1_rx;
        p_back_r += -js_1_ry - js_1_rx;

        // Write motor speeds
        this.m_front_l.setPower(p_front_l);
        this.m_front_r.setPower(p_front_r);
        this.m_back_l.setPower(p_back_l);
        this.m_back_r.setPower(p_back_r);

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

        // Update grabber position
        if (gp_2_lb) {
            this.grabberPosition += timescale;
        }
        if (gp_2_rb) {
            this.grabberPosition -= timescale;
        }

        // Update flipper position
        if (gp_2_a) {
            this.flipperPosition = 1.0;
        }
        if (gp_2_y) {
            this.flipperPosition = 0.5;
        }

        // Write grabber position to servo
        this.servo4.setPosition(this.grabberPosition);

        // Write flipper position to servos
        this.servo2.setPosition(this.flipperPosition);
        this.servo3.setPosition(this.flipperPosition);

        // Report servo positions
        this.telemetry.addData("Grabber Position", "%.2f", this.grabberPosition);
        this.telemetry.addData("Flipper Position", "%.2f", this.flipperPosition);
    }
}
