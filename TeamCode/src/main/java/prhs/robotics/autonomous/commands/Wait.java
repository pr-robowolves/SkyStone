package prhs.robotics.autonomous.commands;

import com.qualcomm.robotcore.util.ElapsedTime;

import prhs.robotics.autonomous.Command;
import prhs.robotics.autonomous.CommandContext;

public class Wait implements Command {
    private double len;
    private ElapsedTime timer;

    // time is in seconds
    public Wait(double time) {
        this.len = time;
        this.timer = new ElapsedTime();
    }

    @Override
    public void run(CommandContext _ctx) {
        this.timer.reset();
    }

    @Override
    public boolean poll(CommandContext _ctx) {
        return this.timer.seconds() >= len;
    }
}
