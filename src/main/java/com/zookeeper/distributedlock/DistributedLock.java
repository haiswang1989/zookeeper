package com.zookeeper.distributedlock;

import java.util.Collections;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import com.zookeeper.distributedlock.exception.IllegalOperatorException;
import com.zookeeper.distributedlock.exception.LockNodeNotExistException;

/**
 * 分布式锁(实现比较简陋,大致的思想就是这样)
 * 1：所有想获取锁的线程,在指定的结点下创建子结点(结点的类型必须为EPHEMERAL_SEQUENTIAL)
 * 2：创建以后,获取父结点的所有子结点,然后按照生成的序号进行排序,编号最小的获取锁
 * 3：没有获取到锁的,就watcher排在它前面的结点
 * 4: 线程是否锁(删除结点),会notify后置的结点,这样后面的node就会获取到锁
 * 
 * 该种方式实现分布式锁比单纯的创建目录的方式去实现分布式锁的优点是避免了
 * "羊群效应",如果集群的规模比较庞大,使用单纯创建目录的方式,在notify阶段
 * 会影响集群的可用性
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2017年9月8日 上午10:45:30
 */
public class DistributedLock {
    
    //zookeeper的连接
    private ZkClient zkClient;
    
    //锁路径(父路径)
    private String lockPath;
    
    //子路径的前缀
    private String subPathPrefix = "lock_";
    
    //当前锁路径(包含父路径)如:$lockPath + "/" + $subPathPrefix + "***" 
    private String currPath;
    
    //子路径  如:$subPathPrefix + "***"
    private String childPathName; 
    
    /**
     * 构造函数
     * @param zkClient
     * @param path
     */
    public DistributedLock(ZkClient zkClient, String lockPath) {
        this.zkClient = zkClient;
        this.lockPath = lockPath;
    }
    
    /**
     * 获取锁
     */
    public void lock() {
        ensureExist(lockPath);
        currPath = this.zkClient.createEphemeralSequential(lockPath + "/" + subPathPrefix, "");
        childPathName = currPath.substring(lockPath.length() + 1);
        
        List<String> childsPath = this.zkClient.getChildren(lockPath);
        //按照编号排序,在前面的先拿到锁
        Collections.sort(childsPath);
        int index = childsPath.indexOf(childPathName);
        
        //如果是第一个最前面的,那么就直接获取到锁
        if(index != 0) {
            Object obj = new Object();
            //在它前面获取到锁的path
            String beforePath = childsPath.get(index-1);
            //watcher它前面一个path,当他前面一个线程拿到锁后释放的时候,来触发他获取到锁
            beforePath = lockPath + "/" + beforePath;
            this.zkClient.subscribeChildChanges(beforePath, new Listener(obj));
            synchronized (obj) { //如果没有获取到锁就堵塞住
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 释放锁
     */
    public void unlock() {
        ensureHasGetLock();
        //-1表示忽略版本
        this.zkClient.delete(currPath, -1);
    }
    
    /**
     * 
     */
    private void ensureHasGetLock() {
        if(null==currPath) {
            throw new IllegalOperatorException("Can not release lock hasn't locked by youself.");
        }
    }
    
    /**
     * 
     * @param path
     */
    private void ensureExist(String path) {
        boolean isExist = this.zkClient.exists(path);
        if(!isExist) {
            throw new LockNodeNotExistException("Lock node not exist : " + path);
        }
    }
}
