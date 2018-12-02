# 分销商管理系统

##初始化系统步骤
1. 执行doc目录下的init.sql脚本
2. 执行doc目录下的init_data.sql脚本

##构建docker镜像
docker build --rm -t hhplms:1.0.0 .

##部署
docker run -tid --rm -v /root/devops/hhplms/logs:/root/logs -v /usr/share/nginx/html/upload:/root/static/img --env ACTIVE_PROFILE=test -p 12345:8080 --name hhplms hhplms:1.0.0