package prhs.robotics.autonomous;

public interface Command {
    // Start executing command
    void run(CommandContext ctx);

    // Check if command is complete
    boolean poll(CommandContext ctx);
}
