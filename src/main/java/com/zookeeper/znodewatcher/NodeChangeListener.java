package com.zookeeper.znodewatcher;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.apache.zookeeper.CreateMode;

import com.zookeeper.simpleoperator.SimpleApi;

/**
 * 监听的结点,可以不存在
 * 
 * 创建监听结点
 * 删除监听结点
 * 删除子结点
 * 添加子节点
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2017年6月22日 下午2:50:13
 */
public class NodeChangeListener implements IZkChildListener {

    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        System.out.println("parentPath : " + parentPath); //监听路径
        for (String currentChild : currentChilds) {
            System.out.println("child : " + currentChild); //监听路径下的子路径
        }
    }
    
    public static void main(String[] args) {
        SimpleApi simpleApi = new SimpleApi("192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183");
        
        String path = "/nodewatcher";
        simpleApi.deleteZNodeRecursive(path);
        
        //监听"/nodewatcher"结点的变化(创建结点, 删除结点, 添加子节点, 删除子节点)
        //----监听的结点,可以不存在----
        simpleApi.subscribeChildChanges(path, new NodeChangeListener());
        
        //这边创建的时候会触发handleChildChange
        simpleApi.createZNode(path, "", CreateMode.PERSISTENT);
        
        //删除监听的结点,也会触发handleChildChange
        simpleApi.deleteZNode(path);
        
        //监听结点删除以后,再创建以后,就可以直接监听,无需再次subscribe
        simpleApi.createZNode(path, "", CreateMode.PERSISTENT);
        
        //添加子节点,触发handleChildChange
        String childNode = path + "/child";
        simpleApi.createZNode(childNode, "", CreateMode.EPHEMERAL);
        
        //删除子节点,触发handleChildChange
        simpleApi.deleteZNode(childNode);
        
        //同级创建结点,不会触发handleChildChange
        String newPath = "/newNodeWatcher";
        simpleApi.createZNode(newPath, "", CreateMode.EPHEMERAL);
        
        //修改结点的内容,不会触发handleChildChange
        simpleApi.writeData(path, "add data");
        
        simpleApi.createZNode(childNode, "", CreateMode.PERSISTENT);
        String childChildNode = childNode + "/child";
        //创建子节点的子节点也不会触发handleChildChange
        simpleApi.createZNode(childChildNode, "", CreateMode.EPHEMERAL);
        
        simpleApi.close();
    }
    
}
