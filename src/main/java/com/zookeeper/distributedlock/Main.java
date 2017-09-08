package com.zookeeper.distributedlock;

import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        
        String connectString = "10.199.167.139:2181,10.199.167.140:2181,10.199.167.141:2181";
        final ZkClient client = new ZkClient(connectString);
        final String lockPath = "/distributedlock";
        if(!client.exists(lockPath)) {
            client.createPersistent(lockPath);
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                DistributedLock distributedLock1 = new DistributedLock(client, lockPath);
                distributedLock1.lock();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                }
                distributedLock1.unlock();
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                DistributedLock distributedLock2 = new DistributedLock(client, lockPath);
                distributedLock2.lock();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                }
                distributedLock2.unlock();
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                DistributedLock distributedLock3 = new DistributedLock(client, lockPath);
                distributedLock3.lock();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                }
                distributedLock3.unlock();
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                DistributedLock distributedLock4 = new DistributedLock(client, lockPath);
                distributedLock4.lock();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                }
                distributedLock4.unlock();
            }
        }).start();
        
    }

}
