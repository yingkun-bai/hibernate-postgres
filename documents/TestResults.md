ybai@pclnX-gl152:~/workspace/yingkun-demos/target/logs$ grep "Total execution time for batch size" * | grep SEQUENCE
2024-08-16 15:20:24 [main] INFO app.MessageFactory - Total execution time for batch size 1 and type SEQUENCE: 35658 millis
2024-08-16 15:21:27 [main] INFO app.MessageFactory - Total execution time for batch size 5 and type SEQUENCE: 12106 millis
2024-08-16 15:22:24 [main] INFO app.MessageFactory - Total execution time for batch size 10 and type SEQUENCE: 9367 millis
2024-08-16 15:23:17 [main] INFO app.MessageFactory - Total execution time for batch size 20 and type SEQUENCE: 7713 millis
2024-08-16 15:24:09 [main] INFO app.MessageFactory - Total execution time for batch size 50 and type SEQUENCE: 6802 millis
2024-08-16 15:30:56 [main] INFO app.MessageFactory - Total execution time for batch size 100 and type SEQUENCE: 187667 millis
ybai@pclnX-gl152:~/workspace/yingkun-demos/target/logs$ grep "Total execution time for batch size" * | grep AUTO
2024-08-16 15:19:09 [main] INFO app.MessageFactory - Total execution time for batch size 1 and type AUTO: 39381 millis
2024-08-16 15:20:36 [main] INFO app.MessageFactory - Total execution time for batch size 5 and type AUTO: 11468 millis
2024-08-16 15:21:36 [main] INFO app.MessageFactory - Total execution time for batch size 10 and type AUTO: 8560 millis
2024-08-16 15:22:31 [main] INFO app.MessageFactory - Total execution time for batch size 20 and type AUTO: 6514 millis
2024-08-16 15:23:23 [main] INFO app.MessageFactory - Total execution time for batch size 50 and type AUTO: 5995 millis
2024-08-16 15:27:09 [main] INFO app.MessageFactory - Total execution time for batch size 100 and type AUTO: 179647 millis
ybai@pclnX-gl152:~/workspace/yingkun-demos/target/logs$ grep "Total execution time for batch size" * | grep IDENTITY
2024-08-16 15:19:49 [main] INFO app.MessageFactory - Total execution time for batch size 1 and type IDENTITY: 39807 millis
2024-08-16 15:21:15 [main] INFO app.MessageFactory - Total execution time for batch size 5 and type IDENTITY: 39263 millis
2024-08-16 15:22:15 [main] INFO app.MessageFactory - Total execution time for batch size 10 and type IDENTITY: 39413 millis
2024-08-16 15:23:10 [main] INFO app.MessageFactory - Total execution time for batch size 20 and type IDENTITY: 38692 millis
2024-08-16 15:24:02 [main] INFO app.MessageFactory - Total execution time for batch size 50 and type IDENTITY: 39062 millis
2024-08-16 15:27:48 [main] INFO app.MessageFactory - Total execution time for batch size 100 and type IDENTITY: 39355 millis
