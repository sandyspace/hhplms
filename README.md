# 分销商管理系统

##初始化系统步骤
1. 执行doc目录下的init.sql脚本
2. 执行doc目录下的init_data.sql脚本

##构建docker镜像
docker build --rm -t hhplms:1.0.0 .

##构建生产环境docker镜像
docker build --rm -t hhplms_prod:1.0.0 .

##部署
docker run -tid --rm -v /root/devops/hhplms/test/logs:/root/logs -v /home/wwwroot/test.iotecloud.com/client/cbgj/upload:/root/static/img --env ACTIVE_PROFILE=test -p 12345:8080 --name hhplms hhplms:1.0.0

##生产部署
docker run -tid --rm -v /root/devops/hhplms/prod/logs:/root/logs -v /home/wwwroot/iotecloud.com/client/cbgj/upload:/root/static/img --env ACTIVE_PROFILE=prod -p 10086:8080 --name hhplms_prod hhplms_prod:1.0.0
