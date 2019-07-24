package com.zookeeper.simpleoperator;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @author hswang
 * @Date 2019-07-13 10:26
 */
public class TestMain {

    public static void main(String[] args) {
        String path = args[0];

        System.out.println("Path : " + path);

        ZkClient zkClient = new ZkClient("172.16.43.69:2181,172.16.43.70:2181,172.16.43.71:2181,172.16.43.72:2181,172.16.43.73:2181");


        diguidel(path, zkClient);
        System.out.println("over...");
    }

    /**
     *
     * @param path
     * @param zkClient
     */
    public static void diguidel(String path, ZkClient zkClient){
        List<String> childs = null;
        try {
            childs = zkClient.getChildren(path);
            if(null == childs || 0 == childs.size()) {
                boolean isSuccess = zkClient.delete(path);
                System.out.println(path + ":" + isSuccess);
            }
        } catch (Exception e) {
            return;
        }

        for(String child : childs) {
            diguidel(path + "/" + child, zkClient);
        }
    }
}
