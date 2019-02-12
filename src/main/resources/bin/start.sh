#!/bin/sh
##
## java env
JAVA_HOME=/usr/local/java/jdk1.8.0_152

## service name
APP_NAME=${project.artifactId}

SERVICE_DIR="$(cd "$(dirname $0)";pwd)"
SERVICE_NAME=$APP_NAME
JAR_NAME=$SERVICE_NAME\.jar
PID=$SERVICE_NAME\.pid
JAVA_OPTIONS="-Xms512m -Xmx1024m"
PROFILE=${environment}


do_start(){
    ENV=`which java 2>&1 | grep which`
	if [ ! -n "$ENV" ];then
	   nohup java $JAVA_OPTIONS -jar $JAR_NAME >/dev/null 2>&1 &
	else
	   nohup $JAVA_HOME/bin/java $JAVA_OPTIONS -jar $JAR_NAME >/dev/null 2>&1 &
	fi
    
    echo $! > $SERVICE_DIR/$PID
    echo "=== start $SERVICE_NAME"
}

 do_stop(){
    if [ -f $SERVICE_DIR/$PID ];then
	    P_ID=`cat $SERVICE_DIR/$PID`
	    ps -p $P_ID >/dev/null 2>&1
	    if [ "$?" = "0" ]; then
		  kill $P_ID
	    fi
	    rm -rf $SERVICE_DIR/$PID
	    echo "=== stop $SERVICE_NAME"
	fi

   sleep 15

    #再查询一遍，如果没有结束则强杀进程
	NPID=`ps aux | grep java |grep "$SERVICE_NAME".jar | awk '{print $2}'`
	if [ ! -n "$NPID"  ]; then
		echo "=== $SERVICE_NAME process not exists or stop success"
	else
		echo "=== $SERVICE_NAME process pid is:$NPID"
		echo "=== begin kill $SERVICE_NAME process, pid is:$NPID"
		kill -9 $NPID
	fi
}

cd ${SERVICE_DIR}

case "$1" in
    start)
		do_start
		;;
    stop)
	    do_stop
	    ;;
    restart)
	    do_stop
        sleep 2
        do_start
        echo "=== restart $SERVICE_NAME"
	    ;;
     *)
		echo "usage: $0 {start|stop|restart}"
		exit 1
        ;;
    esac
exit 0