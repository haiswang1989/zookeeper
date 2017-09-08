package com.zookeeper.distributedlock.exception;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2017年9月8日 上午11:04:53
 */
public class LockNodeNotExistException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public LockNodeNotExistException(String message) {
        super(message);
    }
    
}
