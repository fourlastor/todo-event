export PROTO_PATH=app/src/main/proto/

java -jar wire-compiler-2.1.1-jar-with-dependencies.jar --proto_path=$PROTO_PATH --java_out=app/src/main/java `ls $PROTO_PATH`
