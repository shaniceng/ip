public abstract class Command {
    private boolean isExit;

    public Command() {
        this.isExit = false;
    }

    public void setExit() {
        this.isExit = true;
    }

    public boolean isExit() {
        return this.isExit;
    }

    public abstract void execute(TaskList t, Ui ui, Storage storage) throws DukeException;
}
