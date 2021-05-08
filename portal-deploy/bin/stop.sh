#!/usr/bin/env bash
kill `netstat -nlp | grep :2257 | awk '{print $7}' | awk -F"/" '{ print $1 }'`

## 停止服务示例，示例中默认端口为7250，需要根据不同的环境更改不同的端口号
