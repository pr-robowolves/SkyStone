package prhs.robotics.autonomous.commands;

import java.util.Locale;

import prhs.robotics.autonomous.Command;
import prhs.robotics.autonomous.CommandContext;

public class SetFlippers implements Command {
    private double pos;

    public SetFlippers(double pos) {
        this.pos = pos;
    }

    @Override
    public void run(CommandContext ctx) {
        ctx.flipper_pos = pos;
    }

    // TODO: maybe wait for position to be reached
    @Override
    public boolean poll(CommandContext _ctx) {
        return true;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Move flippers to %.2f", this.pos);
    }
}
