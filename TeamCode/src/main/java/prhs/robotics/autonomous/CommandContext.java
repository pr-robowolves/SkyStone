package prhs.robotics.autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;

public class CommandContext {
    public DcMotor m_front_l;
    public DcMotor m_front_r;
    public DcMotor m_back_l;
    public DcMotor m_back_r;

    public double flipper_pos;

    public CommandContext(DcMotor mfl, DcMotor mfr, DcMotor mbl, DcMotor mbr, double fp) {
        this.m_front_l = mfl;
        this.m_front_r = mfr;
        this.m_back_l = mbl;
        this.m_back_r = mbr;

        this.flipper_pos = fp;
    }
}
