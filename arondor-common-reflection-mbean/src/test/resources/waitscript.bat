REM timeout /T 10 /NOBREAK
PING -n 5 -w 1000 127.1 >NUL
echo PLouf > target\script.out