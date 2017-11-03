package seedu.address.email;

//@@author awarenessxz
/**
 * Keeps track of Current Email Command Task
 */
public class EmailTask {
    public static final String TASKSEND = "send";
    public static final String TASKCLEAR = "clear";

    private String task;

    public EmailTask() {
        this.task = "";
    }

    /**
     * Returns the Task
     * @return
     */
    public String getTask() {
        return this.task;
    }

    /**
     * Sets the task on Email Command Run
     * @param task
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Checks if the task is valid
     * @return
     */
    public boolean isValid() {
        switch (this.task) {
        case TASKCLEAR:
            return true;
        case TASKSEND:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailTask // instanceof handles nulls
                && this.task.equals(((EmailTask) other).task));
    }
}
