#!/usr/bin/env bash
java -Xms64m -Xmx128m -Xmn48m -jar portal-api.jar &

#java -Xms64m -Xmx128m -Xmn48m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:logs/gc.log -jar portal-api.jar &

## -Xms 初始堆大小； -Xmx 最大堆大小； -Xmn 年轻代大小
