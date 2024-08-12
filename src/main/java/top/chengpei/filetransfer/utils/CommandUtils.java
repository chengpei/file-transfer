package top.chengpei.filetransfer.utils;

import cn.hutool.json.JSONUtil;
import top.chengpei.filetransfer.command.*;

public class CommandUtils {

    public static Class<? extends Command> getCommandType(int type) {
        switch (type) {
            case 0 : return AcquireFileRequestCommand.class;
            case 1 : return PrepareSendFileCommand.class;
            case 2 : return BeReadyReceiveFileCommand.class;
            case 3 : return CompleteFileReceiveCommand.class;
            default: throw new RuntimeException("未定义的类型");
        }
    }

    public static String encode(Command command) {
        return JSONUtil.toJsonStr(command);
    }
}
