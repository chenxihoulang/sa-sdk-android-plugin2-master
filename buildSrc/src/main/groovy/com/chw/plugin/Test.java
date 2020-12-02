package com.chw.plugin;

import com.android.ddmlib.Log;

/**
 * @author ChaiHongwei
 * @date 2020-12-02 16:03
 */
public class Test {
    public void onCreate() {
        long startTime = System.currentTimeMillis();
        doSth();
        long endTime = System.currentTimeMillis() - startTime;
        StringBuilder sb = new StringBuilder();
        sb.append("com/sample/asm/SampleApplication.onCreate time: ");
        sb.append(endTime);
        Log.d("MethodCostTime", sb.toString());
    }

    private void doSth() {

    }
}
