package prhs.robotics;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.LinkedList;

import prhs.robotics.autonomous.Command;
import prhs.robotics.autonomous.Controller;
import prhs.robotics.autonomous.commands.Direction;
import prhs.robotics.autonomous.commands.Move;
import prhs.robotics.autonomous.commands.SetFlippers;
import prhs.robotics.autonomous.commands.Wait;

@Autonomous(name="Red Team Foundation Side")
public class AutoRedFoundation extends Controller {
    private LinkedList<Command> command_queue;

    @Override
    public void init() {
        this.command_queue = new LinkedList<>();

        this.command_queue.add(new Move(Direction.BACKWARDS, 35.0));
        this.command_queue.add(new SetFlippers(1.0));
        this.command_queue.add(new Wait(0.5));
        this.command_queue.add(new Move(Direction.FORWARDS, 24.0));
        this.command_queue.add(new SetFlippers(0.5));
        this.command_queue.add(new Wait(0.5));
        this.command_queue.add(new Move(Direction.LEFT, 48.0));

        super.init();
    }

    @Override
    public Command nextCommand() {
        return this.command_queue.pollFirst();
    }
}