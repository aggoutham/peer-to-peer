Peer-to-peer File Sharing system
github :- https://github.com/aggoutham/peer-to-peer.git

##Demo Videos are provided in the "demo" folder within the code.

#######################
Steps to instantiate the distributed system :-

1. Clone the git repository "peer-to-peer" or unzip the source code file "peer-to-peer.zip"

2. In the repo's directory run "mvn clean install" to create a new "target" directory.
(Maven is used as a build tool here to automate the dependencies and source packaging)

3. The jar "peer-to-peer-0.0.1-SNAPSHOT-jar-with-dependencies.jar" is the executable that will now be used to instantiate the entire system.

4. You need multiple hosts to enact the roles of a "central server" and multiple "peers". Say we have host1, host2, host3, host4 and host5.

5. Copy the "peer-to-peer-0.0.1-SNAPSHOT-jar-with-dependencies.jar" to all the hosts that needs to be part of the system.

6. Set up an environment variable "P2P_PATH" in each host that points to a custom "config.properties" file.
If Step 6 is not done, then the peer/server will use default config.properties within the source code.

7. Create random ".dat" files in "data_directory" mentioned in the configuration. This needs to be done for all peers who want to share something in the system.
Detailed layout of this data_directory and requirements are mentioned in the end of this file.

8. To Start CENTRAL SERVER, run the following command (on host1) -
java -cp peer-to-peer-0.0.1-SNAPSHOT-jar-with-dependencies.jar centralServer.StartCentral 2

9. To Start PEER, run the following command (on hosts 2,3,4 and 5) -
java -cp peer-to-peer-0.0.1-SNAPSHOT-jar-with-dependencies.jar peer.StartPeer 2

10. To Start PEER with "User Interactive Shell" use this command - 
java -cp peer-to-peer-0.0.1-SNAPSHOT-jar-with-dependencies.jar peer.StartPeer 1

Now the system is initiated with central server running on one host (host1) and peers running on 4 hosts (host2, host3, host4 and host5).

#######################


#######################
Configuration File :-

Following is the explanation to each parameter in the config.properties file -

1. authToken - A sample plain text password to authenticate every communication in the system. In real-world distributed system, this shall be more sophisticated.
2. peerID - The current peer's ID. Any integer can be given here (must be unique). Irrelevant for central server.
3. peerIP - The current peer's IP. Put the host's public IP address here. Irrelevant for central server.
4. peerListeningPort - The current peer's choice of Listening port. Any integer in port range should work. Irrelevant for central server.
5. data_directory - The directory in the peer's File System where all the shared Files and its chunks are stored. Irrelevant for central server.
6. centralIP - The central server's public address. Every peer needs to know this to call server.
7. centralPort - The central server's listening port. Every peer needs to know this to call server. The central server begins listening on this port.

#######################


#######################
Data Directory - Initiating random files and chunks :-

We followed a specific directory structure and naming convention of Files/Chunks in the distributed system to make demonstration simpler.
However, this is not a limitation to the functionality of the sharing system, any file can be incorporated to work with our system.

1. Every peer has a custom config.properties file that lists a directory in the key "data_directory".
2. Within this directory, you need to create multiple folders like (F101_S50, F102_S25, F103_S1, F104_S38,.....)
3. F101 is the name of the file the peer wanted to share. S50 would mean that the total size of the file in 50MB.
4. Within each directory "F101_S50/" we create a "F101_S50.dat" file that is a randomly generated 50MB file.
5. Split the file into chunks of 1MB with the name "chunk_F101_00, chunk_F101_01, chunk_F101_02 ....."

#######################




