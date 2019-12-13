package prhs.robotics.autonomous.commands;

import java.util.Locale;

import prhs.robotics.autonomous.Command;
import prhs.robotics.autonomous.CommandContext;

import static prhs.robotics.util.Constants.STEPS_PER_IN;

public class Move implements Command {
    private Direction dir;
    private double distance;

    // Distance is in inches
    public Move(Direction d, double dist) {
        this.dir = d;
        this.distance = dist;
    }

    @Override
    public void run(CommandContext ctx) {
        int mfl_pos = ctx.m_front_l.getTargetPosition();
        int mfr_pos = ctx.m_front_r.getTargetPosition();
        int mbl_pos = ctx.m_back_l.getTargetPosition();
        int mbr_pos = ctx.m_back_r.getTargetPosition();

        switch (dir) {
            case FORWARDS:
                ctx.m_front_l.setTargetPosition(mfl_pos - (int) (STEPS_PER_IN * this.distance));
                ctx.m_front_r.setTargetPosition(mfr_pos + (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_l.setTargetPosition(mbl_pos - (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_r.setTargetPosition(mbr_pos + (int) (STEPS_PER_IN * this.distance));
                break;

            case BACKWARDS:
                ctx.m_front_l.setTargetPosition(mfl_pos + (int) (STEPS_PER_IN * this.distance));
                ctx.m_front_r.setTargetPosition(mfr_pos - (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_l.setTargetPosition(mbl_pos + (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_r.setTargetPosition(mbr_pos - (int) (STEPS_PER_IN * this.distance));
                break;

            case LEFT:
                ctx.m_front_l.setTargetPosition(mfl_pos + (int) (STEPS_PER_IN * this.distance));
                ctx.m_front_r.setTargetPosition(mfr_pos - (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_l.setTargetPosition(mbl_pos - (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_r.setTargetPosition(mbr_pos + (int) (STEPS_PER_IN * this.distance));
                break;

            case RIGHT:
                ctx.m_front_l.setTargetPosition(mfl_pos - (int) (STEPS_PER_IN * this.distance));
                ctx.m_front_r.setTargetPosition(mfr_pos + (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_l.setTargetPosition(mbl_pos + (int) (STEPS_PER_IN * this.distance));
                ctx.m_back_r.setTargetPosition(mbr_pos - (int) (STEPS_PER_IN * this.distance));
                break;
        }
    }

    // TODO: this could be improved
    @Override
    public boolean poll(CommandContext ctx) {
        return !ctx.m_front_l.isBusy()
                && !ctx.m_front_r.isBusy()
                && !ctx.m_back_l.isBusy()
                && !ctx.m_back_r.isBusy();
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Move %s %.2f feet", this.dir, this.distance);
    }
}
