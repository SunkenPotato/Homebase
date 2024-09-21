# Homebase Client
A cloud storage client I made for fun. 

Originally planned for private use. \
It uses the server [(name-here) Server](https://github.com/SunkenPotato/Cloud-server) to store files and authenticate

## Usage
You can download it, but it won't be any good since there's no server to connect it to :\
I might make one in the future, but I'm not sure tbh.

Clone the repo and compile it

Dependencies: Java 21

```shell
git clone https://github.com/SunkenPotato/P2-Client.git
./gradlew jlink
cd build/image/bin/
./app
# OR, if you're on Windows
start app.bat
```

By default, it uses the address `127.0.0.1:8000` for the server. You can configure it to something else though.
If you're really intent on using it, go and get the server.
You'll have to set up the database manually though as well as tables.

Have fun ¯\\\_(ツ)\_/¯
