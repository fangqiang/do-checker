package cn.truthseeker.dochecker.exception;

/**
 * 检查字段条件失败时抛出该异常
 */
public class DoCheckException extends RuntimeException {
    public DoCheckException(String message) {
        super(message);
    }
}
