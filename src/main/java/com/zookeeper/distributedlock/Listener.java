package com.zookeeper.distributedlock;

import java.util.List;
import org.I0Itec.zkclient.IZkChildListener;

/**
 * 结点的监听
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2017年6月22日 下午2:50:13
 */
public class Listener implements IZkChildListener {

    private Object obj;
    
    public Listener(Object obj) {
        this.obj = obj;
    }
    
    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        synchronized (obj) {
            obj.notifyAll();
        }
    }
}
