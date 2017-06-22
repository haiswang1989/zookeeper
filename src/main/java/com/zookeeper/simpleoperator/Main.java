package com.zookeeper.simpleoperator;

import org.apache.zookeeper.CreateMode;

public class Main {

    public static void main(String[] args) {
        
        SimpleApi simpleApi = new SimpleApi("192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183");
        
        String path = "/hello";
        String data = "hello zookeeper!";
        
        //创建一个临时结点
        simpleApi.createZNode(path, data, CreateMode.EPHEMERAL);
        
        //判断结点是否存在
        boolean isZNodeExist = simpleApi.isZNodeExist(path);
        System.out.println("Is path [" + path + "] exist : " + isZNodeExist);
        
        //获取结点的值
        String readData = simpleApi.getZNodeValue(path);
        System.out.println("readData : " + readData);
        
        //创建一个子节点
        //String childPath = path + "/child";
        //这边创建hello的子节点抛出异常,临时结点下不能创建子节点
        //simpleApi.createZNode(childPath, "", CreateMode.EPHEMERAL); //KeeperErrorCode = NoChildrenForEphemerals for /hello/child
        String persistentNode = "/persistent";
        simpleApi.deleteZNode(persistentNode);
        simpleApi.createZNode(persistentNode, "", CreateMode.PERSISTENT);
        
        String childPath = persistentNode + "/child";
        simpleApi.createZNode(childPath, "", CreateMode.EPHEMERAL);
        
        //这边直接删除/persistent结点会报错,因为该结点下面存在子节点"child"
        //simpleApi.deleteZNode(persistentNode); //KeeperErrorCode = Directory not empty for /persistent
        //可以用该api来递归删除存在子节点的结点
        simpleApi.deleteZNodeRecursive(persistentNode);
        
        
        String newData = "new data";
        //修改结点的内容
        simpleApi.writeData(path, newData);
        String readNewData = simpleApi.getZNodeValue(path);
        System.out.println("readNewData : " + readNewData);
        
        simpleApi.close();
    }

}
