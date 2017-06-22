package com.zookeeper.znodewatcher;

import org.I0Itec.zkclient.IZkDataListener;
import org.apache.zookeeper.CreateMode;

import com.zookeeper.simpleoperator.SimpleApi;

/**
 * 监听结点数据的变化
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2017年6月22日 下午4:10:22
 */
public class NodeDataChangeListener implements IZkDataListener {

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        //dataPath 数据变化的结点
        //data 变化后的值
        System.out.println("data change - " + dataPath + " : " + data);
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        //结点删除的时候才会触发该方法
        System.out.println("data delete - " + dataPath);
    }
    
    public static void main(String[] args) {
        
        SimpleApi simpleApi = new SimpleApi("192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183");
        
        String path = "/datachangewatcher";
        simpleApi.deleteZNode(path);
        
        simpleApi.createZNode(path, "", CreateMode.PERSISTENT);
        simpleApi.subscribeDataChanges(path, new NodeDataChangeListener());
        
        //触发handleDataChange
        simpleApi.writeData(path, "add data");
        
        //触发handleDataChange
        simpleApi.writeData(path, "");
        
        //触发handleDataDeleted
        simpleApi.deleteZNode(path);
        
        //触发handleDataChange
        simpleApi.createZNode(path, "", CreateMode.PERSISTENT);
        
        //不触发任何方法
        String childPath = path + "/child";
        simpleApi.createZNode(childPath, "", CreateMode.PERSISTENT);
        
        simpleApi.close();
    }
    
}
