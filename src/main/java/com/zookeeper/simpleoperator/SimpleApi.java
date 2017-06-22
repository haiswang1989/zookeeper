package com.zookeeper.simpleoperator;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 简单API测试
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2017年6月22日 上午11:43:15
 */
public class SimpleApi {
    
    private String zkServers;
    
    private ZkClient zkClient = null;
    
    public SimpleApi(String zkServers) {
        this.zkServers = zkServers;
        zkClient = new ZkClient(this.zkServers);
    }
    
    /**
     * 创建结点
     * @param path 目录
     * @param data 数据
     * @param createMode CreateMode.PERSISTENT(持久化的结点) CreateMode.EPHEMERAL(临时结点)
     */
    public void createZNode(String path, String data, CreateMode createMode) {
        this.zkClient.create(path, data, createMode);
    }
    
    /**
     * 获取结点的值
     * @param path
     * @return
     */
    public String getZNodeValue(String path) {
        Stat stat = new Stat(); //这个stat是干什么用的?
        String readData = this.zkClient.readData(path, stat);
        return readData;
    }
    
    /**
     * 获取结点是否存在
     * @param path
     */
    public boolean isZNodeExist(String path) {
        return this.zkClient.exists(path);
    }
    
    /**
     * 递归的删除
     * @param path
     */
    public void deleteZNodeRecursive(String path) {
        this.zkClient.deleteRecursive(path);
    }
    
    /**
     * 删除结点
     * 如果有子节点那么就会抛出异常
     * @param path
     */
    public void deleteZNode(String path) {
        this.zkClient.delete(path);
    }
    
    /**
     * 写入结点的内容
     * @param path
     * @param data
     */
    public void writeData(String path, String data) {
        this.zkClient.writeData(path, data);
    }
    
    /**
     * 监听子节点的变化
     * @param path
     * @param listener
     */
    public void subscribeChildChanges(String path, IZkChildListener listener) {
        this.zkClient.subscribeChildChanges(path, listener);
    }
    
    /**
     * 监听结点数据的变化
     * @param path
     * @param listener
     */
    public void subscribeDataChanges(String path, IZkDataListener listener) {
        this.zkClient.subscribeDataChanges(path, listener);
    }
    
    /**
     * 关闭连接
     */
    public void close() {
        zkClient.close();
    }
}
