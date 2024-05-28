# databaseback
数据库备份脚本测试
#!/bin/bash

. /etc/profile
. ~/.bash_profile
# 主从虚拟机备份
# IP  根据项目需求修改 从虚拟机
IP=192.168.88.225 
# 用户命 从虚拟机
USER=root
# 密码 从虚拟机
PASSWORD=W7zqv076nz
# 待上传文件根目录 从虚拟机
CLIENTDIR=/home/mysqlbackup/
# 上传文件名称
FILENAME=back_`date +%F`.sql
# SFTP文件夹 主虚拟机
SERVERDIR="/home/mysqlbackup/"

# 将容器中的dump备份文件传到宿主机中
# 判断文件夹是否存在
if [ ! -d "$SERVERDIR" ]; then
  # 运行脚本逻辑
  mkdir "$SERVERDIR"
  docker exec mysql sh -c 'exec mysqldump --all-databases -uroot -pW7zqv076nz --all-databases' > /home/mysqlbackup/back_`date +%F`.sql
else
  docker exec mysql  sh -c 'exec mysqldump --all-databases -uroot -pW7zqv076nz --all-databases' > /home/mysqlbackup/back_`date +%F`.sql
fi

# 上传文件至备份虚拟机
lftp -u ${USER},${PASSWORD} sftp://${IP} <<EOF
mkdir ${CLIENTDIR}
cd ${CLIENTDIR} 
lcd ${CLIENTDIR}
put ${FILENAME}
by
EOF


# 对接致哥uptime接口
FILESIZE=$(stat --format=%s $CLIENTDIR/$FILENAME)
DATE=$(date)
NAME="MYSQL"
curl -X POST -H "Content-Type: application/json"  \
     -d "{\"dbName\":\"$NAME\",\"fileName\":\"$FILENAME\", \"fileSize\":\"$FILESIZE\",\"date\":\"$DATE\"}" \
     接口地址




-----------------------influxdb-------------------


#!/bin/bash

. /etc/profile
. ~/.bash_profile

# IP 根据项目需求修改
IP=192.168.88.225
# 用户命
USER=root
# 密码 根据实际情况修改
PASSWORD=W7zqv076nz
# 待上传文件根目录
CLIENTDIR=/home/influxdbbackup/
# 上传文件名称
FILENAME=back_`date +%F`
# SFTP文件夹
SERVERDIR="/home/influxdbbackup/"

# 判断文件夹是否存在
if [ ! -d "$SERVERDIR" ]; then
  # 运行脚本逻辑
  mkdir "$SERVERDIR"
  docker exec influxdb sh -c 'influxd backup -portable /path/to/back_`date +%F`;exit'
  docker cp influxdb:/path/to/back_`date +%F` /home/influxdbbackup/
  docker exec influxdb sh -c 'rm -rf /path/to/back_`date +%F`;exit'
else
  docker exec influxdb sh -c 'influxd backup -portable /path/to/back_`date +%F`;exit'
  docker cp influxdb:/path/to/back_`date +%F` /home/influxdbbackup/
  docker exec influxdb sh -c 'rm -rf /path/to/back_`date +%F`;exit'
fi

# 上传文件至备份虚拟机
lftp -u ${USER},${PASSWORD} sftp://${IP} <<EOF
mkdir ${CLIENTDIR}
cd ${CLIENTDIR}
lcd ${CLIENTDIR}
mirror -R ${FILENAME}
by
EOF

# 对接致哥uptime接口
FILESIZE=$(ls -l $CLIENTDIR/$FILENAME |awk '{sum += $5};END{print sum}')
DATE=$(date)
NAME="INFLUXDB"

curl -X POST -H "Content-Type: application/json"  \
     -d "{\"dbName\":\"$NAME\",\"fileName\":\"$FILENAME\", \"fileSize\":\"$FILESIZE\",\"date\":\"$DATE\"}" \
     接口地址
