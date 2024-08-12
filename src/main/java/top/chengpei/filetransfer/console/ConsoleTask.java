package top.chengpei.filetransfer.console;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.chengpei.filetransfer.console.runnable.TaskRunnable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsoleTask {

    private String command;

    private TaskRunnable runnable;
}
