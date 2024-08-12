package top.chengpei.filetransfer.command;

public abstract class AbstractCommand implements Command {

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
